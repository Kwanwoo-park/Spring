package com.example.spring.web;

import com.example.spring.config.auth.dto.SessionUser;
import com.example.spring.service.posts.PostsService;
import com.example.spring.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model) {/*    -서버 템플릿 엔진에서 사용할 수 있는 객체를 저장할 수 있습니다.
                                            -여기서는 postsService.findAllDesc()로 가져온 결과를 posts로
                                            index.mustache에 전달합니다.*/
        model.addAttribute("posts", postsService.findAllDesc());
        SessionUser user = (SessionUser) httpSession.getAttribute("user"); /*   -앞서 작성된 CustomOAuth2UserService
        에서 로그인 성공시 세션에 SessionUser를 저장하도록 구성 즉, 로그인 성공 시 httpSession.getAttribute("user")에서 값을
        가지고 올 수 있습니다.*/

        if (user != null) { /*  -세션에 저장된 갑싱 있을 때만 model에 userName으로 등록합니다.
                                -세션에 저장된 값이 없으면 model엔 아무런 값이 없어서 로그인 버튼이 보입니다.*/
            model.addAttribute("userName", user.getName());
        }

        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }
}
