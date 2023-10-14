package com.gape.recruit.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Company {
    @Id @GeneratedValue
    @Column(name = "company_id")
    private Long id;

    private String name;

    private String country;

    private String region;

    @OneToMany(mappedBy = "company")
    private List<Recruit> recruits = new ArrayList<>();


}
