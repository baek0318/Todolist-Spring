package com.peachberry.todolist;

import com.peachberry.todolist.controller.dto.TodoRequest;
import com.peachberry.todolist.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class InitDb {

    private final InitMember initMember;

    public InitDb(InitMember initMember) {
        this.initMember = initMember;
    }

    @PostConstruct
    public void init() {
        initMember.createMember();
    }

    @Component
    @Transactional
    static class InitMember {

        private final PasswordEncoder encoder;
        private final EntityManager em;

        public InitMember(PasswordEncoder encoder, EntityManager em) {
            this.encoder = encoder;
            this.em = em;
        }

        public void createMember() {
            Authority userAuth = new Authority(Role.USER);
            em.persist(userAuth);

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

            Category category2 = new Category(
                    "하루일과3",
                    member
            ) ;
            em.persist(category2);

            Todo todo1 = new Todo(
                    member,
                    category,
                    LocalDate.now(),
                    "밥먹기",
                    TodoStatus.ING
            );
            em.persist(todo1);

            Todo todo2 = new Todo(
                    member,
                    category,
                    LocalDate.now(),
                    "학교가기",
                    TodoStatus.ING
            );
            em.persist(todo2);
        }
    }
}
