package com.example.spring.web;

import com.example.spring.config.auth.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)/*  -테스트를 진행할 때 JUnit에 내장된 실행자 외에 다른 실행자를 실행시킵니다.
                                -여기서는 SpringRunner라는 스프링 실행자를 사용합니다.
                                -즉, 스프링 부트 테스트와 JUnit 사이에 연결자 역할을 합니다.*/
@WebMvcTest(controllers = HelloController.class, /*  -여러 스프링 테스트 어노테이션 중, Web(Spring MVC)에 집중할 수 있는 어노테이션입니다.
                                                    -선언할 경우 @Controller, @ControllerAdvice를 사용할 수 있습니다.
                                                    -단, @Service, @Component, @Repository등은 사용할 수 없습니다.
                                                    -여기서는 컨트롤러만 사용하기 때문에 선언합니다.*/
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        }
)
public class HelloControllerTest {
    @Autowired // -스프링이 관리하는 빈(Bean)을 주입 받습니다.
    private MockMvc mvc; /* -웹 API를 테스트할 때 사용합니다.
                            -스프링 MVC 테스트의 시작접입니다.
                            -이 클래스를 통해 HTTP GET, POST 등에 대한 API 테스트를 할 수 있습니다.*/

    @WithMockUser(roles = "USER")
    @Test
    public void hello가_리턴된다() throws Exception {
        String hello = "hello";

        mvc.perform(get("/hello"))/*    -MockMvc를 통해 /hello 주소로 HTTP GET 요청을 합니다.
                                                    -체이닝이 지원되어 아래와 같이 여러 검증 기능을 이어서 선언할 수 있습니다.*/
                .andExpect(status().isOk())/*   -mvc.perform의 결과를 검증합니다.
                                                -HTTP Header status를 검증합니다.
                                                -우리가 흔히 알고 있는 200, 404, 500 등의 상태를 검증합니다.
                                                -여기선 OK 즉, 200인지 아닌지를 검증합니다.*/
                .andExpect(content().string(hello));/*  -mvc.perform의 결과를 검증합니다.
                                                        -응답 본문의 내용을 검증합니다.
                                                        -Controller에서 "hello"를 리턴하기 때문에 이 값이 맞는지 검증합니다.*/
    }

    @WithMockUser(roles = "USER")
    @Test
    public void helloDto가_리턴된다() throws Exception {
        String name = "hello";
        int amount = 1000;

        mvc.perform(get("/hello/dto").param("name", name).param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));

        /*-param
        * -API 테스트할 때 사용될 요청 파라미터를 설정합니다.
        * -단, 값은 String만 허용됩니다.
        * -그래서 숫자/날짜 등의 데이터도 등록할 때는 문자열로 변경해야만 가능합니다.*/

        /*-jsonPath
        * -JSON응답값을 필드별로 검증할 수 있는 메소드입니다.
        * -$를 기준으로 필드명을 명시합니다.
        * -여기서는 name과 amount를 검증하니 $.name, %.amount로 검증합니다.*/
    }
}
