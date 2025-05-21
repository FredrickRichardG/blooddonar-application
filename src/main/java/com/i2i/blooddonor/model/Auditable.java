package com.i2i.blooddonor.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<T> {

    @CreatedDate
    @Column(nullable = false,updatable = false)
    private Timestamp created;

    @CreatedBy
    @Column(name="created_by",nullable = false,updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(insertable = false)
    private Timestamp updated;

    @LastModifiedBy
    @Column(name="updated_by",insertable = false)
    private String updatedBy;
}
