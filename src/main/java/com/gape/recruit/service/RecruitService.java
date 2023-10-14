package com.gape.recruit.service;

import com.gape.recruit.domain.Company;
import com.gape.recruit.domain.Recruit;
import com.gape.recruit.domain.Users;
import com.gape.recruit.repository.CompanyRepository;
import com.gape.recruit.repository.RecruitRepository;
import com.gape.recruit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public Long post(Long companyId, Long reward, String content, String position, String techStack) {
        Company company = companyRepository.findOne(companyId);
        Recruit recruit = Recruit.createRecruit(company, position, reward, techStack, content);
        recruitRepository.save(recruit);
        return recruit.getId();
    }

    @Transactional
    public void delete(Long recruitId) {
        Recruit recruit = recruitRepository.findOne(recruitId);
        recruit.delete();
    }

    @Transactional
    public void apply(Long recruitId, Long userId) throws IllegalStateException {
        Users user = userRepository.findOne(userId);
        Recruit recruit = recruitRepository.findOne(recruitId);
        if (user.getRecruit() != null)
            throw new IllegalStateException("이미 지원한 공고가 있습니다.");
        recruit.setUser(user);
    }

    public Recruit findOne(Long recruitId) {
        return recruitRepository.findOne(recruitId);
    }

    public List<Recruit> findAll() {
        return recruitRepository.findAll();
    }
}
