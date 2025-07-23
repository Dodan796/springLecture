package hello.hello_spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) {
        // model 안에 data 변수를 만들어서 hello!! 입력값을 넣어놓음
        model.addAttribute("data", "hello!!");

        // templates 패키지 아래 hello 파일로 이동한다.
        // Springboot 템플릿엔진의 default 경로는
        // => resources:templates/ + {ViewName}+ .html로 바로 연동이 된다.
        return "hello";
    }

    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model) {
        model.addAttribute("name", name);
        return "hello-template";
    }

    // @ResponseBody => HTTP에서 Body 부분에 return값을 바로 넣는 어노테이션
    @ResponseBody
    @GetMapping("hello-string")
    public String helloString(@RequestParam("name") String name) {
        return "hello " + name;
    }

    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name) {

        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }

    static class Hello {
        private String name;


        // Property 접근방식
        // Getter: 객체의 private 필드 값을 외부에서 읽을 수 있도록 해주는 메서드
        public String getName() {
            return name;
        }

        // Setter: 객체의 private 필드 값을 외부에서 변경할 수 있도록 해주는 메서드
        public void setName(String name) {
            this.name = name;
        }
    }
}


