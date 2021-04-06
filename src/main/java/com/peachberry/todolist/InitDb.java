package com.peachberry.todolist;

import com.peachberry.todolist.controller.dto.TodoRequest;
import com.peachberry.todolist.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

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

            Todo todo1 = new Todo(
                    member,
                    category,
                    LocalDateTime.now(),
                    "밥먹기",
                    TodoStatus.ING
            );
            em.persist(todo1);

            Todo todo2 = new Todo(
                    member,
                    category,
                    LocalDateTime.now(),
                    "학교가기",
                    TodoStatus.ING
            );
            em.persist(todo2);
        }
    }
}
