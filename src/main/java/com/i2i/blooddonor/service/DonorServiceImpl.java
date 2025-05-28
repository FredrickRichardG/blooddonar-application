package com.i2i.blooddonor.service;

import com.i2i.blooddonor.model.Member;
import com.i2i.blooddonor.model.MemberDetail;
import com.i2i.blooddonor.repository.DonorEntityManagerRepository;
import com.i2i.blooddonor.repository.DonorMemberDetailRepository;
import com.i2i.blooddonor.repository.DonorRepository;
import com.i2i.blooddonor.requestModel.MemberDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Transactional
@Service
public class DonorServiceImpl implements DonorService {

    private final DonorRepository donorRepository;
    private final DonorEntityManagerRepository donorEntityManagerRepository;
    private final DonorMemberDetailRepository donorMemberDetailRepository;


    public DonorServiceImpl(DonorRepository donorRepository, DonorEntityManagerRepository donorEntityManagerRepository,DonorMemberDetailRepository donorMemberDetailRepository) {
        this.donorRepository = donorRepository;
        this.donorEntityManagerRepository = donorEntityManagerRepository;
        this.donorMemberDetailRepository = donorMemberDetailRepository;
    }

    @CachePut(value="MEMBER_CACHE",key = "#member.id")
    @Override
    public Member createMember(Member member, MemberDetail memberDetail) {
        member.setMemberdetail(memberDetail);
        memberDetail.setMember(member);

      return donorRepository.save(member);
    }

    @Override
    public Member patchMember(Integer id, String bloodGroup){

        Optional<Member> memberData = donorRepository.findById(id);
        if (memberData.isPresent()){
         Member  existingData = memberData.get();
        existingData.setBloodGroup(bloodGroup);
        return donorRepository.save(existingData);
        }else{

            throw new EntityNotFoundException("Following Id is not present "+id);
        }
    }

    @Cacheable(value="MEMBER_CACHE",key = "#id")
    public Member findByIdMember(Integer id){

        Optional<Member> memberOpData = donorRepository.findById(id);
        if(memberOpData.isPresent()){
            return memberOpData.get();
        }else{
            throw new EntityNotFoundException("Following Id is not present "+id);
        }
    }

    public List<Member> findAllMember(){

        return donorRepository.findAll();
    }

    @CacheEvict(value="MEMBER_CACHE",key = "#id")
    public void deleteMemberId(Integer id){
         donorRepository.deleteById(id);
    }

    @CachePut(value="MEMBER_CACHE",key = "#member.id")
    @Override
    public Member updateMember(Member member){

        Optional<Member> memberData = donorRepository.findById(member.getId());
        if (memberData.isPresent()){
            Member  existingData = memberData.get();
            existingData.setBloodGroup(member.getBloodGroup());
            existingData.setLastDonatedOn(member.getLastDonatedOn());
            return donorRepository.save(existingData);
        }else{

            throw new EntityNotFoundException("Following Id is not present "+member.getId());
        }
    }

    public Page<Member> findAllMemberWithPaginator(int page, int size){
        Pageable pageable = PageRequest.of(page,size);
        return donorRepository.findAll(pageable);
    }

    public List<Member> findEligibleMember(){
        LocalDate criteriaDate =LocalDate.now().minusDays(180);
        return donorRepository.findEligibleUser(criteriaDate);
    }

    public Member findByMemberEntityId(Integer id){

        return donorEntityManagerRepository.findById(id);
    }

    public List<Member> findEligibleMemberRegToBloodGrp(String bloodGroup){
        LocalDate criteriaDate =LocalDate.now().minusDays(180);
        return donorEntityManagerRepository.findEligibleUserRegToBloodGrp(criteriaDate,bloodGroup);
    }

    public List<Member>findSpecificBloodGroup(String bloodGroup){
        return donorRepository.findSpecificBloodGroup(bloodGroup);
    }

    public List<MemberDetail> findAllMemberDetail(){

        return donorMemberDetailRepository.findAllMemberDetail();
    }
}
