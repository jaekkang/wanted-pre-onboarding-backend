package com.gape.recruit.repository;

import com.gape.recruit.domain.Recruit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RecruitRepository {

    private final EntityManager em;

    public void save(Recruit recruit) {
        em.persist(recruit);
    }

    public Recruit findOne(Long recruitId) {
        Recruit recruit = em.find(Recruit.class, recruitId);
        if (recruit == null)
            throw new IllegalArgumentException("해당 ID는 존재하지 않습니다.");
        return recruit;
    }

    public void delete(Recruit recruit) {
        em.remove(recruit);
    }

    public List<Recruit> findAll() {
        return em.createQuery("select r from Recruit r", Recruit.class).getResultList();
    }

    public Optional<List<Recruit>> searchRecruit(String keyword) {
        String jpqlQuery = "select r from Recruit r " +
                "where r.position like :keyword " +
                "or r.techStack like :keyword " +
                "or r.company.name like :keyword " +
                "or r.company.country like :keyword " +
                "or r.company.region like :keyword";

        TypedQuery<Recruit> query = em.createQuery(jpqlQuery, Recruit.class);
        query.setParameter("keyword", "%" + keyword + "%");

        List<Recruit> results = query.getResultList();

        return Optional.ofNullable(results);
    }
}
