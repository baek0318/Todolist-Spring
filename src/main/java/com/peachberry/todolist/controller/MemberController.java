package com.peachberry.todolist.controller;

import com.peachberry.todolist.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/detail")
    public ResponseEntity<?> findMemberDetail() {
        //memberService.findMember();

        return ResponseEntity.ok().build();
    }
}
