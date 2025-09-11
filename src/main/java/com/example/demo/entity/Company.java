package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    public Company() {

    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
