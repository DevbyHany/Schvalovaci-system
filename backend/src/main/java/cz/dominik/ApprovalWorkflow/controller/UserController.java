package cz.dominik.ApprovalWorkflow.controller;

import cz.dominik.ApprovalWorkflow.dto.RegisterDTO;
import cz.dominik.ApprovalWorkflow.dto.UserResponseDTO;
import cz.dominik.ApprovalWorkflow.entity.User;
import cz.dominik.ApprovalWorkflow.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Uživatelé", description = "Registrace a informace o přihlášeném uživateli")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @Operation(
            summary = "Informace o přihlášeném uživateli",
            description = "Vrátí údaje o aktuálně přihlášeném uživateli."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Úspěšně vráceno"),
            @ApiResponse(responseCode = "401", description = "Nepřihlášený uživatel")
    })
    public UserResponseDTO getCurrentUser(@AuthenticationPrincipal User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    @PostMapping("/register")
    @Operation(
            summary = "Registrace nového uživatele",
            description = "Vytvoří nový uživatelský účet. Přístupné bez přihlášení."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Uživatel úspěšně registrován"),
            @ApiResponse(responseCode = "400", description = "Neplatná vstupní data nebo email už existuje")
    })
    public UserResponseDTO register(@Valid @RequestBody RegisterDTO dto) {
        return userService.register(dto);
    }
}