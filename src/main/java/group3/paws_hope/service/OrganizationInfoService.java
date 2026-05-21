package group3.paws_hope.service;

import group3.paws_hope.dto.req.OranizationInfoReq;
import group3.paws_hope.dto.res.OranizationInfoRes;
import group3.paws_hope.entity.OrganizationInfo;
import group3.paws_hope.repository.OrganizationInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrganizationInfoService {

    private final OrganizationInfoRepository organizationInfoRepository;

    public OranizationInfoRes getInfo() {
        OrganizationInfo org = organizationInfoRepository.findById(1)
                .orElse(null);

        if (org == null) return null;

        return OranizationInfoRes.toJson(org);
    }

    public OranizationInfoRes saveOrUpdate(OranizationInfoReq req) {
        try {

            OrganizationInfo org = organizationInfoRepository.findById(1)
                    .orElse(new OrganizationInfo());

            org.setId(1);
            org.setOrgName(req.getOrgName());
            org.setLogoUrl(req.getLogoUrl());
            org.setHotline(req.getHotline());
            org.setEmail(req.getEmail());
            org.setFacebookLink(req.getFacebookLink());
            org.setAddress(req.getAddress());
            org.setMissionStatement(req.getMissionStatement());

            return OranizationInfoRes.toJson(organizationInfoRepository.save(org));

        } catch (Exception e) {
            return null;
        }
    }
}