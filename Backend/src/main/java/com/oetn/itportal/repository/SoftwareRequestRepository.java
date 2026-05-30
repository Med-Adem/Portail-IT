package com.oetn.itportal.repository;

import com.oetn.itportal.model.SoftwareRequest;
import com.oetn.itportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoftwareRequestRepository extends JpaRepository<SoftwareRequest, Long> {
    List<SoftwareRequest> findByUserOrderByCreatedAtDesc(User user);
    List<SoftwareRequest> findAllByOrderByCreatedAtDesc(); // pour les admins
}
