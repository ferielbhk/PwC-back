package PWC.controllers;

import PWC.dto.UserDTO;
import PWC.entities.User;
import PWC.requests.ChangePasswordRequest;
import PWC.requests.ChangePasswordResponse;
import PWC.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PatchMapping("/change-password")
    public ResponseEntity<ChangePasswordResponse> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        ChangePasswordResponse response = service.changePassword(request, connectedUser);

        if (response.getMessage().equals("Password changed successfully")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    @GetMapping("/getall")
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }
    @GetMapping("/{id}")
    public User getbyId (@PathVariable int id){return service.getUserById(id);}
    @GetMapping("/getDataByToken")
    public UserDTO getDataByTokenl() {
        return service.getUserDataByToken();
    }
}