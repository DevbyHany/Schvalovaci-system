package cz.dominik.ApprovalWorkflow.controller;

import cz.dominik.ApprovalWorkflow.dto.ApprovalRequestResponseDTO;
import cz.dominik.ApprovalWorkflow.dto.CreateRequestDTO;
import cz.dominik.ApprovalWorkflow.entity.User;
import cz.dominik.ApprovalWorkflow.service.ApprovalService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller pro správu schvalovacích žádostí.
 * Všechny endpointy vyžadují autentizaci přes Basic Auth.
 */
@RestController
@RequestMapping("/api/requests")
public class ApprovalController {

    private ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    /** Vytvoří novou žádost pro přihlášeného uživatele. POST /api/requests */
    @PostMapping
    public ApprovalRequestResponseDTO createRequest(
            @Valid @RequestBody CreateRequestDTO dto,
            @AuthenticationPrincipal User user) {
        return approvalService.createRequest(dto.getTitle(), dto.getDescription(), user);
    }

    /** Schválí žádost podle ID. Přístup pouze pro APPROVER a ADMIN. PUT /api/requests/{id}/approve */
    @PutMapping("/{id}/approve")
    public ApprovalRequestResponseDTO approveRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return approvalService.approveRequest(user, id);
    }

    /** Zamítne žádost podle ID. Přístup pouze pro APPROVER a ADMIN. PUT /api/requests/{id}/reject */
    @PutMapping("/{id}/reject")
    public ApprovalRequestResponseDTO rejectRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return approvalService.rejectRequest(user, id);
    }

    /** Vrátí detail žádosti podle ID. GET /api/requests/{id} */
    @GetMapping("/{id}")
    public ApprovalRequestResponseDTO detailRequest(@PathVariable Long id) {
        return approvalService.getRequestById(id);
    }

    /** Vrátí seznam žádostí podle role přihlášeného uživatele. GET /api/requests */
    @GetMapping
    public List<ApprovalRequestResponseDTO> allRequests(@AuthenticationPrincipal User user) {
        return approvalService.getAllRequests(user);
    }
}
