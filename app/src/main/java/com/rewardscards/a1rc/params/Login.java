package com.rewardscards.a1rc.params;

public class Login {
    String email;
    String password;
    String token_id ;

    public Login(String email, String password,String token_id ) {
        this.email = email;
        this.password = password;
        this.token_id = token_id ;
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
