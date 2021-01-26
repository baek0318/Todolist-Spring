package com.peachberry.todolist.repository;

import com.peachberry.todolist.domain.Calendar;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.domain.Todo;
import com.peachberry.todolist.domain.TodoStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        em.persist(todo);
        logger.info("todo save success");
        return todo.getId();
    }

    /**
     * 로그인 되어있는 member의 모든 todo를 불러온다
     * @param member 현재 로그인 되어있는 멤버
     * @return 찾은 값들을 List로 반환한다
     */
    public List<Todo> findAll(Member member) {
        return em.createQuery("select td from Todo td where td.member = :member", Todo.class)
                .setParameter("member", member)
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
        return em.createQuery("select td from Todo td where td.status = :status and td.member = :member", Todo.class)
                .setParameter("status", status)
                .setParameter("member", member)
                .getResultList();
    }

    /**
     * 날짜 정보를 가지고 todo를 찾는다
     * @param date 찾고 싶은 날짜
     * @param member 현재 로그인된 member
     * @return 해당하는 todo를 list로 반환한다
     */
    public List<Todo> findByCalendar(Calendar date, Member member) {
        return em.createQuery("select td from Todo td where td.calendar =:date and td.member = :member", Todo.class)
                .setParameter("date", date)
                .setParameter("member", member)
                .getResultList();
    }

    /**
     * Todo status를 변경할 수 있다
     * @param status 변경할려는 status
     * @param id 해당하는 id
     */
    public void reviseStatus(TodoStatus status, Long id) {
        em.createQuery("update Todo set Todo.status = :status where Todo.id = :id")
                .setParameter("status", status)
                .setParameter("id", id);
    }

    /**
     * Todo title을 변경할 수 있다
     * @param title 변경할려는 title
     * @param id 해당하는 id
     */
    public void reviseTodo(String title, Long id) {
        em.createQuery("update Todo set Todo.title = :title where Todo.id = :id")
                .setParameter("title", title)
                .setParameter("id", id);
    }

    /**
     * Todo calendar를 변경할 수 있다
     * @param date 변경할려는 date
     * @param id 해당하는 id
     */
    public void reviseCalendar(Calendar date, Long id) {
        em.createQuery("update Todo set Todo.calendar = :date where Todo.id = :id")
                .setParameter("date", date)
                .setParameter("id", id);
    }

    /**
     * Todo를 삭제할 수 있다
     * @param id 해당하는 id
     */
    public void deleteById(Long id) {
        em.createQuery("delete from Todo where Todo.id = :id")
                .setParameter("id", id);
    }


}