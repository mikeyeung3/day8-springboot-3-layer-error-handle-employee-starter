package com.example.demo.repository;

import com.example.demo.entity.Company;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CompanyRepository {

    private final List<Company> companies = new ArrayList<>();

    public Company createCompany(Company company) {
        company.setId(companies.size() + 1);
        companies.add(company);
        return company;
    }

    public List<Company> getCompanies(Integer page, Integer size) {
        if (page != null && size != null) {
            int start = (page - 1) * size;
            int end = Math.min(start + size, companies.size());
            if (start >= companies.size()) {
                return new ArrayList<>();
            }
            return companies.subList(start, end);
        }
        return companies;
    }

    public Company updateCompany(int id, Company updatedCompany) {
        Company company = getCompanyById(id);
        company.setName(updatedCompany.getName());
        return company;

    }

    public Company getCompanyById(int id) {
        for (Company c : companies) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public void deleteCompany(int id) {
        companies.remove(getCompanyById(id));
    }

    public void empty() {
        companies.clear();
    }
}
