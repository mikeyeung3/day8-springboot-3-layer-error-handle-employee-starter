package com.example.demo;

import com.example.demo.entity.Company;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CompanyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanCompanies() {
        jdbcTemplate.execute("DELETE FROM employee");
        jdbcTemplate.execute("DELETE FROM company");
        jdbcTemplate.execute("ALTER TABLE company AUTO_INCREMENT = 1");
    }

    private Company createCompany() throws Exception {
        Gson gson = new Gson();
        String company = gson.toJson(new Company(null, "Spring"));
        ResultActions result = mockMvc.perform(post("/companies").contentType(MediaType.APPLICATION_JSON).content(company));
        String jsonString = result.andReturn().getResponse().getContentAsString();
        return gson.fromJson(jsonString, Company.class);
    }

    @Test
    void should_return_created_company_when_post_companies() throws Exception {
        String requestBody = """
                {
                    "name": "Spring"
                }
                """;
        MockHttpServletRequestBuilder request = post("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Spring"));
    }

    @Test
    void should_return_all_companies_when_no_param() throws Exception {
        createCompany();
        mockMvc.perform(get("/companies").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void should_return_company_when_get_id_found() throws Exception {
        Company company = createCompany();

        MockHttpServletRequestBuilder request = get("/companies/" + company.getId())
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(company.getId()))
                .andExpect(jsonPath("$.name").value(company.getName()));
    }

    @Test
    void should_return_company_when_put_with_id_found() throws Exception {
        Company company = createCompany();
        String requestBody = """
                {
                    "name": "Spring2"
                }
                """;
        MockHttpServletRequestBuilder request = put("/companies/" + company.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(company.getId()))
                .andExpect(jsonPath("$.name").value("Spring2"));
    }

    @Test
    void should_return_no_content_when_delete_id_found() throws Exception {
        Company company = createCompany();

        MockHttpServletRequestBuilder request = delete("/companies/" + company.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    void should_return_truncated_companies_when_page_size_is_limit() throws Exception {
        for (int i = 0; i < 10; i++) {
            createCompany();
        }
        MockHttpServletRequestBuilder request = get("/companies?page=1&size=5")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void should_status_404_when_get_company_by_id_not_found() throws Exception {
        MockHttpServletRequestBuilder request = get("/companies/999")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    void should_status_404_when_put_company_by_id_not_found() throws Exception {
        String requestBody = """
                {
                    "name": "Spring2"
                }
                """;
        MockHttpServletRequestBuilder request = put("/companies/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }
}
