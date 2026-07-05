package cz.dominik.ApprovalWorkflow.dto;

import cz.dominik.ApprovalWorkflow.entity.RequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class ApprovalRequestResponseDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Žádost o nový notebook")
    private String title;

    @Schema(example = "Potřebuji nový notebook pro práci na novém projektu")
    private String description;

    @Schema(example = "2026-06-21T10:00:00")
    private LocalDateTime createdAt;

    @Schema(example = "2026-06-21T10:00:00")
    private LocalDateTime updatedAt;

    @Schema(example = "PENDING")
    private RequestStatus requestStatus;

    @Schema(example = "Žádost byla zrušena z důvodu nedostatku dovolené")
    private String cancellationReason;

    private UserResponseDTO creator;

    private UserResponseDTO approver;

    private UserResponseDTO canceler;

    public ApprovalRequestResponseDTO(UserResponseDTO approver, UserResponseDTO creator, RequestStatus requestStatus, LocalDateTime updatedAt, LocalDateTime createdAt, String description, String title, Long id, String cancellationReason, UserResponseDTO canceler) {
        this.approver = approver;
        this.creator = creator;
        this.requestStatus = requestStatus;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.description = description;
        this.title = title;
        this.id = id;
        this.canceler = canceler;
        this.cancellationReason = cancellationReason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public UserResponseDTO getCreator() {
        return creator;
    }

    public void setCreator(UserResponseDTO creator) {
        this.creator = creator;
    }

    public UserResponseDTO getApprover() {
        return approver;
    }

    public void setApprover(UserResponseDTO approver) {
        this.approver = approver;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public UserResponseDTO getCanceler() {
        return canceler;
    }

    public void setCanceler(UserResponseDTO canceler) {
        this.canceler = canceler;
    }
}
