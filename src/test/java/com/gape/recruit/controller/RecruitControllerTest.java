package com.gape.recruit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gape.recruit.domain.Company;
import com.gape.recruit.domain.Recruit;
import com.gape.recruit.domain.Users;
import com.gape.recruit.dto.recruit.RecruitDto;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class RecruitControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EntityManager em;

    @MockBean
    private RecruitService recruitService;

    @MockBean
    private UserService userService;


    @MockBean
    private CompanyService companyService;

    @Before
    public void setup() {
        Company company = createCompany("company1", "korea", "seoul");
        Company company2 = createCompany("company2", "aerok", "busan");
        Recruit recruit1 = Recruit.createRecruit(company, "Frontend",1000000L,"react/typescript", "find frontend developer" );
        Recruit recruit2 = Recruit.createRecruit(company2, "Frontend1",1000000L,"react/typescript1", "find frontend developer1" );
        Recruit recruit3 = Recruit.createRecruit(company, "Frontend2",1000000L,"react/typescript2", "find frontend developer2" );

        List<Recruit> recruits = new ArrayList<>();
        recruits.add(recruit1);
        recruits.add(recruit2);
        recruits.add(recruit3);

        Users user = createUser("userA", 1L);
        Users user1 = createUser("userB", 2L);

        when(userService.findOne(1L)).thenReturn(user);
        when(recruitService.findOne(1L)).thenReturn(recruit1);
        when(companyService.findOne(1L)).thenReturn(company);
        when(recruitService.findAll()).thenReturn(recruits);
        List<Recruit> searchResults = Arrays.asList(recruit1, recruit2, recruit3);
        when(recruitService.findByKeyword("react")).thenReturn(Optional.of(searchResults));
        when(recruitService.delete(1L)).thenReturn(1L);
        when(recruitService.apply(1L, user)).thenReturn(1L);
        when(recruitService.apply(anyLong(), any())).thenReturn(1L);
    }

    @Test
    public void 공고_조회() throws Exception {
        mockMvc.perform(get("/api/recruits/{id}", 1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.reward").value(1000000))
                .andExpect(jsonPath("$.data.position").value("Frontend"))
                .andExpect(jsonPath("$.data.techStack").value("react/typescript"))
                .andReturn();
    }

    @Test
    public void 공고_목록_조회() throws Exception {
        mockMvc.perform(get("/api/recruits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(3))
                .andExpect(jsonPath("$.data[0].position").value("Frontend"));
    }

    @Test
    public void 공고_조회_상세페이지() throws Exception {
        mockMvc.perform(get("/api/recruits/{id}/detail", 1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.reward").value(1000000))
                .andExpect(jsonPath("$.data.position").value("Frontend"))
                .andExpect(jsonPath("$.data.techStack").value("react/typescript"))
                .andExpect(jsonPath("$.data.companyName").value("company1"))
                .andExpect(jsonPath("$.data.country").value("korea"))
                .andExpect(jsonPath("$.data.region").value("seoul"))
                .andReturn();
    }

    @Test
    public void 공고_키워드_검색() throws Exception {
        mockMvc.perform(get("/api/recruits/search").param("search", "react").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].position").value("Frontend"))
                .andExpect(jsonPath("$.[1].position").value("Frontend1"))
                .andExpect(jsonPath("$.[2].position").value("Frontend2"));
    }

    @Test
    public void 공고_게시() throws Exception {
        RecruitDto.CreateRecruitRequest request = new RecruitDto.CreateRecruitRequest();
        request.setCompanyId(1L);
        request.setPosition("backend");
        request.setReward(1000000L);
        request.setTechStack("java/spring");
        request.setContent("We are looking for a backend developer.");

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/recruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.length()").value(1));

    }

    @Test
    public void 공고_등록() throws Exception {
        RecruitDto.ApplyRecruitRequest request = new RecruitDto.ApplyRecruitRequest();

        request.setUserId(1L);

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/recruits/{id}/apply", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId").value(1));
    }

    @Test
    public void 공고_삭제() throws Exception {
        mockMvc.perform(delete("/api/recruits/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1));
    }

    @Test
    public void 공고_수정() throws Exception {
        RecruitDto.UpdateRecruitRequest request = new RecruitDto.UpdateRecruitRequest();
        request.setReward(1234L);
        request.setContent("수정된 컨텐츠 입니다.");
        request.setPosition("수정된 포지션입니다.");
        request.setTechStack("수정된 기술입니다.");

        String requestBody = objectMapper.writeValueAsString(request);

        Recruit updatedRecruit = new Recruit();
        updatedRecruit.setId(1L);
        updatedRecruit.setReward(1234L);
        updatedRecruit.setContent("수정된 컨텐츠 입니다.");
        updatedRecruit.setPosition("수정된 포지션입니다.");
        updatedRecruit.setTechStack("수정된 기술입니다.");

        when(recruitService.findOne(1L)).thenReturn(updatedRecruit);

        mockMvc.perform(patch("/api/recruits/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content").value("수정된 컨텐츠 입니다."))
                .andExpect(jsonPath("position").value("수정된 포지션입니다."))
                .andExpect(jsonPath("techStack").value("수정된 기술입니다."));
    }


    public Company createCompany(String name, String country, String region) {
        Company company = new Company();
        company.setName(name);
        company.setCountry(country);
        company.setRegion(region);

        when(companyService.join(company)).thenReturn(company.getId());
        return company;
    }

    public Users createUser(String name, Long id) {
        Users user = new Users();
        user.setId(id);
        user.setName(name);
        when(userService.join(user)).thenReturn(user.getId());
        return user;
    }
}
