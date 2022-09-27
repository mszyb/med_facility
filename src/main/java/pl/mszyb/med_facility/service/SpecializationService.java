package pl.mszyb.med_facility.service;

import org.springframework.stereotype.Service;
import pl.mszyb.med_facility.entity.Specialization;
import pl.mszyb.med_facility.repository.SpecializationRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SpecializationService {

    private final SpecializationRepository specializationRepository;

    public SpecializationService(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    public List<Specialization> findAllActive() {
        return specializationRepository.findAllActive();
    }

    public List<Specialization> findAll() {
        return specializationRepository.findAll();
    }

    public void deactivate(long id) {
        Specialization specialization = specializationRepository.findById(id).orElseThrow(NoSuchElementException::new);
        specialization.setActive(false);
        specializationRepository.save(specialization);
    }

    public void activate(long id) {
        Specialization specialization = specializationRepository.findById(id).orElseThrow(NoSuchElementException::new);
        specialization.setActive(true);
        specializationRepository.save(specialization);
    }

    public void save(Specialization specialization) {
        specializationRepository.save(specialization);
    }

    public Specialization findByName(String name) {
        return specializationRepository.findByName(name);
    }

    public Optional<Specialization> findById(Long id) {
        return specializationRepository.findById(id);
    }
}
