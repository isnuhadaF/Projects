package com.afsec.payroll;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "staff")
public abstract class Staff {

    @Id
    private String id;
    private String name;


    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}