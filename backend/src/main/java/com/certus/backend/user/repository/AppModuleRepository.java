package com.certus.backend.user.repository;

import com.certus.backend.user.domain.AppModule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AppModuleRepository extends JpaRepository<AppModule, Long> {
    Optional<AppModule> findByName(String name);
}
