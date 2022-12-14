package pl.mszyb.med_facility.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private User physician;
    @OneToOne
    private User patient;
    @Column(unique = true)
    private ZonedDateTime startTime;
    @Column(unique = true)
    private ZonedDateTime endTime;
    @ManyToOne
    private Specialization selectedSpec;
    @ManyToOne
    private ServiceType selectedService;

    private boolean isDone = false;
}
