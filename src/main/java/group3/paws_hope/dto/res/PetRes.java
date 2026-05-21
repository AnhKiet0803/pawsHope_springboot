package group3.paws_hope.dto.res;

import group3.paws_hope.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class PetRes {

    private Long petId;
    private String petCode;
    private String name;
    private String gender;
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

    public static PetRes toJson(Pet pet) {
        return new PetRes(
                pet.getPetId(),
                pet.getPetCode(),
                pet.getName(),
                pet.getGender().name(),
                pet.getSpecies().name(),
                pet.getBreed(),
                pet.getAgeMonths(),
                pet.getWeightKg(),
                pet.getHealthStatus().name(),
                pet.getPersonality(),
                pet.getStatus().name(),
                pet.getImageUrl(),
                pet.getKennel() != null ? pet.getKennel().getKennelId() : null,
                pet.getFromReport() != null ? pet.getFromReport().getReportId() : null,
                pet.getIntakeDate(),
                pet.getDescription()
        );
    }
}