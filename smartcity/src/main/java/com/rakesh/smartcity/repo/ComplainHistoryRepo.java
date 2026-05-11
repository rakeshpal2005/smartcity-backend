package com.rakesh.smartcity.repo;

import com.rakesh.smartcity.model.ComplainHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplainHistoryRepo extends JpaRepository<ComplainHistory, Long> {

  List<ComplainHistory> findByComplaintId(Long complainId);

}
