package ecma.ai.hrapp.controller;

import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.TurniketDto;
import ecma.ai.hrapp.payload.TurniketHistoryDto;
import ecma.ai.hrapp.service.TurrniketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/api/turniket")
public class TurniketController {

    @Autowired
    TurrniketService turrniketService;

    //PreAuthorize
    //get all
    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER')")
    @GetMapping
    public HttpEntity<?> getAll(HttpServletRequest httpServletRequest){
        ApiResponse all = turrniketService.getAllHistory(httpServletRequest);
        return ResponseEntity.status(all.isSuccess()?200:409).body(all);
    }

    //PreAuthorize
    //get one
    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER')")
    @GetMapping("/{id}")
    public HttpEntity<?> getOne(@PathVariable UUID id,HttpServletRequest httpServletRequest){
        ApiResponse one = turrniketService.getOneStaff(httpServletRequest, id);
        return ResponseEntity.status(one.isSuccess()?200:409).body(one);
    }

    //PreAuthorize
    //adding turniket
    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER')")
    @PostMapping
    public HttpEntity<?> addingTurniket(HttpServletRequest request, @RequestBody TurniketDto turniketDto){
        ApiResponse apiResponse = turrniketService.addTurniket(request, turniketDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    //PreAuthorize
    //adding turniket history !!!!!!
    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER')")
    @PostMapping("/turniketHistory")
    public HttpEntity<?> addingTurniketHistory(HttpServletRequest request, @RequestBody TurniketHistoryDto turniketHistoryDto){
        ApiResponse apiResponse = turrniketService.addTurniketHistory(request, turniketHistoryDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
}
