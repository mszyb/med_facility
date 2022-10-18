package pl.mszyb.med_facility.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mszyb.med_facility.entity.ServiceType;
import pl.mszyb.med_facility.repository.ServiceTypeRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ServiceTypeServiceTest {

    @Mock
    ServiceTypeRepository repository;
    @InjectMocks
    ServiceTypeService serviceTypeService;
    private ServiceType service;

    @BeforeEach
    void setup() {
        service = new ServiceType();
        service.setId(1L);
    }

    @Test
    void should_activate_service() {
        given(repository.findById(anyLong())).willReturn(Optional.of(service));
        service.setActive(false);
        assertFalse(service.isActive());
        serviceTypeService.activate(service.getId());
        assertTrue(service.isActive());
    }

    @Test
    void should_deactivate_service() {
        given(repository.findById(anyLong())).willReturn(Optional.of(service));
        service.setActive(true);
        assertTrue(service.isActive());
        serviceTypeService.deactivate(service.getId());
        assertFalse(service.isActive());
    }

    @Test
    void should_throw_no_such_element_exception() {
        given(repository.findById(anyLong())).willReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> serviceTypeService.activate(service.getId()));
        assertThrows(NoSuchElementException.class, () -> serviceTypeService.deactivate(service.getId()));
    }
}