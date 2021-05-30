package ecma.ai.hrapp.component;

import ecma.ai.hrapp.entity.Company;
import ecma.ai.hrapp.entity.Role;
import ecma.ai.hrapp.entity.User;
import ecma.ai.hrapp.repository.CompanyRepository;
import ecma.ai.hrapp.repository.RoleRepository;
import ecma.ai.hrapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    @Value("${spring.datasource.initialization-mode}")
    private String initialMode;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CompanyRepository companyRepository;

    @Override
    public void run(String... args) throws Exception {
        if (initialMode.equals("always")) {
//            List<Role> all = roleRepository.findAll();

            Set<Role> roles = new HashSet<>(roleRepository.findAll()); //bazada necha xil rol bo'lsa berdm


            User user = new User("direktor", passwordEncoder.encode("123"),
                    "dr@mail.ru", roles, "direktor", true);
            User direktor = userRepository.save(user);

            Company company = new Company("PDP", direktor);
            companyRepository.save(company);
        }
    }
}
