package com.workOUTcoach.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @NotBlank
    @Size(max = 100)
    @Email
    @Column
    private String email;

    @NotBlank
    @Size(max = 100)
    @Column
    private String password;

    @NotBlank
    @Size(max = 50)
    @Column
    private String name;

    @NotBlank
    @Size(max = 50)
    @Column
    private String surname;

    @NotBlank
    @Size(max = 255)
    @Column
    private String securityQuestion;

    @NotBlank
    @Size(max = 30)
    @Column
    private String securityAnswer;

    @Column
    private String resetToken;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Authority> authorities = new HashSet<>();

    public User() {
    }

    public User(String email, String password, String name, String surname, String securityQuestion, String securityAnswer) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.resetToken=null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public String getResetToken(){ return resetToken; }

    public void setResetToken(String resetToken){ this.resetToken=resetToken; }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}
