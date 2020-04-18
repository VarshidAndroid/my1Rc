package com.rewardscards.a1rc.params;

public class RegistrationMoel {
    String first_name;
    String last_name;
    String sa_identity_number;
    String mobile_number;
    String email;
    String password;
    String country_code;
    String token_id ;
    //address1,address2,address3,address4,device_id,device_type,token_id

    public RegistrationMoel(String first_name, String last_name, String sa_identity_number, String mobile_number, String email, String password,String country_code,String  token_id ) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.sa_identity_number = sa_identity_number;
        this.mobile_number = mobile_number;
        this.email = email;
        this.password = password;
        this.country_code = country_code;

        this.token_id  = token_id ;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getSa_identity_number() {
        return sa_identity_number;
    }

    public void setSa_identity_number(String sa_identity_number) {
        this.sa_identity_number = sa_identity_number;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
