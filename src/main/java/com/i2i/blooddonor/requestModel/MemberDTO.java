package com.i2i.blooddonor.requestModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberDTO {

    private String name;
    private String bloodGroup;
    private Date lastDonatedOn;
    private String contact;
    private String Address;

}
