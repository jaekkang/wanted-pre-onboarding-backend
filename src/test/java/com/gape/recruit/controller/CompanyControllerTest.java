package com.gape.recruit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gape.recruit.domain.Company;
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
public class CompanyControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EntityManager em;


    @MockBean
    private UserService userService;

    @MockBean
    private RecruitService recruitService;

    @MockBean
    private CompanyService companyService;

    @Before
    public void setup() {
        Company company1 = createCompany("company1", "korea", "seoul");
        Company company2 = createCompany("company2", "korea111", "seoul111");
        Company company3 = createCompany("company3", "korea222", "seoul222");

        List<Company> companies = new ArrayList<>();
        companies.add(company1);
        companies.add(company2);
        companies.add(company3);

        when(companyService.findOne(1L)).thenReturn(company1);
        when(companyService.findAll()).thenReturn(companies);
    }

    @Test
    public void 회사_목록_조회() throws Exception {
        mockMvc.perform(get("/api/companies")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(3))
                .andExpect(jsonPath("$.data[2].name").value("company3"));
    }

    public Company createCompany(String name, String country, String region) {
        Company company = new Company();
        company.setName(name);
        company.setCountry(country);
        company.setRegion(region);

        when(companyService.join(company)).thenReturn(company.getId());
        return company;
    }
}
