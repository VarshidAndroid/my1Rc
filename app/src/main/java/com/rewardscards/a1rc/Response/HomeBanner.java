package com.rewardscards.a1rc.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeBanner {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("total_adverts")
    @Expose
    private String totalAdverts;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTotalAdverts() {
        return totalAdverts;
    }

    public void setTotalAdverts(String totalAdverts) {
        this.totalAdverts = totalAdverts;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }


    public class Datum {

        @SerializedName("ads_image")
        @Expose
        private String adsImage;
        @SerializedName("position")
        @Expose
        private String position;
        @SerializedName("store_number")
        @Expose
        private String store_number;

        public String getStore_number() {
            return store_number;
        }

        public void setStore_number(String store_number) {
            this.store_number = store_number;
        }

        public Datum(String store_number) {
            this.store_number = store_number;
        }

        public String getAdsImage() {
            return adsImage;
        }

        public void setAdsImage(String adsImage) {
            this.adsImage = adsImage;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }



    }
}
