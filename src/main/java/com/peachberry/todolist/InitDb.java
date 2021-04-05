package com.peachberry.todolist;

import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Category;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitMember initMember;

    @PostConstruct
    public void init() {
        initMember.createMember();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitMember {

        private final PasswordEncoder encoder;
        private final EntityManager em;

        public void createMember() {
            Authority userAuth = new Authority(Role.USER);
            Authority adminAuth = new Authority(Role.ADMIN);
            em.persist(userAuth);
            em.persist(adminAuth);

            Member member = Member.builder()
                    .email("peachberry2@kakao.com")
                    .password(encoder.encode("1234"))
                    .name("baek")
                    .authority(userAuth)
                    .build();

            em.persist(member);

            Category category = new Category(
                    "하루일과",
                    member
            ) ;
            em.persist(category);

            Category category3 = new Category(
                    "하루일과3",
                    member
            ) ;
            em.persist(category3);
        }
    }
}
