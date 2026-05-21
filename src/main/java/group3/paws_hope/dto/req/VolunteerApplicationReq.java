package group3.paws_hope.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VolunteerApplicationReq {
    private Long userId;

    @NotBlank(message = "Full name cannot be left blank.")
    private String full_name;

    @Email(message = "Email is not in the correct format.")
    @NotBlank(message = "Email cannot be left blank.")
    private String email;

    @NotBlank(message = "Phone cannot be left blank.")
    private String phone;

    private LocalDate dateOfBirth;
    private String address;
    private String occupation;
    private String skills;
    private String experienceWithAnimals;
    private String reasonToJoin;
    private String availableDays;
    private String preferredTasks;
    private Boolean hasTransport;
}