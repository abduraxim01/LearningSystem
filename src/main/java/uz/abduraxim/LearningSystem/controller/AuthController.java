package uz.abduraxim.LearningSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<?> login(@RequestPart("username") String username,
                                   @RequestPart("password") String password) throws Exception {
        return ResponseEntity.ok(login.login(username, password));
    }
}
