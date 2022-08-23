package pl.mszyb.med_facility.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;
    private String confirmationToken;
    @CreationTimestamp
    private Timestamp created;
    @OneToOne
    private User user;

    public ConfirmationToken() {
        confirmationToken = UUID.randomUUID().toString();
    }
}
