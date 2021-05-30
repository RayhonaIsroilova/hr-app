package ecma.ai.hrapp.controller;

import ecma.ai.hrapp.entity.enums.TaskStatus;
import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.TaskDto;
import ecma.ai.hrapp.service.TaskServise;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    TaskServise taskServise;

    //PreAuthorize
    //get all
    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER')")
    @GetMapping
    public HttpEntity<?> getAll(HttpServletRequest httpServletRequest) {
        ApiResponse all = taskServise.getAll(httpServletRequest);
        return ResponseEntity.status(all.isSuccess() ? 200 : 409).body(all);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER')")
    //PreAuthorize
    //get one
    @GetMapping("/{id}")
    public HttpEntity<?> getOne(HttpServletRequest httpServletRequest, @PathVariable UUID id) {
        ApiResponse one = taskServise.getOne(httpServletRequest, id);
        return ResponseEntity.status(one.isSuccess() ? 200 : 409).body(one);
    }


    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER')")
    @GetMapping("/{status}")
    public HttpEntity<?> getAllSuccessTask(HttpServletRequest httpServletRequest,@PathVariable TaskStatus status) {
        ApiResponse one = taskServise.getAllSuccessTask(httpServletRequest, status);
        return ResponseEntity.status(one.isSuccess() ? 200 : 409).body(one);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER')")
    @GetMapping("/AfterDeadline")
    public HttpEntity<?> getWithTimeAfter(HttpServletRequest httpServletRequest) {
        ApiResponse one = taskServise.getWithTimeAfter(httpServletRequest);
        return ResponseEntity.status(one.isSuccess() ? 200 : 409).body(one);
    }


    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER')")
    @GetMapping("/BeforeDeadline")
    public HttpEntity<?> getWithTimeBefore(HttpServletRequest httpServletRequest) {
        ApiResponse one = taskServise.getWithTimeBefore(httpServletRequest);
        return ResponseEntity.status(one.isSuccess() ? 200 : 409).body(one);
    }
    //PreAuthorize
    //adding tasks
    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER')")
    @SneakyThrows
    @PostMapping
    public HttpEntity<?> addingTasks(@Valid @RequestBody TaskDto taskDto) {
        ApiResponse apiResponse = taskServise.addTask(taskDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    //PreAuthorize
    //update tasks
    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER')")
    @SneakyThrows
    @PutMapping("/{id}")
    public HttpEntity<?> editingTasks(@PathVariable UUID id,@Valid @RequestBody TaskDto taskDto, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = taskServise.putAll(httpServletRequest, id, taskDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
