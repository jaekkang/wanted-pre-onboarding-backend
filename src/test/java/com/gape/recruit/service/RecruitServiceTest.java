package com.gape.recruit.service;

import com.gape.recruit.domain.Company;
import com.gape.recruit.domain.Recruit;
import com.gape.recruit.domain.RecruitStatus;
import com.gape.recruit.domain.Users;
import com.gape.recruit.repository.RecruitRepository;
import jakarta.persistence.EntityManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
public class RecruitServiceTest {
    @Autowired RecruitService recruitService;
    @Autowired RecruitRepository recruitRepository;
    @Autowired CompanyService companyService;

    @Autowired
    EntityManager em;

    @Test
    public void 채용공고_추가() throws Exception {
        // given
        Company company = createCompany("company1", "korea", "seoul");

        Long recruitId = recruitService.post(company.getId(), 1000000L, "프론트엔드 개발자 구합니다.", "Frontend", "react/typescript");
        // when

        Recruit findRecruit = recruitRepository.findOne(recruitId);
        // then
        assertEquals(recruitId, findRecruit.getId(), "생성된 채용공고의 id를 통해 저장소에서 불러온 데이터와 id의 값이 같아야됩니다.");
    }

    @Test
    public void 채용공고_삭제() throws Exception {
        // given
        Company company = createCompany("company1", "korea", "seoul");
        Long recruitId = recruitService.post(company.getId(), 1000000L, "프론트엔드 개발자 구합니다.", "Frontend", "react/typescript");

        Recruit findRecruit = recruitService.findOne(recruitId);
        // when

        recruitService.delete(recruitId);

        // then

        assertEquals(RecruitStatus.INVALID, findRecruit.getStatus());
    }

    @Test
    public void 채용공고_등록() throws Exception {
        // given
        Company company = createCompany("company1", "korea", "seoul");
        Long recruitId = recruitService.post(company.getId(), 1000000L, "프론트엔드 개발자 구합니다.", "Frontend", "react/typescript");
        Recruit findRecruit = recruitService.findOne(recruitId);

        Users user = createUser("user1");
        // when

        recruitService.apply(recruitId, user.getId());
        // then

        assertTrue(findRecruit.getUsers().contains(user));
    }

    public Company createCompany(String name, String country, String region) {
        Company company = new Company();
        company.setName(name);
        company.setCountry(country);
        company.setRegion(region);

        companyService.join(company);
        return company;
    }

    public Users createUser(String name) {
        Users user = new Users();
        user.setName(name);

        em.persist(user);
        return user;
    }
}