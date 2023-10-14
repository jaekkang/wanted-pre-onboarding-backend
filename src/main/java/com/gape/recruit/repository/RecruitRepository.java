package com.gape.recruit.repository;

import com.gape.recruit.domain.Recruit;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecruitRepository {

    private final EntityManager em;

    public void save(Recruit recruit) {
        em.persist(recruit);
    }

    public Recruit findOne(Long recruitId) {
        return em.find(Recruit.class, recruitId);
    }

    public List<Recruit> findAll() {
        return em.createQuery("select r from Recruit", Recruit.class).getResultList();
    }
}
