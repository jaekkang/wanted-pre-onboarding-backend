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
        Users user = em.find(Users.class, userId);
        if (user == null) {
            throw new IllegalArgumentException("해당 ID의 사용자는 존재하지 않습니다.");
        }
        return user;
    }

    public List<Users> findAll() {
        return em.createQuery("select u from Users u", Users.class).getResultList();
    }
}
