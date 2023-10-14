package com.gape.recruit.repository;

import com.gape.recruit.domain.Users;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final EntityManager em;

    public void save(Users user) {
        em.persist(user);
    }

    public Users findOne(Long userId) {
        return em.find(Users.class, userId);
    }

    public List<Users> findAll() {
        return em.createQuery("select u from Users u", Users.class).getResultList();
    }
}
