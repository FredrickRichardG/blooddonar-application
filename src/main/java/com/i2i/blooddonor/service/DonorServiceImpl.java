package com.i2i.blooddonor.service;

import com.i2i.blooddonor.model.Member;
import com.i2i.blooddonor.repository.DonorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DonorServiceImpl implements DonorService {

    private final DonorRepository donorRepository;

    public DonorServiceImpl(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    @Override
    public Member createMember(Member member) {
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
    public void deleteMemberId(Integer id){
         donorRepository.deleteById(id);
    }

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
        System.out.println("criteria_date-----"+criteriaDate);
        return donorRepository.findEligibleUser(criteriaDate);
    }
}
