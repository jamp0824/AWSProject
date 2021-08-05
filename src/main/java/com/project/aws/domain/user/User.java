package com.project.aws.domain.user;

import com.project.aws.domain.BaseTimeEntity;
import lombok.Builder;

import javax.persistence.*;

public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String name, String email, String picture, Role role){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User update(String name,String email){
        this.name = name;
        this.email = email;

        return this;
    }
    public String getRoleKey(){
        return this.role.getKey();
    }
}