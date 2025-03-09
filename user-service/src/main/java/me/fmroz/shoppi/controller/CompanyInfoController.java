package me.fmroz.shoppi.controller;

import lombok.RequiredArgsConstructor;
import me.fmroz.shoppi.model.CompanyInfo;
import me.fmroz.shoppi.service.CompanyInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company-info")
@RequiredArgsConstructor
public class CompanyInfoController {

    private final CompanyInfoService companyInfoService;

    @GetMapping("/{userId}")
    public ResponseEntity<CompanyInfo> getCompanyInfo(@PathVariable Long userId) {
        return ResponseEntity.ok(companyInfoService.findCompanyInfoById(userId));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<CompanyInfo> addCompanyInfo(@PathVariable Long userId, @RequestBody CompanyInfo companyInfo) {
        return ResponseEntity.ok(companyInfoService.addCompanyInfo(userId, companyInfo));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<CompanyInfo> updateCompanyInfo(@PathVariable Long userId, @RequestBody CompanyInfo updatedCompanyInfo) {
        return ResponseEntity.ok(companyInfoService.updateCompanyInfo(userId, updatedCompanyInfo));
    }
}
