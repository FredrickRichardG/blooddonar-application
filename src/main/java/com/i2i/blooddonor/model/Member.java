package com.i2i.blooddonor.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.sql.Date;


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

    @OneToOne(mappedBy="member",cascade = CascadeType.ALL)
    @JsonManagedReference
    @Schema(hidden = true)
    private MemberDetail memberdetail;


}
