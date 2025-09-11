package com.example.demo.controller;

import com.example.demo.dto.CompanyRequest;
import com.example.demo.dto.CompanyResponse;
import com.example.demo.dto.mapper.CompanyMapper;
import com.example.demo.entity.Company;
import com.example.demo.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;

    public CompanyController(CompanyService companyService, CompanyMapper companyMapper) {
        this.companyService = companyService;
        this.companyMapper = companyMapper;
    }

    @GetMapping
    public List<CompanyResponse> getCompanies(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        List<Company> companies = companyService.getCompanies(page, size);
        return companyMapper.toResponse(companies);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponse createCompany(@RequestBody CompanyRequest companyRequest) {
        Company entity = companyMapper.toEntity(companyRequest);
        return companyMapper.toResponse(companyService.createCompany(entity));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompanyResponse updateCompany(@PathVariable int id, @RequestBody CompanyRequest companyRequest) {
        Company entity = companyMapper.toEntity(companyRequest);
        return companyMapper.toResponse(companyService.updateCompany(id, entity));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompanyResponse getCompanyById(@PathVariable int id) {
        return companyMapper.toResponse(companyService.getCompanyById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@PathVariable int id) {
        companyService.deleteCompany(id);
    }
}
