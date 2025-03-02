package me.fmroz.shoppi.repository;

import me.fmroz.shoppi.model.CompanyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, Long> {
    Optional<CompanyInfo> findByUserId(Long userId);
}
