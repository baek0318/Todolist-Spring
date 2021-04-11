package com.peachberry.todolist.controller;

import com.peachberry.todolist.controller.dto.MemberResponse;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/{member-id}")
    public ResponseEntity<?> findMemberDetail(@PathVariable(name = "member-id") Long id) {

        Member member = memberService.findById(id);

        return ResponseEntity.ok(new MemberResponse.MemberInfo(member));
    }
}
