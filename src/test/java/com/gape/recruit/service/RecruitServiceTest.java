package com.gape.recruit.service;

import com.gape.recruit.domain.Company;
import com.gape.recruit.domain.Recruit;
import com.gape.recruit.domain.RecruitStatus;
import com.gape.recruit.domain.Users;
import com.gape.recruit.dto.recruit.RecruitDto;
import com.gape.recruit.repository.RecruitRepository;
import jakarta.persistence.EntityManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
public class RecruitServiceTest {
    @Autowired RecruitService recruitService;
    @Autowired RecruitRepository recruitRepository;
    @Autowired CompanyService companyService;
    @Autowired UserService userService;

    @Autowired
    EntityManager em;

    @Test
    public void 채용공고_조회() throws Exception {
        // given
        List<Recruit> recruits = recruitService.findAll();

        // when & then
        assertEquals(5, recruits.size());
    }

    @Test
    public void 채용공고_검색() throws Exception {
        // given
        Optional<List<Recruit>> recruits = recruitService.findByKeyword("backend");
        // when // then

        assertTrue(recruits.isPresent());
        assertEquals(3, recruits.get().size());
    }

    @Test
    public void 채용공고_추가() throws Exception {
        // given
        Company company = createCompany("company3", "korea", "seoul");
        Recruit recruit = Recruit.createRecruit(company, "Frontend",1000000L,"react/typescript", "프론트엔드 개발자 구합니다." );
        Long recruitId = recruitService.post(recruit);
        // when

        Recruit findRecruit = recruitRepository.findOne(recruitId);
        // then
        assertEquals(recruitId, findRecruit.getId(), "생성된 채용공고의 id를 통해 저장소에서 불러온 데이터와 id의 값이 같아야됩니다.");
    }

    @Test
    public void 채용공고_삭제() throws Exception {
        // given
        Company company = createCompany("company3", "korea", "seoul");
        Recruit recruit = Recruit.createRecruit(company, "Frontend",1000000L,"react/typescript", "프론트엔드 개발자 구합니다." );
        Long recruitId = recruitService.post(recruit);

        Recruit findRecruit = recruitService.findOne(recruitId);
        // when

        recruitService.delete(findRecruit.getId());

        // then
        assertThrows(InvalidDataAccessApiUsageException.class, () -> recruitService.findOne(recruitId), "이미 삭제된 ID로 조회시 에러가 나야됩니다.");
    }

    @Test
    public void 채용공고_등록() throws Exception {
        // given
        Company company = createCompany("company3", "korea", "seoul");
        Recruit recruit = Recruit.createRecruit(company, "Frontend",1000000L,"react/typescript", "프론트엔드 개발자 구합니다." );
        Long recruitId = recruitService.post(recruit);
        Recruit findRecruit = recruitService.findOne(recruitId);

        Users user = createUser("user1");
        // when

        recruitService.apply(recruitId, user);
        // then

        assertTrue(findRecruit.getUsers().contains(user));
    }

    @Test
    public void 채용공고_등록_중복사용자() throws Exception {
        Company company = createCompany("company3", "korea", "seoul");
        Recruit recruit = Recruit.createRecruit(company, "Frontend",1000000L,"react/typescript", "프론트엔드 개발자 구합니다." );
        Long recruitId = recruitService.post(recruit);
        Recruit findRecruit = recruitService.findOne(recruitId);

        Users user = createUser("user1");
        // when

        recruitService.apply(recruitId, user);
        // then

        assertThrows(IllegalStateException.class, () -> recruitService.apply(recruitId, user));
    }

    @Test
    public void 채용공고_삭제_유저등록된() throws Exception {
        //given
        Company company = createCompany("company3", "korea", "seoul");
        Recruit recruit = Recruit.createRecruit(company, "Frontend",1000000L,"react/typescript", "프론트엔드 개발자 구합니다." );
        Users user = createUser("UserC");
        Long recruitId = recruitService.post(recruit);
        Recruit findRecruit = recruitService.findOne(recruitId);
        //when

        recruitService.apply(recruitId, user);
        recruitService.delete(findRecruit.getId());
        //then
        assertThrows(InvalidDataAccessApiUsageException.class, () -> recruitService.findOne(recruitId), "이미 삭제된 ID로 조회시 에러가 나야됩니다.");
    }

    @Test
    public void 채용공고_수정() {
        //given
        Company company = createCompany("company3", "korea", "seoul");
        Recruit recruit = Recruit.createRecruit(company, "Frontend",1000000L,"react/typescript", "프론트엔드 개발자 구합니다." );
        recruitService.post(recruit);
        RecruitDto.UpdateRecruitRequest request = new RecruitDto.UpdateRecruitRequest();

        request.setReward(100L);
        request.setContent("수정된 데이터입니다.");
        request.setPosition("수정된 포지션입니다.");
        request.setTechStack("수정된 기술입니다.");
        //when

        recruitService.update(recruit.getId(), request);

        //then
        assertEquals(recruit.getReward(), 100L);
        assertEquals(recruit.getContent(), "수정된 데이터입니다.");
        assertEquals(recruit.getPosition(), "수정된 포지션입니다.");
        assertEquals(recruit.getTechStack(), "수정된 기술입니다.");
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