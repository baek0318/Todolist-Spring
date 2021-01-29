package com.peachberry.todolist.repository;

import com.peachberry.todolist.domain.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {

    private final Logger logger = LoggerFactory.getLogger(MemberRepository.class);

    @PersistenceContext
    private EntityManager em;


    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    /**
     * id로 member를 찾아주는 메서드
     * @param id db에 사용된 아이디
     * @return 찾은 member 객체
     */
    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    /**
     * email로 member를 찾아주는 메서드
     * @param email 찾고자 하는 이메일
     * @return member 객체
     */
    public List<Member> findByEmail(String email) {
        return em.createQuery("select m from Member m where m.email = :email",Member.class)
                .setParameter("email", email)
                .getResultList();
    }

    /**
     * Member table에 있는 모든 Member를 찾아주는 메서드
     * @return Member 객체의 리스트
     */
    public List<Member> findMembers() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }


}
