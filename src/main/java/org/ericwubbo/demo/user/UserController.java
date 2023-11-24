package org.ericwubbo.demo.user;

import org.ericwubbo.demo.BadInputException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDto userRegistrationDto, UriComponentsBuilder ucb) {
        var username = getValidValueOrThrow(userRegistrationDto.username(), "username");
        var password = getValidValueOrThrow(userRegistrationDto.password(), "password");
        var possibleUser = userRepository.findByUsername(username);
        if (possibleUser.isPresent()) throw new BadInputException("username already exists");

        var newUser = new User(username, password);
        userRepository.save(newUser);
        URI locationOfNewUser = ucb
                .path("users/{username}")
                .buildAndExpand(newUser.getUsername())
                .toUri();
        return ResponseEntity.created((locationOfNewUser)).body(new UserRegistrationResultDto(newUser.getUsername()));
    }

    @GetMapping("{username}")
    public ResponseEntity<UserRegistrationResultDto> getUser(@PathVariable String username) {
        var possiblyFoundUser = userRepository.findByUsername(username);
        return possiblyFoundUser.map(user -> ResponseEntity.ok(new UserRegistrationResultDto(user.getUsername())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private static String getValidValueOrThrow(String rawString, String fieldname) {
        if (rawString == null) throw new BadInputException(fieldname + " is missing");
        var result = rawString.trim();
        if (result.isEmpty()) throw new BadInputException(fieldname + " should not be blank");
        return result;
    }
}
