package ecma.ai.hrapp.repository;

import ecma.ai.hrapp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
//    Optional<User> findByEmail(String email);
}
