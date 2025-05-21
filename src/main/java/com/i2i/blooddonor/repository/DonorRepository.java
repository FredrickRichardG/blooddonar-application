package com.i2i.blooddonor.repository;

import com.i2i.blooddonor.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

public interface DonorRepository extends JpaRepository<Member,Integer> , PagingAndSortingRepository<Member,Integer> {
    @Query("select m from Member m where m.lastDonatedOn < :criteriaDate")
    List<Member> findEligibleUser(@Param("criteriaDate") LocalDate criteriaDate);
}
