package group3.paws_hope.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "organization_info")
@Getter
@Setter
public class OrganizationInfo {

    @Id
    @Column(name = "id")
    private Integer id = 1;

    @Column(name = "org_name", length = 255)
    private String orgName;

    @Column(name = "logo_url", length = 255)
    private String logoUrl;

    @Column(length = 20)
    private String hotline;

    @Column(length = 100)
    private String email;

    @Column(name = "facebook_link", length = 255)
    private String facebookLink;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "mission_statement", columnDefinition = "TEXT")
    private String missionStatement;
}