package com.peachberry.todolist.repository;

import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorityRepository {

    private final Logger logger = LoggerFactory.getLogger(AuthorityRepository.class);

    @PersistenceContext
    private EntityManager em;

    /**
     * authority를 DB에 저장해준다
     * @param authority authority객체를 넘겨주면 된다
     */
    public Long save(Authority authority) {
        em.persist(authority);
        logger.info("member save success");
        return authority.getId();
    }

    /**
     * 주어진 Role을 DB에 접근해서 찾아준다
     * @param role 찾고자하는 Role
     * @return 찾았다면 Authority 객체를 넘겨준다 아니면 null을 넘긴다
     */
    public Optional<Authority> findByRole(Role role) {
        return em.createQuery("select a from Authority a where a.role = :role", Authority.class)
                .setParameter("role", role)
                .getResultStream()
                .findFirst();
    }

    public Authority findById(Long id) {
        return em.find(Authority.class, id);
    }
}
