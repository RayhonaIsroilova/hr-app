package ecma.ai.hrapp.controller;

import ecma.ai.hrapp.entity.enums.Month;
import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.PaidDto;
import ecma.ai.hrapp.payload.TurniketDto;
import ecma.ai.hrapp.payload.TurniketHistoryDto;
import ecma.ai.hrapp.service.PaidServise;
import ecma.ai.hrapp.service.TurrniketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/paidSalary")
public class PaidSalaryController {

    @Autowired
    PaidServise servise;

    //PreAuthorize
    //get all
    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER')")
    @GetMapping("/{month}")
    public HttpEntity<?> getAllByMonth(HttpServletRequest httpServletRequest,@PathVariable Month month){
        ApiResponse all = servise.getByMonth(httpServletRequest,month);
        return ResponseEntity.status(all.isSuccess()?200:409).body(all);
    }

    //PreAuthorize
    //get one
    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER')")
    @GetMapping("/{ownerId}")
    public HttpEntity<?> getOne(@PathVariable UUID ownerId,HttpServletRequest httpServletRequest){
        ApiResponse one = servise.getByOwner(httpServletRequest, ownerId);
        return ResponseEntity.status(one.isSuccess()?200:409).body(one);
    }

    //PreAuthorize
    //adding turniket
    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER')")
    @PostMapping
    public HttpEntity<?> adding(HttpServletRequest request,@Valid @RequestBody PaidDto paidDto){
        ApiResponse apiResponse = servise.addSalary(request, paidDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    //PreAuthorize
    //adding turniket history !!!!!!
    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER')")
    @PutMapping("/{uuid}")
    public HttpEntity<?> update(HttpServletRequest request,
                                               @Valid @RequestBody PaidDto paidDto,
                                               @PathVariable UUID uuid){
        ApiResponse apiResponse = servise.Update(request,paidDto,uuid);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
}
