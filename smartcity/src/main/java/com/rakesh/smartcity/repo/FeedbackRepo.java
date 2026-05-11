package com.rakesh.smartcity.repo;


import com.rakesh.smartcity.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  FeedbackRepo extends JpaRepository<Feedback, Long> {
    List<Feedback> findByGivenToId(Long workerId);

    Optional<Feedback> findByComplainId(Long complaintId);

}
