package com.gape.recruit.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Users {
    @Id
    @GeneratedValue
    @Column(name = "users_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "recruit_id")
    private Recruit recruit;

    private String name;
}
