package online.store.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import online.store.book.dto.user.UserRegistrationRequestDto;
import online.store.book.dto.user.UserResponseDto;
import online.store.book.exceptions.RegistrationException;
import online.store.book.service.user.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name = "User management", description = "Endpoints for user management")
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;

    @Operation(summary = "Save new user")
    @PostMapping("/registration")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto requestUser)
            throws RegistrationException {
        return userService.register(requestUser);
    }
}
