package com.gape.recruit;

import com.gape.recruit.domain.Company;
import com.gape.recruit.domain.Recruit;
import com.gape.recruit.domain.Users;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }
    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        public void dbInit() {
            Users user1 = new Users();
            user1.setName("userA");
            em.persist(user1);

            Users user2 = new Users();
            user2.setName("userB");
            em.persist(user2);

            Company company = new Company();
            company.setName("company1");
            company.setCountry("korea");
            company.setRegion("seoul");
            em.persist(company);

            Company company2 = new Company();
            company2.setName("company2");
            company2.setCountry("korea");
            company2.setRegion("busan");
            em.persist(company2);

            Recruit recruit = Recruit.createRecruit(company, "backend", 1000000L, "java/spring", "백엔드 개발자 구합니다.");
            em.persist(recruit);
            Recruit recruit1 = Recruit.createRecruit(company2, "frontend", 1000000L, "flutter/dart", "프론트 개발자 구합니다.");
            em.persist(recruit1);
            Recruit recruit2 = Recruit.createRecruit(company, "backend", 1000000L, "java/spring", "백엔드 개발자 구합니다.");
            em.persist(recruit2);
            Recruit recruit3 = Recruit.createRecruit(company2, "frontend", 1000000L, "flutter dart", "프론트 개발자 구합니다.");
            em.persist(recruit3);
            Recruit recruit4 = Recruit.createRecruit(company, "backend", 1000000L, "java/spring", "백엔드 개발자 구합니다.");
            em.persist(recruit4);

        }
    }
}
