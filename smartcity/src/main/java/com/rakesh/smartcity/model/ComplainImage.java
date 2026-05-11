package com.rakesh.smartcity.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "complain_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplainImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImageType imageType;

    @ManyToOne
    @JoinColumn(name = "complain_id", nullable = false)
    private Complain complain;
}