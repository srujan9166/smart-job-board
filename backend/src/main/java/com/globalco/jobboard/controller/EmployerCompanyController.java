package com.globalco.jobboard.controller;

import com.globalco.jobboard.dto.request.CompanyRequestDTO;
import com.globalco.jobboard.dto.response.CompanyResponseDTO;
import com.globalco.jobboard.security.CustomUserDetails;
import com.globalco.jobboard.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/** Authenticated employer company-profile endpoints. */
@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class EmployerCompanyController {
    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyResponseDTO> create(@Valid @RequestBody CompanyRequestDTO dto, @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.createCompanyForEmployer(dto, principal.getId()));
    }

    @GetMapping("/me")
    public ResponseEntity<CompanyResponseDTO> mine(@AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(companyService.getMyCompany(principal.getId()));
    }

    @PutMapping("/me")
    public ResponseEntity<CompanyResponseDTO> update(@Valid @RequestBody CompanyRequestDTO dto, @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(companyService.updateMyCompany(dto, principal.getId()));
    }
}
