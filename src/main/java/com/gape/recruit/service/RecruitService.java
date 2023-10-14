package com.gape.recruit.service;

import com.gape.recruit.domain.Company;
import com.gape.recruit.domain.Recruit;
import com.gape.recruit.repository.CompanyRepository;
import com.gape.recruit.repository.RecruitRepository;
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

    @Transactional
    public Long addRecruit(Long companyId) {
        Company company = companyRepository.findOne(companyId);
        Recruit recruit = Recruit.createRecruit(company);
        return recruit.getId();
    }

    public Recruit findOne(Long recruitId) {
        return recruitRepository.findOne(recruitId);
    }

    public List<Recruit> findAll() {
        return recruitRepository.findAll();
    }
}
