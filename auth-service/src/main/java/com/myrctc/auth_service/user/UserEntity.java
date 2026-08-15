package com.myrctc.auth_service.user;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class UserEntity {
    @Id
    Email email;
    String password;
    String username;
    Integer age;
}
