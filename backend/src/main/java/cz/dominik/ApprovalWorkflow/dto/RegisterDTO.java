package cz.dominik.ApprovalWorkflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class RegisterDTO {

    @NotBlank(message = "Jméno nesmí být prázdné")
    @Schema(example = "Jan Novák")
    private String name;

    @NotBlank(message = "Email nesmí být prázdný")
    @Email(message = "Zadej platný email")
    @Schema(example = "jan.novak@seznam.cz")
    private String email;

    @NotBlank(message = "Heslo nesmí být prázdné")
    @Length(min = 6, message = "Heslo musí mít alespoň 6 znaků")
    @Length(max = 20, message = "Heslo nesmí obsahovat více než 20 znaků")
    @Schema(example = "heslo123")
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
