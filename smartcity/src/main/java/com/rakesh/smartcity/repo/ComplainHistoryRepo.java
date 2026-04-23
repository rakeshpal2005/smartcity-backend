package com.rakesh.smartcity.repo;

import com.rakesh.smartcity.model.Complainhistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComplainHistoryRepo extends JpaRepository<Complainhistory, Long> {

  List<Complainhistory> findByComplainId(Long complainId);

}
