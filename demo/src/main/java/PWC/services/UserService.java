package PWC.services;


import PWC.dto.UserDTO;
import PWC.entities.Post;
import PWC.entities.User;
import PWC.repository.PostRepository;
import PWC.repository.UserRepository;
import PWC.requests.ChangePasswordRequest;
import PWC.requests.ChangePasswordResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final UserRepository userRepository;
    private PostRepository postRepository;
    @NonNull
    HttpServletRequest request;
    public ChangePasswordResponse changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        ChangePasswordResponse response = new ChangePasswordResponse();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            response.setMessage("Wrong password");
            return response;
        }


        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);

        response.setMessage("Password changed successfully");
        return response;
    }

    public User getUserById(int id) {
        return repository.findById(id).orElse(null);
    }

    public UserDTO getUserDataByToken() {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        try {
            User user = repository.findByEmail(userEmail);
            if (user != null) {
                return UserDTO.fromEntity(user);
            }
        } catch (Exception e) {

            e.printStackTrace();


        }

        return null;
    }
    public List<User> getAllUsers() {
        return repository.findAll(); // Example using JPA repository

    }



}