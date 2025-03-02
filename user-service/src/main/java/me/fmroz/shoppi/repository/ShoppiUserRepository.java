package me.fmroz.shoppi.repository;

import me.fmroz.shoppi.model.ShoppiUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppiUserRepository extends JpaRepository<ShoppiUser, Long> {
    Optional<ShoppiUser> findByEmail(String email);

    Optional<ShoppiUser> findByRefreshToken(String refreshToken);
}
