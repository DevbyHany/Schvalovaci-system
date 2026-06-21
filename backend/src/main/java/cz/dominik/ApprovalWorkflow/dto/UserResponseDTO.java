package cz.dominik.ApprovalWorkflow.dto;

import cz.dominik.ApprovalWorkflow.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public class UserResponseDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Jan Novák")
    private String name;

    @Schema(example = "jan.novak@seznam.cz")
    private String email;

    @Schema(example = "APPROVER")
    private Role role;

    public UserResponseDTO(Long id, String name, String email, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
