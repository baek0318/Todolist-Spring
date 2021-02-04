package com.peachberry.todolist.repository;

import com.peachberry.todolist.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
     * @param member 현재 로그인 되어있는 멤버
     * @return 찾은 값들을 List로 반환한다
     */
    public List<Todo> findAll(Member member) {
        return em.createQuery("select td from Todo td where td.member.id = :id", Todo.class)
                .setParameter("id", member.getId())
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
     * @param member 현재 로그인된 member
     * @return 해당하는 todo를 list로 반환한다
     */
    public List<Todo> findByStatus(TodoStatus status, Member member) {
        return em.createQuery("select td from Todo td where td.status = :status and td.member.id = :id", Todo.class)
                .setParameter("status", status)
                .setParameter("id", member.getId())
                .getResultList();
    }

    /**
     * 날짜 정보를 가지고 todo를 찾는다
     * @param date 찾고 싶은 날짜
     * @param member 현재 로그인된 member
     * @return 해당하는 todo를 list로 반환한다
     */
    public List<Todo> findByCalendar(Calendar date, Member member) {
        return em.createQuery("select td from Todo td where td.calendar =:date and td.member.id = :id", Todo.class)
                .setParameter("date", date)
                .setParameter("id", member.getId())
                .getResultList();
    }

    public List<Todo> findByCategory(Category category, Member member) {
        return em.createQuery("select td from Todo td where td.category = :category and td.member.id = :id", Todo.class)
                .setParameter("category", category)
                .setParameter("id", member.getId())
                .getResultList();
    }

    /**
     * Todo status를 변경할 수 있다
     * @param status 변경할려는 status
     * @param id 해당하는 id
     */
    public Long reviseStatus(TodoStatus status, Long id) {
        Todo todo = findById(id);
        todo.changeStatus(status);
        save(todo);
        return todo.getId();
    }

    /**
     * Todo title을 변경할 수 있다
     * @param title 변경할려는 title
     * @param id 해당하는 id
     */
    public Long reviseTodo(String title, Long id) {
        Todo todo = findById(id);
        todo.changeTitle(title);
        save(todo);
        return todo.getId();
    }

    /**
     * Todo calendar를 변경할 수 있다
     * @param date 변경할려는 date
     * @param id 해당하는 id
     */
    public Long reviseCalendar(Calendar date, Long id) {
        Todo todo = findById(id);
        todo.changeCalendar(date);
        save(todo);
        return todo.getId();
    }

    /**
     * Todo를 삭제할 수 있다
     * @param id 해당하는 id
     */
    public void deleteById(Long id) {
        Todo todo = em.find(Todo.class, id);
        em.remove(todo);
        logger.info("todo delete success");
    }

}
