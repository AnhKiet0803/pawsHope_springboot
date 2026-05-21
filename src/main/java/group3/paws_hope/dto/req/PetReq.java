package group3.paws_hope.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PetReq {

    private String petCode;

    @NotBlank(message = "Pet name cannot be left blank.")
    private String name;

    private String gender;

    @NotBlank(message = "Species cannot be left blank.")
    private String species;

    private String breed;
    private Integer ageMonths;
    private BigDecimal weightKg;
    private String healthStatus;
    private String personality;
    private String status;
    private String imageUrl;
    private Long kennelId;
    private Long fromReportId;
    private LocalDate intakeDate;
    private String description;
}