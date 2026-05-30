package com.oetn.itportal.repository;

import com.oetn.itportal.model.HardwareDevice;
import com.oetn.itportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HardwareDeviceRepository extends JpaRepository<HardwareDevice, Long> {
    List<HardwareDevice> findByUserOrderBySubmittedAtDesc(User user);
    List<HardwareDevice> findAllByOrderBySubmittedAtDesc(); // pour les admins
    boolean existsBySerialNumber(String serialNumber);
}
