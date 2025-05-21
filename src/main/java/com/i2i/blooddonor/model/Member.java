package com.i2i.blooddonor.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "member")
public class Member extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @Column(name = "bloodgroup")
    private String bloodGroup;

    @Column(name="last_donated_on")
    private Date lastDonatedOn;

}
