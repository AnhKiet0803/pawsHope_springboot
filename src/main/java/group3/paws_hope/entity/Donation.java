package group3.paws_hope.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "donations")
@Getter
@Setter
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "donation_id")
    private Long donationId;

    @ManyToOne
    @JoinColumn(name = "campaign_id", nullable = false)
    private DonationCampaign campaign;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "donor_name_manual", length = 100)
    private String donorNameManual = "GUEST";

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod = PaymentMethod.PAYPAL;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "source_order_id")
    private Order sourceOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "donation_type", nullable = false)
    private DonationType donationType;

    @Column(name = "received_at", insertable = false, updatable = false)
    private Timestamp receivedAt;

    public enum PaymentMethod {
        PAYPAL
    }

    public enum PaymentStatus {
        PENDING, PAID, FAILED, REFUNDED
    }

    public enum DonationType {
        DONATE, PRODUCT_SALE
    }
}