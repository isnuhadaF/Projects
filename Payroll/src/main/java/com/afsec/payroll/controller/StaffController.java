package com.afsec.payroll.controller;

import com.afsec.payroll.*;
import com.afsec.payroll.repository.StaffRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/staff")
public class StaffController {

    private final StaffRepository repository;

    public StaffController(StaffRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/fulltime")
    public FullTimeStaff addFullTime(@RequestBody FullTimeStaff staff) {
        return repository.save(staff);
    }

    @PostMapping("/contract")
    public ContractStaff addContract(@RequestBody ContractStaff staff) {
        return repository.save(staff);
    }

    @GetMapping
    public List<Staff> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}/pay")
    public double getPay(@PathVariable String id) {
        Staff staff = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No staff with id " + id));

        if (staff instanceof Payable payable) {
            return payable.calculatePay();
        }
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "This staff type cannot be paid");
    }
}