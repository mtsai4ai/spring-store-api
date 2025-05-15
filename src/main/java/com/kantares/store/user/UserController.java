package com.kantares.store.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@AllArgsConstructor
@RestController
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @GetMapping("users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            UserDto userDto = userMapper.toDto(user);
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("users")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Email already exists"));
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);

        UserDto userDto = userMapper.toDto(savedUser);
        URI uri = uriBuilder.path("/users/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("users/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest updateUserRequest) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        userMapper.update(updateUserRequest, user);
        User updatedUser = userRepository.save(user);
        UserDto updatedUserDto = userMapper.toDto(updatedUser);
        return ResponseEntity.ok(updatedUserDto);
    }

    @DeleteMapping("users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("users/{id}/update-password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody UpdatePasswordRequest request) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (!user.getPassword().equals(request.getOldPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String newPassword = request.getNewPassword();
        user.setPassword(newPassword);
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }
}
