package ecma.ai.hrapp.service;

import ecma.ai.hrapp.entity.Role;
import ecma.ai.hrapp.entity.Turniket;
import ecma.ai.hrapp.entity.TurniketHistory;
import ecma.ai.hrapp.entity.User;
import ecma.ai.hrapp.entity.enums.RoleName;
import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.TurniketDto;
import ecma.ai.hrapp.payload.TurniketHistoryDto;
import ecma.ai.hrapp.repository.CompanyRepository;
import ecma.ai.hrapp.repository.TuniketHistoryRepository;
import ecma.ai.hrapp.repository.TuniketRepository;
import ecma.ai.hrapp.repository.UserRepository;
import ecma.ai.hrapp.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@Service
public class TurrniketService {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    TuniketRepository tuniketRepository;
    @Autowired
    TuniketHistoryRepository tuniketHistoryRepository;

    //Faqat manejer add qila oladi turniketniiiii
    public ApiResponse addTurniket(HttpServletRequest httpServletRequest, TurniketDto turniketDTo) {
        String authorization = httpServletRequest.getHeader("Authorization");
        authorization = authorization.substring(7);
        String usernameFromToken = jwtProvider.getUsernameFromToken(authorization);
        Optional<User> byUsername = userRepository.findByUsername(usernameFromToken);

        if (!byUsername.isPresent() && !byUsername.get().getRoles().contains(RoleName.ROLE_MANAGER.name()))
            return new ApiResponse("siz kimsiz?", false);

        if (!companyRepository.findById(turniketDTo.getCompanyId()).isPresent()) {
            return new ApiResponse("ERROR", false);
        }
        if (!userRepository.findById(turniketDTo.getOwnerId()).isPresent()) {
            return new ApiResponse("ERROR", false);
        }

        Turniket turniket = new Turniket();
        turniket.setCompany(companyRepository.getOne(turniketDTo.getCompanyId()));
        turniket.setOwner(userRepository.getOne(turniketDTo.getOwnerId()));
        Turniket save = tuniketRepository.save(turniket);

        return new ApiResponse("success", true, save);
    }


    public ApiResponse addTurniketHistory(HttpServletRequest request, TurniketHistoryDto turniketHistoryDto) {
        String authorization = request.getHeader("Authorization");
        authorization = authorization.substring(7);
        String usernameFromToken = jwtProvider.getUsernameFromToken(authorization);
        Optional<User> byUsername = userRepository.findByUsername(usernameFromToken);

        if (!byUsername.isPresent() &&
                !(byUsername.get().getRoles().contains(RoleName.ROLE_MANAGER.name())
                        || byUsername.get().getRoles().contains(RoleName.ROLE_DIRECTOR.name())))
            return new ApiResponse("siz kimsiz?", false);


        if (!tuniketRepository.findById(turniketHistoryDto.getTurniketId()).isPresent()) {
            return new ApiResponse("ERROR", false);
        }

        TurniketHistory turniketHistory = new TurniketHistory();
        turniketHistory.setTurniket(tuniketRepository.getOne(turniketHistoryDto.getTurniketId()));
        turniketHistory.setType(turniketHistoryDto.getType());

        if (tuniketRepository.getOne(turniketHistoryDto.getTurniketId()).
                getCompany().getId().equals(turniketHistoryDto.getCompanyID())) {
            turniketHistory.setSuccessful(true);
            tuniketHistoryRepository.save(turniketHistory);
            return new ApiResponse("success", true, turniketHistory);
        } else
            return new ApiResponse("boshqa company ga kirmoqchimisan haliii", false);
    }

    // o'zini shaxsiy barcha ishga kelib etishi
    public ApiResponse getAllHistory(HttpServletRequest httpServletRequest) {
        String authorization = httpServletRequest.getHeader("Authorization");
        authorization = authorization.substring(7);
        String usernameFromToken = jwtProvider.getUsernameFromToken(authorization);
        return new ApiResponse("marxamat sizning tarixingiz",
                true, tuniketHistoryRepository.findAllByTurniket_Owner_Username(usernameFromToken));
    }

    //    /user id orqali uning tarixini ko'rish
    public ApiResponse getOneStaff(HttpServletRequest httpServletRequest, UUID id) {
        String authorization = httpServletRequest.getHeader("Authorization");
        authorization = authorization.substring(7);
        String usernameFromToken = jwtProvider.getUsernameFromToken(authorization);
        Optional<User> byUsername = userRepository.findByUsername(usernameFromToken);
        Optional<User> byIdxodim = userRepository.findById(id);

        if (!byIdxodim.isPresent() && !byUsername.isPresent() &&
                !(byUsername.get().getRoles().contains(new Role(1, RoleName.ROLE_DIRECTOR))
                        || byUsername.get().getRoles().contains(new Role(2, RoleName.ROLE_MANAGER))))
            return new ApiResponse("Adashdiz afsus", false);
        Optional<TurniketHistory> allByTurniket_owner_id = tuniketHistoryRepository.findAllByTurniket_Owner_Id(id);
        if (!(allByTurniket_owner_id.isPresent())) {
            return new ApiResponse("Adashdiz afsus", false);
        }
        return new ApiResponse("marxamat xodimlarning tasklari",
                true, allByTurniket_owner_id.get());
    }


}
