package com.company.repository;

import com.company.entity.ConfirmationTokenEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationTokenEntity, Integer> {

    Optional<ConfirmationTokenEntity> findByToken(String token);

    @Transactional
    @Modifying
    @Query("update ConfirmationTokenEntity c set c.confirmedAt = ?2 where c.token=?1")
    int updateConfirmedAt(String token, LocalDateTime now);

    @Query("select c from ConfirmationTokenEntity c where c.confirmedAt is null and c.expiresAt < ?1")
    List<ConfirmationTokenEntity> getExpiredAndUnconfirmedTokens(LocalDateTime now);
}
