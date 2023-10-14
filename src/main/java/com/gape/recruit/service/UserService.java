package com.gape.recruit.service;

import com.gape.recruit.domain.Users;
import com.gape.recruit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Transactional
    public Long join(Users user) {
        userRepository.save(user);
        return user.getId();
    }

    public Users findOne(Long userId) {
        return userRepository.findOne(userId);
    }

    public List<Users> findAll() {
        return userRepository.findAll();
    }
}
