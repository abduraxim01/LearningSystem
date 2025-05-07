package uz.abduraxim.LearningSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uz.abduraxim.LearningSystem.DTO.response.ResponseStructure;
import uz.abduraxim.LearningSystem.service.auth.AuthService;

@RestController
@RequestMapping(value = "/api")
public class AuthController {

    private final AuthService login;

    @Autowired
    public AuthController(AuthService login) {
        this.login = login;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<ResponseStructure> login(@RequestPart("username") String username,
                                                   @RequestPart("password") String password) {
        return ResponseEntity.ok(login.login(username, password));
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @GetMapping(value = "/getCurrentUser/{username}")
    public ResponseEntity<?> getCurrentUser(@PathVariable String username,
                                            Authentication authentication) {
        return ResponseEntity.ok(login.getCurrentUser(authentication, username));
    }
}
