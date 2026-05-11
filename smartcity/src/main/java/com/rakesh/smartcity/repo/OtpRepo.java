package com.rakesh.smartcity.repo;

import com.rakesh.smartcity.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepo extends JpaRepository<Otp, Long> {

    Optional<Otp> findByEmail(String email);

    void deleteByEmail(String email);
}