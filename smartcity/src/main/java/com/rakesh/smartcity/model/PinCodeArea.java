package com.rakesh.smartcity.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="pincodeareas")
public class PinCodeArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private String state;
    private String pincode;
    private String areaname;

    @OneToMany(mappedBy = "pincodearea", cascade = CascadeType.ALL)
    private List<Complain> complains;

}
