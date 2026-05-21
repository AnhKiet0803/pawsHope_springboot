package group3.paws_hope.repository;

import group3.paws_hope.entity.Kennel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KennelRepository extends JpaRepository<Kennel,Long> {
}
