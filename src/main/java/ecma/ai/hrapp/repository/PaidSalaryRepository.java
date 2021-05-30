package ecma.ai.hrapp.repository;

import ecma.ai.hrapp.entity.PaidSalary;
import ecma.ai.hrapp.entity.User;
import ecma.ai.hrapp.entity.enums.Month;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaidSalaryRepository extends JpaRepository<PaidSalary, UUID> {
    boolean existsByOwner_IdAndPeriod(UUID owner_id, Month period);

    List<PaidSalary> findAllByOwner(User owner);
    Optional<PaidSalary> findAllByPeriod(Month period);

}
