package com.peachberry.todolist.service;

import com.peachberry.todolist.domain.Category;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.dto.CategoryDTO;
import com.peachberry.todolist.repository.CategoryRepository;
import com.peachberry.todolist.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final MemberRepository memberRepository;

    public CategoryService(CategoryRepository categoryRepository, MemberRepository memberRepository) {
        this.categoryRepository = categoryRepository;
        this.memberRepository = memberRepository;
    }

    public void save(CategoryDTO categoryDTO) {
        //카테고리를 저장할때 어떤 유저의 카테고리인지 정해주고 넣어줘야한다
        Member m = memberRepository.findById(categoryDTO.getMember().getId());
        findDuplicateTitle(categoryDTO);
        Category category = new Category(categoryDTO.getCategory().getTitle(), m);
        categoryRepository.save(category);
    }

    private void findDuplicateTitle(CategoryDTO categoryDTO) {
        List<Category> category = categoryRepository.findByTitle(categoryDTO.getCategory().getTitle(), categoryDTO.getMember());
        if(!category.isEmpty()){
            throw new IllegalStateException("동일한 카테고리가 존재합니다");
        }
    }

    public List<Category> findByTitle(CategoryDTO categoryDTO) {
        //해당아이디에 같은 주제의 카테고리가 있는지 확인
        return categoryRepository.findByTitle(categoryDTO.getCategory().getTitle(), categoryDTO.getMember());
    }

    public List<Category> findAll(Member member) {
        return categoryRepository.findAll(member);
    }

    public void reviseTitle(String title, Category category) {
        //해당아이디의 특정 카테고리의 주제를 수정
        categoryRepository.reviseCategory(title, category.getId());
    }

    public void deleteCategory(Category category) {
        //해당아이디의 특정 카테고리 삭제
        categoryRepository.deleteById(category.getId());
    }
}
