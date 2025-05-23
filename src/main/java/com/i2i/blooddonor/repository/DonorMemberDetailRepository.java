package com.i2i.blooddonor.repository;

import com.i2i.blooddonor.model.MemberDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface DonorMemberDetailRepository extends JpaRepository<MemberDetail,Integer> {

    @Query(name="md.getCommunicationDetail")
    List<MemberDetail> findAllMemberDetail();
}
