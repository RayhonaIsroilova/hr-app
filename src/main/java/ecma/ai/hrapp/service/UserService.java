package ecma.ai.hrapp.service;

import ecma.ai.hrapp.component.Checker;
import ecma.ai.hrapp.component.MailSender;
import ecma.ai.hrapp.component.PasswordGenerator;
import ecma.ai.hrapp.entity.Role;
import ecma.ai.hrapp.entity.User;
import ecma.ai.hrapp.entity.enums.RoleName;
import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.UserDto;
import ecma.ai.hrapp.repository.RoleRepository;
import ecma.ai.hrapp.repository.UserRepository;
import ecma.ai.hrapp.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    Checker checker;
    @Autowired
    PasswordGenerator passwordGenerator;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MailSender mailSender;
    @Autowired
    JwtProvider jwtProvider;


    public ApiResponse add(UserDto userDto) throws MessagingException {
        Optional<Role> optionalRole = roleRepository.findById(userDto.getRoleId());
        if (!optionalRole.isPresent()) return new ApiResponse("Role id not found!", false);

        boolean check = checker.check(optionalRole.get().getName().name());//

        if (!check) {
            return new ApiResponse("Dostup netu!", false);
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            return new ApiResponse("Already exists!", false);
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPosition(userDto.getPosition());

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(optionalRole.get());
        user.setRoles(roleSet);

        String password = passwordGenerator.genRandomPassword(8);
        user.setPassword(passwordEncoder.encode(password));

        String code = UUID.randomUUID().toString();
        user.setVerifyCode(code);

        userRepository.save(user);

        //mail xabar yuborish kk
        boolean addStaff = mailSender.mailTextAddStaff(userDto.getEmail(), code, password);

        if (addStaff) {
            return new ApiResponse("User qo'shildi! va emailga xabar ketdi!", true);
        } else {
            return new ApiResponse("Xatolik yuz berdi", false);
        }
    }

    public ApiResponse update(UserDto userDto, UUID uuid) throws MessagingException {
        Optional<Role> optionalRole = roleRepository.findById(userDto.getRoleId());
        if (!optionalRole.isPresent()) return new ApiResponse("Role id not found!", false);

        if (!userRepository.existsById(uuid)) return new ApiResponse("user id not found!", false);

        boolean check = checker.check(optionalRole.get().getName().name());//

        if (!check) {
            return new ApiResponse("Dostup netu!", false);
        }

        if (userRepository.existsByEmailAndIdNot(userDto.getEmail(), uuid)) {
            return new ApiResponse("Already exists!", false);
        }
        User user = userRepository.findById(uuid).get();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPosition(userDto.getPosition());

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(optionalRole.get());
        user.setRoles(roleSet);
//parolni edit qilib bolmaydi !!! shundoq ham xavfsizlik 1000 LEVEL

        boolean send = mailSender.send(user.getEmail(),
                "parolni edit qilib bolmaydi !!! shundoq ham xavfsizlik 1000 LEVEL account edited");
        if (send) {
            userRepository.save(user);
            return new ApiResponse("user saqlandi. emailga jo'natildi. emaildan korib ol", true);
        } else {
            return new ApiResponse("emailga jo'natilmadi", false);
        }
    }

    //director un
    public ApiResponse getAllUserForDirector(HttpServletRequest httpServletRequest) {
        String authorization = httpServletRequest.getHeader("Authorization");
        authorization = authorization.substring(7);
        String usernameFromToken = jwtProvider.getUsernameFromToken(authorization);

        Optional<User> byUsername = userRepository.findByUsername(usernameFromToken);
        if (!byUsername.isPresent() && !byUsername.get().getRoles().contains(RoleName.ROLE_DIRECTOR.name()))
            return new ApiResponse("siz kimsiz?", false);


        return new ApiResponse("mana oll", true, userRepository.findAll());
    }

    //manager un
    public ApiResponse getAllUserForManager(HttpServletRequest httpServletRequest) {
        String authorization = httpServletRequest.getHeader("Authorization");
        authorization = authorization.substring(7);
        String usernameFromToken = jwtProvider.getUsernameFromToken(authorization);

        Optional<User> byUsername = userRepository.findByUsername(usernameFromToken);
        if (!byUsername.isPresent() && !byUsername.get().getRoles().contains(RoleName.ROLE_MANAGER.name()))
            return new ApiResponse("siz kimsiz?", false);


        return new ApiResponse("mana oll", true
                ,userRepository.findByRolesLike()
                );

    }


}
