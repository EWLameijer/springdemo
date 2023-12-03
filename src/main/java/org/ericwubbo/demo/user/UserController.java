package org.ericwubbo.demo.user;

import lombok.RequiredArgsConstructor;
import org.ericwubbo.demo.BadInputException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;


@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("register")
    public ResponseEntity<UserRegistrationResultDto> register(
            @RequestBody UserRegistrationDto userRegistrationDto, UriComponentsBuilder ucb) {
        var username = getValidValueOrThrow(userRegistrationDto.username(), "username");
        var password = getValidValueOrThrow(userRegistrationDto.password(), "password");
        var possibleUser = userService.findByUsername(username);
        if (possibleUser.isPresent()) throw new BadInputException("username already exists");

        var newUser = userService.save(username, password, MARole.USER);
        URI locationOfNewUser = ucb
                .path("users/{username}")
                .buildAndExpand(newUser.getUsername())
                .toUri();
        return ResponseEntity.created(locationOfNewUser).body(new UserRegistrationResultDto(newUser.getUsername()));
    }

    @GetMapping("{username}")
    public ResponseEntity<UserRegistrationResultDto> getUser(@PathVariable String username, Principal principal) {
        if (!principal.getName().equals(username)) return ResponseEntity.notFound().build();
        var possiblyFoundUser = userService.findByUsername(username);
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
