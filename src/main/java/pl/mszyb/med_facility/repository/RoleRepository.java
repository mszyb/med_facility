package pl.mszyb.med_facility.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mszyb.med_facility.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(String roleName);
}
