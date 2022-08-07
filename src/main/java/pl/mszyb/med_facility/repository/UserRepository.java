package pl.mszyb.med_facility.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mszyb.med_facility.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(long id);
    Optional<User> findByEmail(String email);

}
