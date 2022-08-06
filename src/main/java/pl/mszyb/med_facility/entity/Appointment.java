package pl.mszyb.med_facility.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long physicianId;
    private Long patientId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
