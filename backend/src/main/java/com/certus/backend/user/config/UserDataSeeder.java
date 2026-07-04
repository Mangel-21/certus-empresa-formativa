package com.certus.backend.user.config;

import com.certus.backend.user.domain.*;
import com.certus.backend.user.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class UserDataSeeder {

    @Bean
    CommandLineRunner initDatabase(
            RoleRepository roleRepository,
            AppModuleRepository moduleRepository,
            ActionRepository actionRepository,
            PermissionRepository permissionRepository,
            UserRepository userRepository) {
        
        return args -> {
            // Seed Modules
            AppModule feedModule = createModuleIfNotFound(moduleRepository, "FEED", "Muro de Noticias");
            AppModule contestModule = createModuleIfNotFound(moduleRepository, "CONTESTS", "Módulo de Concursos");
            
            // Seed Actions
            Action readAction = createActionIfNotFound(actionRepository, "READ", "Leer recursos");
            Action createAction = createActionIfNotFound(actionRepository, "CREATE", "Crear recursos");
            
            // Seed Roles
            Role studentRole = createRoleIfNotFound(roleRepository, "ROLE_STUDENT", "Estudiante de CERTUS");
            
            // Seed Permissions (Student can read/create on FEED)
            if (permissionRepository.count() == 0) {
                Permission p1 = new Permission();
                p1.setRole(studentRole);
                p1.setModule(feedModule);
                p1.setAction(readAction);
                permissionRepository.save(p1);
                
                Permission p2 = new Permission();
                p2.setRole(studentRole);
                p2.setModule(feedModule);
                p2.setAction(createAction);
                permissionRepository.save(p2);
            }
            
            // Seed Default User (Optional)
            if (userRepository.findByEmail("admin@certus.edu.pe").isEmpty()) {
                User admin = new User();
                admin.setEmail("admin@certus.edu.pe");
                admin.setFirstName("Admin");
                admin.setLastName("Certus");
                admin.getRoles().add(studentRole);
                userRepository.save(admin);
            }
        };
    }

    private AppModule createModuleIfNotFound(AppModuleRepository repo, String name, String desc) {
        return repo.findByName(name).orElseGet(() -> {
            AppModule m = new AppModule();
            m.setName(name);
            m.setDescription(desc);
            return repo.save(m);
        });
    }

    private Action createActionIfNotFound(ActionRepository repo, String name, String desc) {
        return repo.findByName(name).orElseGet(() -> {
            Action a = new Action();
            a.setName(name);
            a.setDescription(desc);
            return repo.save(a);
        });
    }

    private Role createRoleIfNotFound(RoleRepository repo, String name, String desc) {
        return repo.findByName(name).orElseGet(() -> {
            Role r = new Role();
            r.setName(name);
            r.setDescription(desc);
            return repo.save(r);
        });
    }
}
