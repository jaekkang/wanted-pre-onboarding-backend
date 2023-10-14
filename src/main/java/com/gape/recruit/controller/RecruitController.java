package com.gape.recruit.controller;

import com.gape.recruit.domain.Company;
import com.gape.recruit.domain.Recruit;
import com.gape.recruit.domain.Users;
import com.gape.recruit.service.CompanyService;
import com.gape.recruit.service.RecruitService;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecruitController {
    private final RecruitService recruitService;
    private final CompanyService companyService;

    @PostMapping("/api/recruits")
    public CreateRecruitResponse post(@RequestBody @Valid CreateRecruitRequest request) {
        Company company = companyService.findOne(request.companyId);
        Recruit recruit = Recruit.createRecruit(company, request.position, request.reward, request.techStack, request.content);
        Long recruitId = recruitService.post(recruit);

        return new CreateRecruitResponse(recruitId);
    }

    @Data
    static class CreateRecruitRequest {
        @NotEmpty
        private Long companyId;

        @NotEmpty
        private String position;

        @NotEmpty
        private Long reward;

        @NotEmpty
        private String techStack;

        @NotEmpty
        private String content;
    }

    @Data
    static class CreateRecruitResponse {

        private Long id;

        public CreateRecruitResponse(Long id) {
            this.id = id;
        }
    }
}
