package group3.paws_hope.repository;

import group3.paws_hope.entity.OrganizationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationInfoRepository extends JpaRepository<OrganizationInfo,Integer> {
}
