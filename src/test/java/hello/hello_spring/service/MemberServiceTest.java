package hello.hello_spring.service;

import hello.hello_spring.domain.Member;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * MemberService Test Code
 */
class MemberServiceTest {

    MemberService memberService;
    MemberRepository memberRepository;

    @BeforeEach
    public void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @AfterEach
    public void afterEach() {
        memberRepository.clearStore();
    }

    // Test 코드를 작성하는 부분은 한글로 수정해도 문제없다.
    @Test
    void 회원가입() {
        // Test 3과정
        //1. given => 어떤걸 주어지면
        Member member = new Member();
        member.setName("spring");

        //2. when => 이걸 실행시에
        Long saveId = memberService.join(member);

        //3. then => 이런 결과가 나와야 한다
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());


    }

    @Test
    public void 중복_회원_예외() {
        // 1. given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        // 2. when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        // 2-1. try catch 활용한 Exception 처리
        /*try {
            memberService.join(member2);
            fail();
        }catch(IllegalStateException e){
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.123123");
        }*/

        // 3. then


    }


    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}