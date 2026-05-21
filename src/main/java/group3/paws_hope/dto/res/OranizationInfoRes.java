package group3.paws_hope.dto.res;

import group3.paws_hope.entity.OrganizationInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class OranizationInfoRes {
    private Integer id;
    private String orgName;
    private String logoUrl;
    private String hotline;
    private String email;
    private String facebookLink;
    private String address;
    private String missionStatement;

    public static OranizationInfoRes toJson(OrganizationInfo organizationInfo) {
        return new OranizationInfoRes(
                organizationInfo.getId(),
                organizationInfo.getOrgName(),
                organizationInfo.getLogoUrl(),
                organizationInfo.getHotline(),
                organizationInfo.getEmail(),
                organizationInfo.getFacebookLink(),
                organizationInfo.getAddress(),
                organizationInfo.getMissionStatement()
        );
    }
}
