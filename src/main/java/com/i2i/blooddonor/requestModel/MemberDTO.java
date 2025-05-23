package com.i2i.blooddonor.requestModel;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class MemberDTO {

    private String name;
    private String bloodGroup;
    private Date lastDonatedOn;
    private String contact;
    private String Address;
}
