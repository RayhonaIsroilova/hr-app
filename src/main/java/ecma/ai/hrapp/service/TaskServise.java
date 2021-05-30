package ecma.ai.hrapp.service;

import ecma.ai.hrapp.component.MailSender;
import ecma.ai.hrapp.component.PasswordGenerator;
import ecma.ai.hrapp.entity.Role;
import ecma.ai.hrapp.entity.Task;
import ecma.ai.hrapp.entity.User;
import ecma.ai.hrapp.entity.enums.RoleName;
import ecma.ai.hrapp.entity.enums.TaskStatus;
import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.TaskDto;
import ecma.ai.hrapp.repository.TaskRepository;
import ecma.ai.hrapp.repository.UserRepository;
import ecma.ai.hrapp.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskServise {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordGenerator passwordGenerator;
    @Autowired
    MailSender mailSender;
    @Autowired
    JwtProvider jwtProvider;

    public ApiResponse addTask(TaskDto taskDto) throws MessagingException {
        Optional<User> taker = userRepository.findById(taskDto.getTaskTaker());
        Optional<User> giver = userRepository.findById(taskDto.getTaskGiver());

        if (!taker.isPresent() || !giver.isPresent()) {
            return new ApiResponse("taker yoki giver yoq", false);
        }

        int maxTake = 0;
        for (Role role : taker.get().getRoles()) {
            maxTake = Math.max(role.getName().getRoleC(), maxTake);
        }
        int maxGive = 0;
        for (Role role : giver.get().getRoles()) {
            maxGive = Math.max(role.getName().getRoleC(), maxGive);
        }
        if (maxTake > maxGive) {
            return new ApiResponse("kichkina odam katta odamga task berolmaydi", false);
        }

        Task newTask = new Task();
        newTask.setTaskTaker(taker.get());
        newTask.setTaskGiver(giver.get());
        newTask.setName(taskDto.getName());
        newTask.setStatus(TaskStatus.NEW);
        newTask.setDeadline(taskDto.getDeadline());
        newTask.setDescription(taskDto.getDescription());
//email jonatish
        boolean send = mailSender.send(taker.get().getEmail(),
                newTask.toString().concat("   \n vazifa seni kutayabdi\n"));


        if (send) {
            taskRepository.save(newTask);
            return new ApiResponse("task saqlandi. emailga jo'natildi. emaildan korib ol", true);
        } else {
            return new ApiResponse("emailga jo'natilmadi", false);
        }


    }

    // it's fot Manager and Director
    public ApiResponse getAll(HttpServletRequest httpServletRequest) {
        String authorization = httpServletRequest.getHeader("Authorization");
        authorization = authorization.substring(7);
        String usernameFromToken = jwtProvider.getUsernameFromToken(authorization);
        return new ApiResponse("marxamat sizning tasklaringiz",
                true, taskRepository.findAllByTaskGiver_Username(usernameFromToken));
    }

    //uzi bergan aynan qaysidir taskni ko'radi
    public ApiResponse getOne(HttpServletRequest httpServletRequest, UUID uuid) {
        String authorization = httpServletRequest.getHeader("Authorization");
        authorization = authorization.substring(7);
        String usernameFromToken = jwtProvider.getUsernameFromToken(authorization);
        return new ApiResponse("marxamat sizning tasklaringiz",
                true, taskRepository.findAllByTaskGiver_UsernameAndId(usernameFromToken, uuid));
    }


    // it's fot Manager and Director. status orqali tasklarni olish
    public ApiResponse getAllSuccessTask(HttpServletRequest httpServletRequest, TaskStatus taskStatus) {
        return new ApiResponse("marxamat sizning tasklaringiz",
                true, taskRepository.findAllByStatus(taskStatus));
    }


    public ApiResponse putAll(HttpServletRequest httpServletRequest, UUID uuid, TaskDto taskDto) throws MessagingException {
        String authorization = httpServletRequest.getHeader("Authorization");
        authorization = authorization.substring(7);
        String usernameFromToken = jwtProvider.getUsernameFromToken(authorization);
        Optional<Task> byId = taskRepository.findById(uuid);
        if (!byId.isPresent()) return new ApiResponse("bu task mavjud emas", false);

        if (!taskRepository.existsByTaskGiver_UsernameAndId(usernameFromToken, uuid)) {
            return new ApiResponse("bu task sizga tegishli emas", false);
        }
        Task task = byId.get();
        task.setStatus(TaskStatus.COMPLETED);
        task.setCompletedDate(new Timestamp(System.currentTimeMillis()));


        //email jonatish
        boolean send = mailSender.send(task.getTaskTaker().getEmail(), task.toString().concat("  " +
                " \n vazifani bajarib boldim"));


        if (send) {
            taskRepository.save(task);
            return new ApiResponse("task saqlandi. emailga jo'natildi", true);
        } else {
            return new ApiResponse("emailga jo'natilmadi", false);
        }
    }


    public ApiResponse getWithTimeAfter(HttpServletRequest httpServletRequest) {
        String authorization = httpServletRequest.getHeader("Authorization");
        authorization = authorization.substring(7);
        String usernameFromToken = jwtProvider.getUsernameFromToken(authorization);
        Optional<User> byUsername = userRepository.findByUsername(usernameFromToken);
        if (!byUsername.isPresent() && !(byUsername.get().getRoles().contains(new Role(1, RoleName.ROLE_DIRECTOR))
                || byUsername.get().getRoles().contains(new Role(2, RoleName.ROLE_MANAGER)))) {
            return new ApiResponse("men sizni tanimadm", false);
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Optional<Task> allByDeadlineAfter = taskRepository.findAllByDeadlineAfter(timestamp);
        return new ApiResponse("sizning so'rovingiz", true, allByDeadlineAfter.orElse(new Task()));

    }


    public ApiResponse getWithTimeBefore(HttpServletRequest httpServletRequest) {
        String authorization = httpServletRequest.getHeader("Authorization");
        authorization = authorization.substring(7);
        String usernameFromToken = jwtProvider.getUsernameFromToken(authorization);
        Optional<User> byUsername = userRepository.findByUsername(usernameFromToken);
        if (!byUsername.isPresent() && !(byUsername.get().getRoles().contains(new Role(1, RoleName.ROLE_DIRECTOR))
                || byUsername.get().getRoles().contains(new Role(2, RoleName.ROLE_MANAGER)))) {
            return new ApiResponse("men sizni tanimadm", false);
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Optional<Task> allByDeadlineAfter = taskRepository.findAllByDeadlineBefore(timestamp);
        return new ApiResponse("sizning so'rovingiz", true, allByDeadlineAfter.orElse(new Task()));

    }

}
