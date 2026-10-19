package com.afsec.payroll.repository;

import com.afsec.payroll.Staff;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StaffRepository extends MongoRepository<Staff, String> {

}