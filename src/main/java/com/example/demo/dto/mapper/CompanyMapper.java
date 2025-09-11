package com.example.demo.dto.mapper;

import com.example.demo.dto.CompanyRequest;
import com.example.demo.dto.CompanyResponse;
import com.example.demo.entity.Company;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyMapper {
    public CompanyResponse toResponse(Company company) {
        CompanyResponse response = new CompanyResponse();
        BeanUtils.copyProperties(company, response);
        return response;
    }

    public List<CompanyResponse> toResponse(List<Company> companies) {
        return companies.stream().map(this::toResponse).toList();
    }

    public Company toEntity(CompanyRequest response) {
        Company company = new Company();
        BeanUtils.copyProperties(response, company);
        return company;
    }
}
