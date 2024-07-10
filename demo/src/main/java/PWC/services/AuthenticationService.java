package PWC.services;

import PWC.entities.Role;
import PWC.entities.User;
import PWC.repository.UserRepository;
import PWC.requests.AuthenticationRequest;
import PWC.requests.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<Map<String, String>> register(RegisterRequest request) {
        User user = repository.findByEmail(request.getEmail());
        if (user != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Email already exists"));
        }

        user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .tel(request.getTel())
                .informations(request.getInformations())
                .isActive(request.getIsActive())
                .image(request.getImage())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf(request.getRole().name()))
                .build();
        repository.save(user);

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        log.debug("Generated JWT: {}", jwtToken);
        log.debug("Generated Refresh Token: {}", refreshToken);

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", jwtToken);
        response.put("refreshToken", refreshToken);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(response);
    }

    public ResponseEntity<Map<String, String>> authenticate(AuthenticationRequest request) {
        User user = repository.findByEmail(request.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Email not found"));
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            String jwtToken = jwtService.generateToken(user);
            log.debug("Generated JWT: {}", jwtToken);

            Map<String, String> response = new HashMap<>();
            response.put("accessToken", jwtToken);

            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Incorrect Password"));
        }
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String refreshToken = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            User user = repository.findByEmail(userEmail);
            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateToken(user);
                Map<String, String> authResponse = new HashMap<>();
                authResponse.put("accessToken", accessToken);
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
