package com.gape.recruit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gape.recruit.domain.Users;
import com.gape.recruit.service.CompanyService;
import com.gape.recruit.service.RecruitService;
import com.gape.recruit.service.UserService;
import jakarta.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EntityManager em;

    @MockBean
    private UserService userService;

    @MockBean
    private CompanyService companyService;

    @MockBean
    private RecruitService recruitService;

    @Before
    public void setup() {
        Users user1 = createUser("UserA", 1L);
        Users user2 = createUser("UserB", 2L);
        Users user3 = createUser("UserC", 3L);
        Users user4 = createUser("UserD", 4L);

        List<Users> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);

        when(userService.findOne(1L)).thenReturn(user1);
        when(userService.findAll()).thenReturn(users);
    }

    @Test
    public void 유저_목록_조회() throws Exception  {
        mockMvc.perform(get("/api/users")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(4))
                .andExpect(jsonPath("$.data[0].name").value("UserA"));
    }

    public Users createUser(String name, Long id) {
        Users user = new Users();
        user.setId(id);
        user.setName(name);
        when(userService.join(user)).thenReturn(user.getId());
        return user;
    }
}
