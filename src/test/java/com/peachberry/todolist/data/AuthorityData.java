package com.peachberry.todolist.data;

import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Role;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class AuthorityData {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void saveAuthority(Role role) {
        Authority userAuth = new Authority(role);
        em.persist(userAuth);
    }
}
