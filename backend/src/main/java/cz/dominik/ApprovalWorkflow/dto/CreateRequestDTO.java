package cz.dominik.ApprovalWorkflow.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class CreateRequestDTO {

    @NotBlank(message = "Titulek nesmí být prázdný")
    @Length(max = 100, message = "Titulek nesmí obsahovat více než 100 znaků")
    private String title;

    @NotBlank(message = "Popis nesmí být prázdný")
    @Length(max = 500, message = "Popis nemůže obsahovat více než 500 znaků")
    private String description;


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
}
