package com.afsec.payroll;

public class FullTimeStaff extends Staff implements Payable{
    private double monthlySalary;


    @Override
    public double calculatePay() {
        return monthlySalary;
    }

    public double getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(double salary) {
        this.monthlySalary = salary;
    }


}
