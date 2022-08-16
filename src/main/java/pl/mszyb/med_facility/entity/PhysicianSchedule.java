package pl.mszyb.med_facility.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
public class PhysicianSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User physician;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private ZonedDateTime startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private ZonedDateTime endTime;
}
