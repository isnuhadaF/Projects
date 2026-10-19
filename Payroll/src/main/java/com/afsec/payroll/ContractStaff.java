package com.afsec.payroll;

public class ContractStaff extends Staff implements Payable{
    private double hourlyRate;
    private int hoursWorked;

    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    public int getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(int hoursWorked) { this.hoursWorked = hoursWorked; }

    @Override
    public double calculatePay() { return hourlyRate * hoursWorked; }
}
