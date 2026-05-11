package com.rakesh.smartcity.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

    @Column(nullable = false)
private String name;

    @Column(nullable = false, unique = true)
private String email;

    @Column(nullable = false, unique = true)
private String phoneNumber;

@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
private List<Complain> complains;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String pinCode;
    @ManyToOne
    @JoinColumn(name = "admin_id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User admin;

    @OneToMany(mappedBy = "admin")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<User> workers;
}
