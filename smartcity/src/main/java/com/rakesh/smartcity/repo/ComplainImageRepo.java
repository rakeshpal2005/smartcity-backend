package com.rakesh.smartcity.repo;


import com.rakesh.smartcity.model.ComplainImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplainImageRepo extends JpaRepository<ComplainImage, Long> {
    List<ComplainImage> findByComplainId(Long Complainid);
}
