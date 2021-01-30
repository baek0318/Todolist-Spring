package com.peachberry.todolist.service;

import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Role;
import com.peachberry.todolist.repository.AuthorityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Transactional
    public Authority saveAuthority(Authority authority) {
        List<Authority> authorities = authorityRepository.findByRole(authority.getRole());
        if(!authorities.isEmpty()){
            return authorities.get(0);
        }else {
            return authorityRepository.findById(authorityRepository.save(authority));
        }
    }
}
