package com.gape.recruit.service;

import com.gape.recruit.domain.Users;
import com.gape.recruit.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
public class UserServiceTest {
    @Autowired UserService userService;

    @Autowired
    EntityManager em;

    @Test
    public void 사용자_조회() throws Exception {
        // given
        Users user = createUser("user1");

        // when
        Users findUser = userService.findOne(user.getId());

        // then
        assertEquals(user.getId(), findUser.getId(), "생성한 user가 service를 통해서 조회가 되어야됩니다.");
        assertEquals(user.getName(), findUser.getName(), "조회된 사용자와 생성한 사용자의 name이 같아야 됩니다.");
    }

    @Test
    public void 사용자_목록_조회() throws Exception {
        // given
        Users user1 = createUser("user1");
        Users user2 = createUser("user2");

        // when
        int size = userService.findAll().size();

        // then
        assertEquals(2, size, "생성한 사용자의 수와 Service를 통해 불러온 저장된 데이터의 수가 같아야 합니다.");
    }

    public Users createUser(String name) {
        Users user = new Users();
        user.setName(name);
        userService.join(user);
        return user;
    }
}