package com.raze.springExam.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username, email, phone_number, password, role;
//    private int views;


    public User(String name, String email, String phone_number, String password, String role) {
        this.username = name;
        this.email = email;
        this.phone_number = phone_number;
        this.password = password;
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
