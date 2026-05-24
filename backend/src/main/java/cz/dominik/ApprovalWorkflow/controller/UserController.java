package cz.dominik.ApprovalWorkflow.controller;

import cz.dominik.ApprovalWorkflow.dto.RegisterDTO;
import cz.dominik.ApprovalWorkflow.dto.UserResponseDTO;
import cz.dominik.ApprovalWorkflow.entity.User;
import cz.dominik.ApprovalWorkflow.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserResponseDTO getCurrentUser(@AuthenticationPrincipal User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    @PostMapping("/register")
    public UserResponseDTO register(@Valid @RequestBody RegisterDTO dto) {
        return userService.register(dto);
    }
}