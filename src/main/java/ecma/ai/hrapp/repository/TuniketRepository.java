package ecma.ai.hrapp.repository;

import ecma.ai.hrapp.entity.Turniket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TuniketRepository extends JpaRepository<Turniket, UUID> {
    Object findAllByOwner_Username(String usernameFromToken);

    Object findAllByOwner_UsernameAndId(String owner_username, UUID id);

//    Optional<User> findByEmail(String email);
}
