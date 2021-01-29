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
    public Long saveAuthority(Authority authority) {
        validateAuthority(authority);
        return authorityRepository.save(authority);

    }

    private void validateAuthority(Authority authority) {
        List<Authority> authorities = authorityRepository.findByRole(authority.getRole());
        if(!authorities.isEmpty()){
            throw new IllegalStateException("이미 존재하는 권한입니다");
        }
    }
}
