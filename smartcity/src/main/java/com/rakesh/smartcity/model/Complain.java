package com.rakesh.smartcity.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "complains")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Complain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplainStatus status;

    @Column(nullable = false)
    private String exactAddress;

    private String landmark;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime resolvedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "assigned_admin_id")
    private User assignedAdmin;

    @ManyToOne
    @JoinColumn(name = "assigned_worker_id")
    private User assignedWorker;

    @ManyToOne
    @JoinColumn(name = "pin_code_area_id", nullable = false)
    private PinCodeArea pinCodeArea;

    @OneToMany(mappedBy = "complain", cascade = CascadeType.ALL)
    private List<ComplainImage> complainImages;
}