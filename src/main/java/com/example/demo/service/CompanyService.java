package com.example.demo.service;

import com.example.demo.entity.Company;
import com.example.demo.repository.CompanyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company createCompany(Company company) {
        return companyRepository.createCompany(company);
    }

    public List<Company> getCompanies(Integer page, Integer size) {
        return companyRepository.getCompanies(page, size);
    }

    public Company updateCompany(int id, Company updatedCompany) {
        Company existingCompany = companyRepository.getCompanyById(id);
        if (existingCompany == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
        return companyRepository.updateCompany(id, updatedCompany);
    }

    public Company getCompanyById(int id) {
        Company company = companyRepository.getCompanyById(id);
        if (company != null) {
            return company;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
    }

    public void deleteCompany(int id) {
        Company company = companyRepository.getCompanyById(id);
        if (company != null) {
            companyRepository.deleteCompany(id);
            return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
    }

    public void empty() {
        companyRepository.empty();
    }

}
