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

@Repository
public class AuthorityRepository {

    private final Logger logger = LoggerFactory.getLogger(AuthorityRepository.class);

    @PersistenceContext
    private EntityManager em;

    /**
     * authority를 DB에 저장해준다
     * @param authority authority객체를 넘겨주면 된다
     */
    public void save(Authority authority) {
        em.persist(authority);
        logger.info("member save success");
    }

    /**
     * 주어진 Role을 바탕으로 해당 Role이 존재하는지 확인
     * @param role 확인하고자하는 Role
     * @return 있다면 true 없다면 false
     */
    public Boolean existByRole(Role role) {
        if(findByRole(role) != null) {
            logger.error("member already exist");
            return true;
        }
        else {
            logger.info("member doesn't exist");
            return false;
        }
    }

    /**
     * 주어진 Role을 DB에 접근해서 찾아준다
     * @param role 찾고자하는 Role
     * @return 찾았다면 Authority 객체를 넘겨준다 아니면 null을 넘긴다
     */
    public Authority findByRole(Role role) {
        try {
            Authority authority = em.createQuery("select a from Authority a where a.role = :role", Authority.class)
                    .setParameter("role", role)
                    .getSingleResult();
            logger.info("success find member");
            return authority;
        }catch(NoResultException e){
            logger.error("can not find role {}" ,e.getMessage());
            return null;
        }
    }
}
