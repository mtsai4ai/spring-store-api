package com.kantares.store.auth;

import com.kantares.store.user.User;
import com.kantares.store.user.UserDto;
import com.kantares.store.user.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final JwtConfig jwtConfig;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
        Authentication auth = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        authenticationManager.authenticate(auth);

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        Jwt accessToken = jwtService.generateAccessToken(user);
        Jwt refreshToken = jwtService.generateRefreshToken(user);

        Cookie cookie = new Cookie("refreshToken", refreshToken.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration()); // 30 days
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "Strict");
        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserDto userDto = new UserDto(user.getId(), user.getEmail(), user.getName());
        return ResponseEntity.ok(userDto);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Invalid email or password!"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> validate(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        System.out.println("Token refresh ......");

        Jwt jwt = jwtService.parseJwt(refreshToken);
        if (!jwt.isValid()) {;
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = jwt.getUserId();
        User user = userRepository.findById(userId).orElseThrow();

        Jwt newAccessToken = jwtService.generateAccessToken(user);
        return ResponseEntity.ok(new JwtResponse(newAccessToken.toString()));
    }
}
