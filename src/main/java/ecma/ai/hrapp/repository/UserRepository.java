package ecma.ai.hrapp.repository;

import ecma.ai.hrapp.entity.Role;
import ecma.ai.hrapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Email;
import java.lang.annotation.Native;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String email);

    @Query(nativeQuery = true, value = "select * from users inner join users_roles on id=users_roles where roles_id=3")
    Optional<User> findByRolesLike();


    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(@Email String email, UUID id);
}
