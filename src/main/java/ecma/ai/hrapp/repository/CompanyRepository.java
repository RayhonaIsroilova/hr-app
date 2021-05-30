package ecma.ai.hrapp.repository;

import ecma.ai.hrapp.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
//    Optional<User> findByEmail(String email);
}
