package hello.hello_spring.controller;

import hello.hello_spring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MemberController {

    private final MemberService memberService;

    // @Autowired: MemberSerivce를 Spirng이 가져다가 연결을 해준다.
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


}
