package com.gape.recruit.service;

import com.gape.recruit.domain.Company;
import com.gape.recruit.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;

    @Transactional
    public Long join(Company company) {
        validatedDuplicateCompany(company);
        companyRepository.save(company);
        return company.getId();
    }

    private void validatedDuplicateCompany(Company company) {
        List<Company> findCompanies = companyRepository.findByName(company.getName());
        if (!findCompanies.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회사입니다.");
        }
    }

    public Company findOne(Long companyId) {
        return companyRepository.findOne(companyId);
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }
}
