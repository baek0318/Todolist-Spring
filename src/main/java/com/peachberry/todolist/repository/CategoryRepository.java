package com.peachberry.todolist.repository;

import com.peachberry.todolist.domain.Category;
import com.peachberry.todolist.domain.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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

    public List<Category> findAll(Long member_id) {
        return em.createQuery("select c from Category c where c.member.id = :id", Category.class)
                .setParameter("id" , member_id)
                .getResultList();
    }

    public List<Category> findByTitle(String title, Long member_id) {
        return em.createQuery("select c from Category c where c.title = :title and c.member.id = :id", Category.class)
                .setParameter("title", title)
                .setParameter("id", member_id)
                .getResultList();
    }

    public Long reviseCategory(String title, Long category_id){
        Category category = findById(category_id);
        category.changeTitle(title);
        save(category);
        return category.getId();
    }

    public Long deleteById(Long category_id) {
        Category category = findById(category_id);
        em.remove(category);
        return category_id;
    }

}
