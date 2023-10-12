package com.gape.recruit.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
public class Recruit {
    @Id
    @GeneratedValue
    @Column(name = "recruit_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "recruit")
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
}
