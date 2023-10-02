package ru.smartjava.backend.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.smartjava.backend.model.EUser;

import java.util.Optional;

@Repository
public interface EUserRepository extends JpaRepository<EUser, Long> {

    Optional<EUser> findByLogin(String login);
    Optional<EUser> findByToken(String token);

    @Modifying
    @Transactional
    @Query("update EUser eu set eu.token=null where eu.id=:id")
    void clearTokenForUserById(@Param("id") Long id);
}
