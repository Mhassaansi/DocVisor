package com.fictivestudios.docsvisor.apiManager.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppResponseModel<T> {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }


    public class Datum {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("data_1")
        @Expose
        private String data1;
        @SerializedName("data_2")
        @Expose
        private String data2;
        @SerializedName("data_3")
        @Expose
        private String data3;
        @SerializedName("data_4")
        @Expose
        private String data4;
        @SerializedName("data_5")
        @Expose
        private String data5;
        @SerializedName("data_6")
        @Expose
        private String data6;
        @SerializedName("data_7")
        @Expose
        private String data7;
        @SerializedName("data_8")
        @Expose
        private String data8;
        @SerializedName("data_9")
        @Expose
        private String data9;
        @SerializedName("data_10")
        @Expose
        private String data10;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getData1() {
            return data1;
        }

        public void setData1(String data1) {
            this.data1 = data1;
        }

        public String getData2() {
            return data2;
        }

        public void setData2(String data2) {
            this.data2 = data2;
        }

        public String getData3() {
            return data3;
        }

        public void setData3(String data3) {
            this.data3 = data3;
        }

        public String getData4() {
            return data4;
        }

        public void setData4(String data4) {
            this.data4 = data4;
        }

        public String getData5() {
            return data5;
        }

        public void setData5(String data5) {
            this.data5 = data5;
        }

        public String getData6() {
            return data6;
        }

        public void setData6(String data6) {
            this.data6 = data6;
        }

        public String getData7() {
            return data7;
        }

        public void setData7(String data7) {
            this.data7 = data7;
        }

        public String getData8() {
            return data8;
        }

        public void setData8(String data8) {
            this.data8 = data8;
        }

        public String getData9() {
            return data9;
        }

        public void setData9(String data9) {
            this.data9 = data9;
        }

        public String getData10() {
            return data10;
        }

        public void setData10(String data10) {
            this.data10 = data10;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }
}
