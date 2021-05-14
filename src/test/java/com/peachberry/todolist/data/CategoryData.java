package com.peachberry.todolist.data;

import com.peachberry.todolist.domain.Category;
import com.peachberry.todolist.domain.Member;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class CategoryData {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Long saveCategory(String title, Long userId) {
        Member member = em.find(Member.class, userId);
        Category category = new Category(title, member);
        em.persist(category);
        return category.getId();
    }
}
