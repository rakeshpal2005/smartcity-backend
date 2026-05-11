package com.rakesh.smartcity.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="complainhistories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplainHistory {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplainStatus complainStatus;


    @Column(nullable = false)
    private  String note;

    @Column(nullable = false)
    private LocalDateTime changedAt;

    @ManyToOne
    @JoinColumn(name = "complaint_id", nullable = false)
    private Complain complaint;

    @ManyToOne
    @JoinColumn(name = "changed_by_user_id", nullable = false)
    private User changedBy;


}
