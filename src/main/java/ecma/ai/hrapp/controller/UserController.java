package ecma.ai.hrapp.controller;

import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.UserDto;
import ecma.ai.hrapp.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.ws.spi.http.HttpContext;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    //
    //PreAuthorize
    //director uchun
    @PreAuthorize(value = "hasRole('ROLE_DIRECTOR')")
    @GetMapping
    public HttpEntity<?> getAllForDirector(HttpServletRequest httpServletRequest) {
        ApiResponse allUserForDirector = userService.getAllUserForDirector(httpServletRequest);
        return ResponseEntity.status(allUserForDirector.isSuccess() ? 200 : 409).body(allUserForDirector);
    }

    //    //PreAuthorize
//    //manager uchun
    @PreAuthorize(value = "hasRole('HR_MANAGER')")
    @GetMapping("/manager")
    public HttpEntity<?> getAllForManager(HttpServletRequest httpServletRequest) {
        ApiResponse allUserForManager = userService.getAllUserForManager(httpServletRequest);
        return ResponseEntity.status(allUserForManager.isSuccess() ? 200 : 409).body(allUserForManager);
    }


    //Yangi user qo'shish
    //MANAGER,DIREKTOR //PreAuthorize
    @PreAuthorize(value = "hasAnyRole('ROLE_MANAGER','ROLE_DIRECTOR')")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody UserDto userDto) throws MessagingException {

        ApiResponse response = userService.add(userDto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    //PreAuthorize
    //put mapping
    @PreAuthorize(value = "hasAnyRole('ROLE_MANAGER','ROLE_DIRECTOR')")
    @SneakyThrows
    @PutMapping("/{id}")
    public HttpEntity<?> update(@PathVariable UUID id, @Valid @RequestBody UserDto userDto) {
        ApiResponse update = userService.update(userDto, id);
        return ResponseEntity.status(update.isSuccess() ? 200 : 409).body(update);
    }
}
