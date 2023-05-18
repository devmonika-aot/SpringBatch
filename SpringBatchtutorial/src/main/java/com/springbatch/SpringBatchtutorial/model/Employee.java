package com.springbatch.SpringBatchtutorial.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

public class Employee {

  private String firstName;
  private String lastName;
  private int age;
  private Address address;
  private PhoneNumbers[] phoneNumbers;
  private String mobileNumber;
  private String faxNumer;

  public Employee() {}

  public Employee(
      String firstName,
      String lastName,
      int age,
      Address address,
      /*
      If we had List<Address> address and If we had to print this list to CSV then address.city won't work
      in the writer .

      We have to create FieldExtractor like below :

      public class AddressFieldExtractor implements FieldExtractor<Address> {
          @Override
          public Object[] extract(Address address) {
              return new Object[] { address.getStreet(), address.getCity(), address.getState(), address.getZipcode() };
          }
      }
      And then we have to edit writer like below to add extractor :
         Map<String, FieldExtractor<?>> extractors = new HashMap<>();
         extractors.put("addresses", new AddressFieldExtractor());
         fieldExtractor.setExtractors(extractors);
       */
      PhoneNumbers[] phoneNumbers,
      String mobileNumber,
      String faxNumer) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.age = age;
    this.address = address;
    this.phoneNumbers = phoneNumbers;
    this.mobileNumber = mobileNumber;
    this.faxNumer = faxNumer;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getFaxNumer() {
    return faxNumer;
  }

  public void setFaxNumer(String faxNumer) {
    this.faxNumer = faxNumer;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public PhoneNumbers[] getPhoneNumbers() {
    return phoneNumbers;
  }

  public void setPhoneNumbers(PhoneNumbers[] phoneNumbers) {
    this.phoneNumbers = phoneNumbers;
  }
}
