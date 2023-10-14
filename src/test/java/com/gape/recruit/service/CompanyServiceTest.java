package com.gape.recruit.service;

import com.gape.recruit.domain.Company;
import com.gape.recruit.repository.CompanyRepository;
import jakarta.persistence.EntityManager;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional()
public class CompanyServiceTest {
    @Autowired CompanyService companyService;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired EntityManager em;

    @Test
    public void 회사조회() throws Exception {
        // given
        Company company = createCompany("company", "korea", "seoul");

        // when
        Company findCompany = companyService.findOne(company.getId());

        // then
        assertEquals(company.getId(), findCompany.getId(), "새로 생성된 회사 정보가 저장소에서 잘 불러와져야됩니다.");
    }

    @Test
    public void 회사목록조회() throws Exception {
        // given
        Company company1 = createCompany("company1", "korea", "seoul");
        Company company2 = createCompany("company2", "korea", "busan");
        // when
        int size = companyService.findAll().size();
        String name = companyService.findOne(company1.getId()).getName();
        String country = companyService.findOne(company1.getId()).getCountry();
        // then
        assertEquals(2, size, "조회된 데이터 개수와 실행시 만들어지는 데이터의 숫자가 같아야합니다.");
        assertEquals("company1", name, "조회된 데이터의 name과 실행시 만들어지는 데이터의 name이 같아야 합니다.");
        assertEquals("korea", country, "조회된 데이터 region와 실행시 만들어지는 데이터 region이 같아야 합니다.");

    }
    public Company createCompany(String name, String country, String region) {
        Company company = new Company();
        company.setName(name);
        company.setCountry(country);
        company.setRegion(region);

        em.persist(company);
        return company;
    }
}