package cz.dominik.ApprovalWorkflow.controller;

import cz.dominik.ApprovalWorkflow.dto.ApprovalRequestResponseDTO;
import cz.dominik.ApprovalWorkflow.dto.CreateRequestDTO;
import cz.dominik.ApprovalWorkflow.entity.User;
import cz.dominik.ApprovalWorkflow.service.ApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller pro správu schvalovacích žádostí.
 * Všechny endpointy vyžadují autentizaci přes Basic Auth.
 */
@Tag(name = "Schvalovací požadavky", description = "Vytváření, schvalování a zamítání požadavků")
@RestController
@RequestMapping("/api/requests")
public class ApprovalController {

    private ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    /** Vytvoří novou žádost pro přihlášeného uživatele. POST /api/requests */
    @Operation(
            summary = "Vytvoření nového požadavku",
            description = "Vytvoří nový schvalovací požadavek. Dostupné pro role USER a APPROVER."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Požadavek úspěšně vytvořen"),
            @ApiResponse(responseCode = "400", description = "Neplatná vstupní data"),
            @ApiResponse(responseCode = "401", description = "Nepřihlášený uživatel")
    })
    @PostMapping
    public ApprovalRequestResponseDTO createRequest(
            @Valid @RequestBody CreateRequestDTO dto,
            @AuthenticationPrincipal User user) {
        return approvalService.createRequest(dto.getTitle(), dto.getDescription(), user);
    }

    /** Schválí žádost podle ID. Přístup pouze pro APPROVER a ADMIN. PUT /api/requests/{id}/approve */
    @Operation(
            summary = "Schválení požadavku",
            description = "Schválí konkrétní požadavek. Dostupné pouze pro role APPROVER a ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Požadavek úspěšně schválen"),
            @ApiResponse(responseCode = "401", description = "Nepřihlášený uživatel"),
            @ApiResponse(responseCode = "403", description = "Nedostatečná oprávnění"),
            @ApiResponse(responseCode = "404", description = "Požadavek nenalezen")
    })
    @PutMapping("/{id}/approve")
    public ApprovalRequestResponseDTO approveRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return approvalService.approveRequest(user, id);
    }

    /** Zamítne žádost podle ID. Přístup pouze pro APPROVER a ADMIN. PUT /api/requests/{id}/reject */
    @Operation(
            summary = "Zamítnutí požadavku",
            description = "Zamítne konkrétní požadavek. Dostupné pouze pro role APPROVER a ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Požadavek úspěšně zamítnut"),
            @ApiResponse(responseCode = "401", description = "Nepřihlášený uživatel"),
            @ApiResponse(responseCode = "403", description = "Nedostatečná oprávnění"),
            @ApiResponse(responseCode = "404", description = "Požadavek nenalezen")
    })
    @PutMapping("/{id}/reject")
    public ApprovalRequestResponseDTO rejectRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return approvalService.rejectRequest(user, id);
    }

    /** Vrátí detail žádosti podle ID. GET /api/requests/{id} */
    @Operation(
            summary = "Detail požadavku",
            description = "Vrátí detail konkrétního požadavku podle ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Úspěšně vráceno"),
            @ApiResponse(responseCode = "401", description = "Nepřihlášený uživatel"),
            @ApiResponse(responseCode = "404", description = "Požadavek nenalezen")
    })
    @GetMapping("/{id}")
    public ApprovalRequestResponseDTO detailRequest(@PathVariable Long id) {
        return approvalService.getRequestById(id);
    }

    /** Vrátí seznam žádostí podle role přihlášeného uživatele. GET /api/requests */
    @Operation(
            summary = "Seznam všech požadavků",
            description = "Vrátí všechny schvalovací požadavky. USER vidí jen své vlastní, APPROVER/ADMIN vidí všechny."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Úspěšně vráceno"),
            @ApiResponse(responseCode = "401", description = "Nepřihlášený uživatel")
    })
    @GetMapping
    public List<ApprovalRequestResponseDTO> allRequests(@AuthenticationPrincipal User user) {
        return approvalService.getAllRequests(user);
    }
}
