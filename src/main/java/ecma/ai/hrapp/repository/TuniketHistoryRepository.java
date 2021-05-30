package ecma.ai.hrapp.repository;

import ecma.ai.hrapp.entity.TurniketHistory;
import ecma.ai.hrapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TuniketHistoryRepository extends JpaRepository<TurniketHistory, UUID> {
//    Optional<User> findByEmail(String email);


    Optional<TurniketHistory> findAllByTurniket_Owner_Username(String turniket_owner_username);
    Optional<TurniketHistory> findAllByTurniket_Owner_Id(UUID turniket_owner_id);
}
