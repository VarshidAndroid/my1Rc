package com.rewardscards.a1rc.params;

public class ApplyRcParam {
    String first_name;
    String last_name;
    String address1;
    String address2;
    String address3;
    String address4;
    String province;
    String code;
    String price;
    String qty;
    String sa_identity_number;
    String mobile_number;

    public ApplyRcParam(String first_name, String last_name, String address1, String address2, String address3, String address4, String province, String code, String price, String qty, String sa_identity_number, String mobile_number) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.address4 = address4;
        this.province = province;
        this.code = code;
        this.price = price;
        this.qty = qty;
        this.sa_identity_number = sa_identity_number;
        this.mobile_number = mobile_number;
    }
}
