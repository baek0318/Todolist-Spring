package com.peachberry.todolist.service;

import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Role;
import com.peachberry.todolist.repository.AuthorityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Transactional
    public Authority findAuthority(Role role) {
        Optional<Authority> optionalAuthority = authorityRepository.findByRole(role);
        nullCheck(optionalAuthority, role);
        return optionalAuthority.get();
    }

    private void nullCheck(Optional<Authority> optionalAuthority, Role role) {
        if(optionalAuthority.isEmpty()) {
            saveAuthority(role);
        }
    }

    private void saveAuthority(Role role) {
        authorityRepository.save(new Authority(role));
    }
}
