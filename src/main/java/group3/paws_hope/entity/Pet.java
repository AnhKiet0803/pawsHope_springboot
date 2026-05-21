package group3.paws_hope.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pets")
@Getter
@Setter
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long petId;

    @Column(name = "pet_code", length = 20, unique = true)
    private String petCode;

    @Column(length = 100, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.UNKNOWN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Species species;

    @Column(length = 100)
    private String breed;

    @Column(name = "age_months")
    private Integer ageMonths;

    @Column(name = "weight_kg")
    private BigDecimal weightKg;

    @Enumerated(EnumType.STRING)
    @Column(name = "health_status")
    private HealthStatus healthStatus = HealthStatus.HEALTHY;

    private String personality;

    @Enumerated(EnumType.STRING)
    private Status status = Status.NOT_READY_FOR_ADOPTION;

    @Column(name = "image_url",length = 255)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "kennel_id")
    private Kennel kennel;

    @ManyToOne
    @JoinColumn(name = "from_report_id")
    private RescueReport fromReport;

    @Column(name = "intake_date")
    private LocalDate intakeDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    public enum Gender {
        MALE, FEMALE, UNKNOWN
    }

    public enum Species {
        DOG, CAT, OTHER
    }

    public enum HealthStatus {
        HEALTHY, VACCINATED, STERILIZED, UNDER_TREATMENT, SPECIAL_NEEDS
    }

    public enum Status {
        NOT_READY_FOR_ADOPTION, AVAILABLE_FOR_ADOPTION, PENDING_ADOPTION, ADOPTED, DECEASED
    }
}