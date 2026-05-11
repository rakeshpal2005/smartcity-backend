package com.rakesh.smartcity.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="feedbacks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private int rating ;


    private String comment ;

    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "complain_id", nullable = false)
    private Complain complain;

    @ManyToOne
    @JoinColumn(name = "given_by_user_id", nullable = false)
    private User givenBy;

    @ManyToOne
    @JoinColumn(name = "given_to_worker_id", nullable = false)
    private User givenTo;

}
