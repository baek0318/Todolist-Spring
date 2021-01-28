package com.peachberry.todolist.repository;

import com.peachberry.todolist.domain.Category;
import com.peachberry.todolist.domain.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class CategoryRepository {

    private Logger logger = LoggerFactory.getLogger(CategoryRepository.class);

    @PersistenceContext
    private EntityManager em;

    public Long save(Category category) {
        if(category.getId() == null) {
            em.persist(category);
            logger.info("save category success!!");
        }
        else{
            em.merge(category);
            logger.info("change category success!!");
        }

        return category.getId();
    }

    public Category findById(Long id) {
        return em.find(Category.class, id);
    }

    public Category findByTitle(String title, Member member) {
        return em.createQuery("select c from Category c where c.title = :title and c.member = :member", Category.class)
                .setParameter("title", title)
                .setParameter("member", member)
                .getSingleResult();
    }

    public Long reviseCategory(String title, Long id){
        Category category = findById(id);
        category.changeTitle(title);
        save(category);
        return category.getId();
    }

    public void deleteById(Long id) {
        Category category = findById(id);
        em.remove(category);
    }

}
