package com.example.Bank.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ClearingCounter {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String  name;

    public ClearingCounter() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ClearingCounter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
