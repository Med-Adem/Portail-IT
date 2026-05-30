package com.oetn.itportal.repository;

import com.oetn.itportal.model.SupportTicket;
import com.oetn.itportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByUserOrderByCreatedAtDesc(User user);
    List<SupportTicket> findByUserAndStatusOrderByCreatedAtDesc(User user, SupportTicket.Status status);
    List<SupportTicket> findAllByOrderByCreatedAtDesc(); // pour les admins
}
