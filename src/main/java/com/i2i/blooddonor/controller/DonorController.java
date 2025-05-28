package com.i2i.blooddonor.controller;

import com.i2i.blooddonor.model.Member;
import com.i2i.blooddonor.model.MemberDetail;
import com.i2i.blooddonor.requestModel.MemberDTO;
import com.i2i.blooddonor.service.DonorService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RefreshScope
@RequestMapping("/blooddonors")
public class DonorController {

    private final DonorService donorService;

    public DonorController(DonorService donorService){
        this.donorService=donorService;
    }

    @Value("${greetings.message}")
    private String greeting;


    @GetMapping("/welcome")
    public ResponseEntity<String> welcomeMessage(){
        return new ResponseEntity<>(greeting,HttpStatus.OK);
    }

    @PostMapping("/newMember")
    public ResponseEntity<Member> newMember(@RequestBody MemberDTO memberDTO){
        Member member = new Member();
        member.setName(memberDTO.getName());
        member.setBloodGroup(memberDTO.getBloodGroup());
        member.setLastDonatedOn(memberDTO.getLastDonatedOn());

        MemberDetail memberDetail = new MemberDetail();
        memberDetail.setContact(memberDTO.getContact());
        memberDetail.setAddress(memberDTO.getAddress());

        Member newData = donorService.createMember(member,memberDetail);
        return new ResponseEntity<>(newData, HttpStatus.CREATED);
    }

    @PatchMapping("/patchMember")
    public ResponseEntity<Member> patchMember(@RequestBody Map<String,Object> request){
        int id = (int) request.get("id");
        String bloodGroup = (String) request.get("bloodGroup");

        Member updatedData= donorService.patchMember(id,bloodGroup);
        return  new ResponseEntity<>(updatedData,HttpStatus.CREATED);
    }

    @GetMapping("/findMemberById")
    public ResponseEntity<Member> findById( @RequestParam Integer id){
        Member memberData = donorService.findByIdMember(id);
        return new ResponseEntity<>(memberData,HttpStatus.OK);
    }

    @GetMapping("/findAll")
    public  ResponseEntity<List<Member>> findAllMember(){
      return new ResponseEntity<>(donorService.findAllMember(),HttpStatus.OK);
    }


    @DeleteMapping("/deleteMemberById")
    public ResponseEntity<String> deleteMemberById(@RequestParam Integer id){
        donorService.deleteMemberId(id);
        return new ResponseEntity<>("Member "+id+" Removed",HttpStatus.OK);
    }

    @PutMapping("/updateMember")
    public ResponseEntity<Member> updateMember(@RequestBody Member member){
        Member updatedData= donorService.updateMember(member);
        return  new ResponseEntity<>(updatedData,HttpStatus.CREATED);
    }

    @GetMapping("/findAllWithPaginator")
    public  ResponseEntity<Page<Member>> findAllWithPaginator(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return new ResponseEntity<>(donorService.findAllMemberWithPaginator(page,size),HttpStatus.OK);
    }

    @GetMapping("/eligibleMember")
    public  ResponseEntity<List<Member>> findEligibleMember(){
        return new ResponseEntity<>(donorService.findEligibleMember(),HttpStatus.OK);
    }

    @GetMapping("/findMemberByIdEntity")
    public ResponseEntity<Member> findMemberByIdEntity( @RequestParam Integer id){
        Member memberData = donorService.findByMemberEntityId(id);
        return new ResponseEntity<>(memberData,HttpStatus.OK);
    }

    @GetMapping("/eligibleMemberRegToBloodGroup")
    public  ResponseEntity<List<Member>> findEligibleMemberRegToBloodGrp(@RequestBody Map<String,Object> request){
        String bloodGroup =(String) request.get("bloodGroup");
        return new ResponseEntity<>(donorService.findEligibleMemberRegToBloodGrp(bloodGroup),HttpStatus.OK);
    }

    @GetMapping("/findSpecificBloodGroupMember")
    public  ResponseEntity<List<Member>> findSpecificBloodGroupMember(@RequestBody Map<String,Object> request){
        String bloodGroup =(String) request.get("bloodGroup");
        return new ResponseEntity<>(donorService.findSpecificBloodGroup(bloodGroup),HttpStatus.OK);
    }

    @PutMapping("/updateLastDonatedIsToday")
    public ResponseEntity<Member> updateMemberLastDonated(@RequestBody Member member){
        LocalDate localDate =LocalDate.now();
        member.setLastDonatedOn(Date.valueOf(localDate));
        Member updatedData= donorService.updateMember(member);
        return  new ResponseEntity<>(updatedData,HttpStatus.CREATED);
    }

    @GetMapping("/findAllMemberDetail")
    public  ResponseEntity<List<MemberDetail>> findAllMemberDetail(){
        return new ResponseEntity<>(donorService.findAllMemberDetail(),HttpStatus.OK);
    }


}

