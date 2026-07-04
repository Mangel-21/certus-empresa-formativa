package com.certus.backend.user.repository;

import com.certus.backend.user.domain.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ActionRepository extends JpaRepository<Action, Long> {
    Optional<Action> findByName(String name);
}
