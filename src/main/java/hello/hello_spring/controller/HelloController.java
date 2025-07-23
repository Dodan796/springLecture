package hello.hello_spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model){
        // model 안에 data 변수를 만들어서 hello!! 입력값을 넣어놓음
        model.addAttribute("data","hello!!");

        // templates 패키지 아래 hello 파일로 이동한다.
        // Springboot 템플릿엔진의 default 경로는
        // => resources:templates/ + {ViewName}+ .html로 바로 연동이 된다.
        return "hello";

    }

}
