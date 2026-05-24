package cz.dominik.ApprovalWorkflow.controller;

import cz.dominik.ApprovalWorkflow.dto.ApprovalRequestResponseDTO;
import cz.dominik.ApprovalWorkflow.dto.CreateRequestDTO;
import cz.dominik.ApprovalWorkflow.entity.User;
import cz.dominik.ApprovalWorkflow.service.ApprovalService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class ApprovalController {

    private ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @PostMapping
    public ApprovalRequestResponseDTO createRequest(
            @Valid @RequestBody CreateRequestDTO dto,
            @AuthenticationPrincipal User user) {
        return approvalService.createRequest(dto.getTitle(), dto.getDescription(), user);
    }

    @PutMapping("/{id}/approve")
    public ApprovalRequestResponseDTO approveRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return approvalService.approveRequest(user, id);
    }

    @PutMapping("/{id}/reject")
    public ApprovalRequestResponseDTO rejectRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return approvalService.rejectRequest(user, id);
    }

    @GetMapping("/{id}")
    public ApprovalRequestResponseDTO detailRequest(@PathVariable Long id) {
        return approvalService.getRequestById(id);
    }

    @GetMapping
    public List<ApprovalRequestResponseDTO> allRequests(@AuthenticationPrincipal User user) {
        return approvalService.getAllRequests(user);
    }
}
