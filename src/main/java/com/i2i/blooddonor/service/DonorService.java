package com.i2i.blooddonor.service;

import com.i2i.blooddonor.model.Member;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DonorService {

    Member createMember(Member member);
    Member patchMember(Integer id, String bloodGroup);
    Member findByIdMember(Integer id);
    List<Member> findAllMember();
    void deleteMemberId(Integer id);
    Member updateMember(Member member);
    Page<Member> findAllMemberWithPaginator(int page, int size);
    List<Member> findEligibleMember();
    Member findByMemberEntityId(Integer id);
    List<Member>findEligibleMemberRegToBloodGrp(String bloodGroup);
    List<Member>findSpecificBloodGroup(String bloodGroup);
}
