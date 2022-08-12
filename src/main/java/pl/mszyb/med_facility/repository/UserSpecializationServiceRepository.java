package pl.mszyb.med_facility.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.mszyb.med_facility.entity.Specialization;
import pl.mszyb.med_facility.entity.UserSpecializationService;

import java.math.BigInteger;
import java.util.List;

public interface UserSpecializationServiceRepository extends JpaRepository<UserSpecializationService, Long> {

    @Query("select distinct u.specialization.id from UserSpecializationService u where u.user.id = ?1")
    List<Long> findSpecializationForUserId(Long id);


}

