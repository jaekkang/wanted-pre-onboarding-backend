package com.gape.recruit.repository;

import com.gape.recruit.domain.Company;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CompanyRepository {

    private final EntityManager em;

    public void save(Company company) {
        em.persist(company);
    }


    public Company findOne(Long companyId) {
        Company company = em.find(Company.class, companyId);
        if (company == null) {
            throw new IllegalArgumentException("해당 ID의 회사는 존재하지 않습니다.");
        }
        return company;
    }

    public List<Company> findAll() {
        return em.createQuery("select c from Company c", Company.class).getResultList();
    }

    public List<Company> findByName(String companyName) {
        return em.createQuery("select c from Company c where c.name = :name", Company.class)
                .setParameter("name", companyName)
                .getResultList();
    }
}
