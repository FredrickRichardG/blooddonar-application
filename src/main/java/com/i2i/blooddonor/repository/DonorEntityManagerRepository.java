package com.i2i.blooddonor.repository;


import com.i2i.blooddonor.model.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public class DonorEntityManagerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Member findById(Integer id){
        return entityManager.find(Member.class,id);
    }

    public List<Member> findEligibleUserRegToBloodGrp(LocalDate criteriaDate,String bloodGroup){
        Query query= entityManager.createQuery("select m from Member m where m.lastDonatedOn < :criteriaDate and m.bloodGroup = :bloodGrp");
        query.setParameter("criteriaDate",criteriaDate);
        query.setParameter("bloodGrp",bloodGroup);

        return query.getResultList();
    }

}
