package pl.mszyb.med_facility.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mszyb.med_facility.entity.Specialization;
import pl.mszyb.med_facility.repository.SpecializationRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SpecializationServiceTest {
    @Mock
    SpecializationRepository repository;
    @InjectMocks
    SpecializationService specializationService;
    private Specialization specialization;

    @BeforeEach
    void setup() {
        specialization = new Specialization();
        specialization.setId(1L);
    }

    @Test
    void should_activate_specialization() {
        given(repository.findById(anyLong())).willReturn(Optional.of(specialization));
        specialization.setActive(false);
        assertFalse(specialization.isActive());
        specializationService.activate(specialization.getId());
        assertTrue(specialization.isActive());
    }

    @Test
    void should_deactivate_specialization() {
        given(repository.findById(anyLong())).willReturn(Optional.of(specialization));
        specialization.setActive(true);
        assertTrue(specialization.isActive());
        specializationService.deactivate(specialization.getId());
        assertFalse(specialization.isActive());
    }

    @Test
    void should_throw_no_such_element_exception() {
        given(repository.findById(anyLong())).willReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> specializationService.activate(specialization.getId()));
        assertThrows(NoSuchElementException.class, () -> specializationService.deactivate(specialization.getId()));
    }
}