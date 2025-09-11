package com.example.demo.service;

import com.example.demo.entity.Company;
import com.example.demo.repository.ICompanyRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final ICompanyRepository companyRepository;

    public CompanyService(ICompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public List<Company> getCompanies(Integer page, Integer size) {
        if (page == null || size == null)
            return companyRepository.findAll();
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return companyRepository.findAll(pageRequest).stream().toList();
    }

    public Company updateCompany(int id, Company updatedCompany) {
        getCompanyById(id);// Check if company exists
        updatedCompany.setId(id);
        return companyRepository.save(updatedCompany);
    }

    public Company getCompanyById(int id) {
        Optional<Company> company = companyRepository.findById(id);
        if (company.isPresent()) {
            return company.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
    }

    public void deleteCompany(int id) {
        Company companyById = getCompanyById(id);
        companyRepository.delete(companyById);
    }
}
