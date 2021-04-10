package com.peachberry.todolist.repository;

import com.peachberry.todolist.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TodoRepository {

    private static final Logger logger = LoggerFactory.getLogger(TodoRepository.class);

    @PersistenceContext
    private EntityManager em;


    public Long save(Todo todo) {
        if(todo.getId() == null){
            em.persist(todo);
            logger.info("todo save success");
        }
        else {
            em.merge(todo);
            logger.info("todo update success");
        }
        return todo.getId();
    }

    /**
     * 로그인 되어있는 member의 모든 todo를 불러온다
     * @param member_id 현재 로그인 되어있는 멤버
     * @return 찾은 값들을 List로 반환한다
     */
    public List<Todo> findAll(Long member_id) {
        return em.createQuery("select td from Todo td where td.member.id = :id", Todo.class)
                .setParameter("id", member_id)
                .getResultList();
    }

    /**
     * id로 todo를 찾는다
     * @param id DB에 적용된 id값
     * @return id에 부합하는 todo값     */
    public Todo findById(Long id) {
        return em.find(Todo.class, id);
    }

    /**
     * 완료상태를 가지고 값을 찾는다
     * @param status 찾고 싶은 상태
     * @param member_id 현재 로그인된 member
     * @return 해당하는 todo를 list로 반환한다
     */
    public List<Todo> findByStatus(TodoStatus status, Long member_id) {
        return em.createQuery("select td from Todo td where td.status = :status and td.member.id = :id", Todo.class)
                .setParameter("status", status)
                .setParameter("id", member_id)
                .getResultList();
    }

    /**
     * 날짜 정보를 가지고 todo를 찾는다
     * @param date 찾고 싶은 날짜
     * @param member_id 현재 로그인된 member
     * @return 해당하는 todo를 list로 반환한다
     */
    public List<Todo> findByDateTime(LocalDate date, Long member_id) {
        return em.createQuery("select td from Todo td where td.date =:date and td.member.id = :id", Todo.class)
                .setParameter("date", date)
                .setParameter("id", member_id)
                .getResultList();
    }

    public List<Todo> findByCategory(Long category_id, Long member_id) {
        return em.createQuery("select td from Todo td where td.category.id = :category_id and td.member.id = :member_id", Todo.class)
                .setParameter("category_id", category_id)
                .setParameter("member_id", member_id)
                .getResultList();
    }

    /**
     * Todo를 삭제할 수 있다
     * @param todo_id 해당하는 id
     */
    public void deleteById(Long todo_id) {
        Todo todo = em.find(Todo.class, todo_id);
        em.remove(todo);
        logger.info("todo delete success");
    }

    /**
     * Todo를 Update
     * @param todo 값이 바뀐 객체
     * @return 바뀐 객체의 아이디 값을 반환
     */
    public Long update(Todo todo) {
        save(todo);
        return todo.getId();
    }
}
