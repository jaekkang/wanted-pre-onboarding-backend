package com.gape.recruit.dto.recruit;
import com.gape.recruit.domain.Company;
import com.gape.recruit.domain.Recruit;
import com.gape.recruit.domain.Users;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class RecruitDto {
    private Long id;

    private String companyName;

    private String country;

    private String region;

    private String position;

    private Long reward;

    private String techStack;

    public RecruitDto(Recruit recruit) {
        Company company = recruit.getCompany();
        this.id = recruit.getId();
        this.companyName = company.getName();
        this.country = company.getCountry();
        this.region = company.getRegion();
        this.position = recruit.getPosition();
        this.reward = recruit.getReward();
        this.techStack = recruit.getTechStack();
    }

    @Data
    static public class FindRecruitDetailResponse {
        private Long id;

        private String companyName;

        private String country;

        private String region;

        private String position;

        private Long reward;

        private String techStack;

        private String content;

        private List<Long> recruitList;


        public FindRecruitDetailResponse(Recruit recruit) {
            Company company = recruit.getCompany();
            this.id = recruit.getId();
            this.companyName = company.getName();
            this.country = company.getCountry();
            this.region = company.getRegion();
            this.position = recruit.getPosition();
            this.reward = recruit.getReward();
            this.techStack = recruit.getTechStack();
            this.content = recruit.getContent();
            this.recruitList = company.getRecruits().stream().map(Recruit::getId).collect(Collectors.toList());
        }
    }

    @Data
    static public class CreateRecruitRequest {
        @NotNull
        private Long companyId;

        @NotEmpty
        private String position;

        @NotNull
        private Long reward;

        @NotEmpty
        private String techStack;

        @NotEmpty
        private String content;
    }

    @Data
    static public class UpdateRecruitRequest {

        private String position;

        private Long reward;

        private String techStack;

        private String content;
    }

    @Data
    static public class UpdateRecruitResponse {

        private String position;

        private Long reward;

        private String techStack;

        private String content;

        public UpdateRecruitResponse(Recruit recruit) {
            this.position = recruit.getPosition();
            this.reward = recruit.getReward();
            this.techStack = recruit.getTechStack();
            this.content = recruit.getContent();
        }
    }

    @Data
    static public class ApplyRecruitRequest {
        @NotNull
        private Long userId;
    }

    @Data
    static public class ApplyRecruitResponse {
        private Long id;
        private Long userId;
        private String userName;

        public ApplyRecruitResponse(Users user, Long recruitId) {
            this.id = recruitId;
            this.userId = user.getId();
            this.userName = user.getName();
        }
    }

    @Data
    static public  class CreateRecruitResponse {

        private Long id;

        public CreateRecruitResponse(Long id) {
            this.id = id;
        }
    }
}
