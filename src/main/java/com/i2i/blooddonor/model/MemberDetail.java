package com.i2i.blooddonor.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
@Table(name="member_detail")
@NamedQuery(name = "md.getCommunicationDetail",query = "select md from MemberDetail md")
public class MemberDetail extends Auditable<String>
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name="contact")
    String contact;
    @Column(name="address")
    String address;

    @OneToOne
    @JoinColumn(name="member_id", referencedColumnName = "id")
    @JsonBackReference
    Member member;
}
