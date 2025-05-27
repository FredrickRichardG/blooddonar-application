package com.i2i.blooddonor.service;

import com.i2i.blooddonor.model.Member;
import com.i2i.blooddonor.repository.DonorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService
{

    private final DonorRepository donorRepository;

    public CustomUserDetailService(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = donorRepository.findByUserName(username);
        if(member==null){
            throw new UsernameNotFoundException("user not found");
        }
        return new User(member.getName(),member.getName(),new ArrayList<>());
    }
}
