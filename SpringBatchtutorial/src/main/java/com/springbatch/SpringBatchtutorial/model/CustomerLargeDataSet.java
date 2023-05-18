package com.springbatch.SpringBatchtutorial.model;

public class CustomerLargeDataSet {

  private int id;
  private String occupation;
  private String name;
  private int custId;
  private double salary;
  private String state;
  private String designation;
  private String efficiency;

  public CustomerLargeDataSet() {}

  public CustomerLargeDataSet(
      int id,
      String occupation,
      String name,
      int custId,
      double salary,
      String state,
      String designation,
      String efficiency) {
    this.id = id;
    this.occupation = occupation;
    this.name = name;
    this.custId = custId;
    this.salary = salary;
    this.state = state;
    this.designation = designation;
    this.efficiency = efficiency;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getOccupation() {
    return occupation;
  }

  public void setOccupation(String occupation) {
    this.occupation = occupation;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getCustId() {
    return custId;
  }

  public void setCustId(int custId) {
    this.custId = custId;
  }

  public double getSalary() {
    return salary;
  }

  public void setSalary(double salary) {
    this.salary = salary;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getDesignation() {
    return designation;
  }

  public void setDesignation(String designation) {
    this.designation = designation;
  }

  public String getEfficiency() {
    return efficiency;
  }

  public void setEfficiency(String efficiency) {
    this.efficiency = efficiency;
  }
}
