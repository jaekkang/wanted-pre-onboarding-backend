package com.gape.recruit.controller;

import com.gape.recruit.domain.Users;
import com.gape.recruit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    @ResponseBody
    public List<Users> findUsers() {
        Users user1 = new Users();
        user1.setName("test1");
        userService.join(user1);
        Users user2 = new Users();
        user2.setName("test2");
        userService.join(user2);
        Users user3 = new Users();
        user3.setName("test3");
        userService.join(user3);

        return userService.findAll();
    }
}
