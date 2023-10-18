package com.gape.recruit.controller;

import com.gape.recruit.domain.Company;
import com.gape.recruit.service.CompanyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/api/companies")
    public CreateCompanyResponse post(@RequestBody @Valid CreateCompanyRequest request) {
        Company company = new Company();
        company.setName(request.name);
        company.setCountry(request.country);
        company.setRegion(request.region);
        Long companyId = companyService.join(company);

        return new CreateCompanyResponse(companyId);
    }

    @GetMapping("/api/companies")
    public Result finfAllCompanies() {
        List<Company> findCompanies = companyService.findAll();
        List<CompanyDto> collect = findCompanies.stream()
                .map(m -> new CompanyDto(m.getId() ,m.getName(), m.getCountry(), m.getRegion()))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    @Data
    static class CreateCompanyRequest {
        @NotEmpty
        private String name;

        @NotEmpty
        private String country;

        @NotEmpty
        private String region;
    }

    @Data
    static class CreateCompanyResponse {
        private Long id;

        public CreateCompanyResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    @AllArgsConstructor
    static class CompanyDto {
        private Long id;

        private String name;

        private String country;

        private String region;
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
}
