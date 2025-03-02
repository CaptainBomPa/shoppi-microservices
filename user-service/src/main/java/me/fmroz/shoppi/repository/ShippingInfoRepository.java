package me.fmroz.shoppi.repository;

import me.fmroz.shoppi.model.ShippingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShippingInfoRepository extends JpaRepository<ShippingInfo, Long> {
    List<ShippingInfo> findByUserId(Long userId);
}
