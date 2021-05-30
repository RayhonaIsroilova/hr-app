package ecma.ai.hrapp.service;

import ecma.ai.hrapp.entity.PaidSalary;
import ecma.ai.hrapp.entity.Role;
import ecma.ai.hrapp.entity.User;
import ecma.ai.hrapp.entity.enums.Month;
import ecma.ai.hrapp.entity.enums.RoleName;
import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.PaidDto;
import ecma.ai.hrapp.repository.PaidSalaryRepository;
import ecma.ai.hrapp.repository.UserRepository;
import ecma.ai.hrapp.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaidServise {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    PaidSalaryRepository paidSalaryRepository;

    public ApiResponse addSalary(HttpServletRequest httpServletRequest, PaidDto paidDto) {
        String authorization = httpServletRequest.getHeader("Authorization");
        authorization = authorization.substring(7);
        String usernameFromToken = jwtProvider.getUsernameFromToken(authorization);
        Optional<User> byUsername = userRepository.findByUsername(usernameFromToken);

        if (!byUsername.isPresent() && byUsername.get().getRoles().contains(new Role(3, RoleName.ROLE_STAFF))) {
            return new ApiResponse("huquqing yo'q", false);
        }
        Optional<User> byId = userRepository.findById(paidDto.getUserId());
        if (!byId.isPresent()) {
            return new ApiResponse("bunday user yo'q", false);
        }

        PaidSalary paidSalary = new PaidSalary();
        paidSalary.setOwner(byId.get());
        paidSalary.setPaid(false);
        paidSalary.setAmount(paidDto.getAmount());
        paidSalary.setPeriod(paidDto.getPeriod());
        if (paidSalaryRepository.existsByOwner_IdAndPeriod(byId.get().getId(), paidDto.getPeriod())) {
            return new ApiResponse("qoyil 2 marta pul to'lading", false);
        }

        paidSalaryRepository.save(paidSalary);
        return new ApiResponse("maosh tasdiqlandi, hali to'lanmandi", true, paidSalary);

    }

    public ApiResponse Update(HttpServletRequest httpServletRequest, PaidDto paidDto, UUID uuid) {
        String authorization = httpServletRequest.getHeader("Authorization");
        authorization = authorization.substring(7);
        String usernameFromToken = jwtProvider.getUsernameFromToken(authorization);
        Optional<User> byUsername = userRepository.findByUsername(usernameFromToken);
        Optional<PaidSalary> salaryByID = paidSalaryRepository.findById(uuid);
        if (!salaryByID.isPresent()) {
            return new ApiResponse("bunday maosh yo'q o'gartiradigam", false);
        }
        if (!byUsername.isPresent() && byUsername.get().getRoles().contains(new Role(3, RoleName.ROLE_STAFF))) {
            return new ApiResponse("huquqing yo'q", false);
        }
        Optional<User> byId = userRepository.findById(paidDto.getUserId());
        if (!byId.isPresent()) {
            return new ApiResponse("bunday user yo'q", false);
        }

        PaidSalary paidSalary = salaryByID.get();
        paidSalary.setOwner(byId.get());
        paidSalary.setPaid(paidDto.isPaid());
        paidSalary.setAmount(paidDto.getAmount());
        paidSalary.setPeriod(paidDto.getPeriod());
        if (paidSalaryRepository.existsByOwner_IdAndPeriod(byId.get().getId(), paidDto.getPeriod())) {
            return new ApiResponse("qoyil 2 marta pul to'lading", false);
        }

        paidSalaryRepository.save(paidSalary);
        return new ApiResponse("maosh tasdiqlandi, hali to'lanmandi", true, paidSalary);

    }

    public ApiResponse getByOwner(HttpServletRequest httpServletRequest, UUID OwnerId) {
        String authorization = httpServletRequest.getHeader("Authorization");
        authorization = authorization.substring(7);
        String usernameFromToken = jwtProvider.getUsernameFromToken(authorization);
        Optional<User> byUsername = userRepository.findByUsername(usernameFromToken);
        Optional<User> byId = userRepository.findById(OwnerId);
        if (!byId.isPresent()) return new ApiResponse("bunday user yo'q", false);

        if (!byUsername.isPresent() && byUsername.get().getRoles().contains(new Role(3, RoleName.ROLE_STAFF))) {
            return new ApiResponse("huquqing yo'q", false);
        }

        return new ApiResponse("maosh tasdiqlandi, hali to'lanmandi", true,
                paidSalaryRepository.findAllByOwner(byId.get()));

    }

    public ApiResponse getByMonth(HttpServletRequest httpServletRequest, Month month) {
        String authorization = httpServletRequest.getHeader("Authorization");
        authorization = authorization.substring(7);
        String usernameFromToken = jwtProvider.getUsernameFromToken(authorization);
        Optional<User> byUsername = userRepository.findByUsername(usernameFromToken);
        Optional<PaidSalary> allByPeriod = paidSalaryRepository.findAllByPeriod(month);
        if (!allByPeriod.isPresent()) {
            return new ApiResponse("bu oyda maosh belgilanmagan", false);
        }
        if (!byUsername.isPresent() && byUsername.get().getRoles().contains(new Role(3, RoleName.ROLE_STAFF))) {
            return new ApiResponse("huquqing yo'q", false);
        }

        return new ApiResponse("maosh tasdiqlandi, hali to'lanmandi", true, allByPeriod.get());

    }


}
