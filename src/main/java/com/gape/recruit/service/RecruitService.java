package com.gape.recruit.service;

import com.gape.recruit.domain.Recruit;
import com.gape.recruit.domain.Users;
import com.gape.recruit.dto.recruit.RecruitDto;
import com.gape.recruit.repository.CompanyRepository;
import com.gape.recruit.repository.RecruitRepository;
import com.gape.recruit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecruitService {
    @Autowired
    RecruitRepository recruitRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    UserRepository userRepository;

    @Transactional
    public Long post(Recruit recruit) {
        recruitRepository.save(recruit);
        return recruit.getId();
    }

    @Transactional
    public Long delete(Long recruitId) {
        Recruit recruit = recruitRepository.findOne(recruitId);
        recruitRepository.delete(recruit);
        return recruitId;
    }

    @Transactional
    public Long apply(Long recruitId, Users user) throws IllegalStateException {
        Recruit recruit = recruitRepository.findOne(recruitId);
        if (user.getRecruit() != null)
            throw new IllegalStateException("이미 지원한 공고가 있습니다.");
        recruit.setUser(user);
        return recruitId;
    }

    public Optional<List<Recruit>> findByKeyword(String keyword) {
        return recruitRepository.searchRecruit(keyword);
    }

    public Recruit findOne(Long recruitId) {
        return recruitRepository.findOne(recruitId);
    }

    @Transactional
    public void update(Long recruitId, RecruitDto.UpdateRecruitRequest request) {
        Recruit recruit = recruitRepository.findOne(recruitId);
        recruit.update(request);
    }

    public List<Recruit> findAll() {
        return recruitRepository.findAll();
    }
}
