package group3.paws_hope.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "item_donations")
@Getter
@Setter
public class ItemDonation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_donation_id")
    private Long itemDonationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "donor_name_manual", length = 100)
    private String donorNameManual;

    @Column(name = "item_name", nullable = false, length = 150)
    private String itemName;

    @ManyToOne
    @JoinColumn(name = "received_by")
    private User receivedBy;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(length = 50)
    private String quantity;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(name = "received_at", insertable = false, updatable = false)
    private Timestamp receivedAt;

    public enum Category {
        FOOD, MEDICAL_SUPPLY, CLEANING, EQUIPMENT, OTHER
    }

    public enum Status {
        PENDING, RECEIVED, USED
    }
}