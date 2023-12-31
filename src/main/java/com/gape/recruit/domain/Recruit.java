package com.gape.recruit.domain;

import com.gape.recruit.dto.recruit.RecruitDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Recruit {
    @Id
    @GeneratedValue
    @Column(name = "recruit_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "recruit", cascade = CascadeType.ALL)
    private List<Users> users = new ArrayList<>();

    private String position;

    private Long reward;

    private String techStack;

    private String content;

    //== 연관관계 메서드 ==//

    public void setCompany(Company company) {
        this.company = company;
        company.getRecruits().add(this);
    }

    public void setUser(Users user) {
        users.add(user);
        user.setRecruit(this);
    }

    //== 생성 메서드 ==//

    public static Recruit createRecruit(Company company, String position, Long reward, String techStack, String content) {
        Recruit recruit = new Recruit();
        recruit.setCompany(company);
        recruit.setPosition(position);
        recruit.setReward(reward);
        recruit.setTechStack(techStack);
        recruit.setContent(content);
        return recruit;
    }

    public void update(RecruitDto.UpdateRecruitRequest request) {
        if(request.getPosition() != null) this.position = request.getPosition();
        if(request.getContent() != null) this.content = request.getContent();
        if(request.getReward() != null) this.reward = request.getReward();
        if(request.getTechStack() != null) this.techStack = request.getTechStack();
    }
}
