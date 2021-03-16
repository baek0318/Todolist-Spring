package com.peachberry.todolist.service;

import com.peachberry.todolist.domain.Category;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.controller.dto.category.CategorySaveDTO;
import com.peachberry.todolist.repository.CategoryRepository;
import com.peachberry.todolist.repository.MemberRepository;
import com.peachberry.todolist.service.dto.category.CategoryServiceDto;
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
    public Long saveCategory(CategoryServiceDto.Save saveDto) {
        //카테고리를 저장할때 어떤 유저의 카테고리인지 정해주고 넣어줘야한다
        Member member = memberRepository.findById(saveDto.getMemberId());
        findDuplicateTitle(saveDto.getTitle(), saveDto.getMemberId());
        Category category = new Category(saveDto.getTitle(), member);
        categoryRepository.save(category);

        return category.getId();
    }

    private void findDuplicateTitle(String title, Long member_id) {
        List<Category> category = categoryRepository.findByTitle(title, member_id);
        if(!category.isEmpty()){
            throw new IllegalStateException("동일한 카테고리가 존재합니다");
        }
    }

    @Transactional
    public Category findByTitle(CategoryServiceDto.FindByTitle byTitleDto) {
        //해당아이디에 같은 주제의 카테고리가 있는지 확인
        List<Category> result = categoryRepository.findByTitle(byTitleDto.getTitle(), byTitleDto.getMemberId());
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
    public Long reviseTitle(CategoryServiceDto.UpdateTitle updateTitleDto) {
        //해당아이디의 특정 카테고리의 주제를 수정
        return categoryRepository.reviseCategory(updateTitleDto.getTitle(), updateTitleDto.getId());
    }

    @Transactional
    public void deleteCategory(Long category_id) {
        //해당아이디의 특정 카테고리 삭제
        categoryRepository.deleteById(category_id);
    }
}
