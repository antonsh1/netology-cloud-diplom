package ru.smartjava.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smartjava.backend.model.EUser;

import java.util.Optional;

@Repository
public interface EUserRepository extends JpaRepository<EUser, Long> {

    Optional<EUser> findByLogin(String login);

}
