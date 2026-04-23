package com.rakesh.smartcity.repo;


import com.rakesh.smartcity.model.Complain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplainRepo extends JpaRepository<Complain,Long> {
    List<Complain> findByUserId(Long userId);
    List<Complain> findByAssignedWorkerId(Long workerId);
    List<Complain> findByAssignedAdminId(Long adminId);
    List<Complain> findByPinCodeAreaId(Long pinCodeAreaId);
}
