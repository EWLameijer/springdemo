package org.ericwubbo.demo.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    record UserRegistrationDto(String username, String password) {}

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("users/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDto userRegistrationDto) {
        var username = userRegistrationDto.username.trim();
        if (username.isEmpty()) {
            var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "username should not be blank");
            return ResponseEntity.badRequest().body(problemDetail);
        }
        var password = userRegistrationDto.password;
        if (!isValid(password)) {
            var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "username should not be blank");
            return ResponseEntity.badRequest().body(problemDetail);
        }
        var possibleUser = userRepository.findByUsername(username);
        if (possibleUser.isPresent()) {
            var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "username already exists");
            return ResponseEntity.badRequest().body(problemDetail);
        }

    }

    private boolean isValid(String password) {
        if (password.trim().)
    }
}
