package com.boot.backend.Sweet.Shop.Management.System.repository;

import com.boot.backend.Sweet.Shop.Management.System.entity.RefreshToken;
import com.boot.backend.Sweet.Shop.Management.System.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM refresh_token WHERE user_id = :userId", nativeQuery = true)
    void deleteByUserIdNative(Long userId);
}
