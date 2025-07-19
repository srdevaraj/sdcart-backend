package com.sdtechno.sdcart.payload;

public class RegisterRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String dob;
    private String mobile;
    private String altMobile;

    public RegisterRequest() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getAltMobile() { return altMobile; }
    public void setAltMobile(String altMobile) { this.altMobile = altMobile; }
}
