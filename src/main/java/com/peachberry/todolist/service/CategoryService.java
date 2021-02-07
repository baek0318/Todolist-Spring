package com.peachberry.todolist.service;

import com.peachberry.todolist.domain.Category;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.dto.CategoryDTO;
import com.peachberry.todolist.repository.CategoryRepository;
import com.peachberry.todolist.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final MemberRepository memberRepository;

    public CategoryService(CategoryRepository categoryRepository, MemberRepository memberRepository) {
        this.categoryRepository = categoryRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void saveCategory(Long member_id, String title) {
        //카테고리를 저장할때 어떤 유저의 카테고리인지 정해주고 넣어줘야한다
        Member member = memberRepository.findById(member_id);
        findDuplicateTitle(title, member_id);
        Category category = new Category(title, member);
        categoryRepository.save(category);
    }

    private void findDuplicateTitle(String title, Long member_id) {
        List<Category> category = categoryRepository.findByTitle(title, member_id);
        if(!category.isEmpty()){
            throw new IllegalStateException("동일한 카테고리가 존재합니다");
        }
    }

    @Transactional
    public Category findByTitle(String title, Long member_id) {
        //해당아이디에 같은 주제의 카테고리가 있는지 확인
        List<Category> result = categoryRepository.findByTitle(title, member_id);
        if(result.isEmpty()) {
            throw new IllegalStateException("동일한 카테고리가 존재하지 않습니다");
        }
        return result.get(0);
    }

    @Transactional
    public List<Category> findAll(Long member_id) {
        return categoryRepository.findAll(member_id);
    }

    @Transactional
    public void reviseTitle(String title, Long category_id) {
        //해당아이디의 특정 카테고리의 주제를 수정
        categoryRepository.reviseCategory(title, category_id);
    }

    @Transactional
    public void deleteCategory(Long category_id) {
        //해당아이디의 특정 카테고리 삭제
        categoryRepository.deleteById(category_id);
    }
}
