package com.rakesh.smartcity.repo;


import com.rakesh.smartcity.model.Role;
import com.rakesh.smartcity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

    List<User> findByRoleAndAdminId(Role role, Long adminId);

    List<User> findByRoleAndPinCode(Role role, String pinCode);

    Optional<User> findByPhoneNumber(String phoneNumber);

     Optional<User> findByIdAndRole(Long id, Role role);
}
