package hello.hello_spring.controller;

import hello.hello_spring.domain.Member;
import hello.hello_spring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MemberController {

    private final MemberService memberService;

    // 1. 생성자 주입을 통한 MemberService => 가장 권장되는 방법.
    // @Autowired: MemberSerivce를 Spirng이 가져다가 연결을 해준다.
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 2. 필드 주입 => 유지보수가 어려운 방법
    /*
     * @Autowired
     * private MemberService memberService;
     *
     */

    // 3. Setter 주입 => Controller를 호출시, public으로 호출이 되어야 한다.
    /*
     * @Autowired
     * public void setMemberService(MemberService memberService){
     *  this.memberService = memberService;
     * }
     */

    @GetMapping("/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());

        System.out.println("member = " + member.getName());

        memberService.join(member);

        return "redirect:/";

    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }


}
