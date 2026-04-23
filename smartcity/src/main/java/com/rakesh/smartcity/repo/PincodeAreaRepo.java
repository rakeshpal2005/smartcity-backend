package com.rakesh.smartcity.repo;

import com.rakesh.smartcity.model.PinCodeArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PincodeAreaRepo extends JpaRepository<PinCodeArea, Long> {

    Optional<PinCodeArea> findByPinCode(String pinCode);
    List<PinCodeArea> findByCity(String city);
}
