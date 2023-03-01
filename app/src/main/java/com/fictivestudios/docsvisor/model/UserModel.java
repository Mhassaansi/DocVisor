package com.fictivestudios.docsvisor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("user_bio")
    @Expose
    private String userBio;
    @SerializedName("user_social_type")
    @Expose
    private String user_social_type;
    @SerializedName("user_is_verified")
    @Expose
    private Integer userIsVerified;
    @SerializedName("user_social_token")
    @Expose
    private String user_social_token;
    @SerializedName("user_profile_complete")
    @Expose
    private Integer userIsProfileComplete;
    @SerializedName("user_is_forgot")
    @Expose
    private String user_is_forgot;
    @SerializedName("user_verfied_code")
    @Expose
    private String user_verfied_code;
    @SerializedName("user_gender")
    @Expose
    private String userGender;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("user_device_type")
    @Expose
    private String userDeviceType;
    @SerializedName("user_device_token")
    @Expose
    private String userDeviceToken;
    private final static long serialVersionUID = -2999001043605277168L;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getUserIsVerified() {
        return userIsVerified;
    }

    public void setUserIsVerified(Integer userIsVerified) {
        this.userIsVerified = userIsVerified;
    }


    public Integer getUserIsProfileComplete() {
        return userIsProfileComplete;
    }

    public void setUserIsProfileComplete(Integer userIsProfileComplete) {
        this.userIsProfileComplete = userIsProfileComplete;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserDeviceType() {
        return userDeviceType;
    }

    public void setUserDeviceType(String userDeviceType) {
        this.userDeviceType = userDeviceType;
    }

    public String getUserDeviceToken() {
        return userDeviceToken;
    }

    public void setUserDeviceToken(String userDeviceToken) {
        this.userDeviceToken = userDeviceToken;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public String getUser_social_type() {
        return user_social_type;
    }

    public void setUser_social_type(String user_social_type) {
        this.user_social_type = user_social_type;
    }

    public String getUser_social_token() {
        return user_social_token;
    }

    public void setUser_social_token(String user_social_token) {
        this.user_social_token = user_social_token;
    }

    public String getUser_is_forgot() {
        return user_is_forgot;
    }

    public void setUser_is_forgot(String user_is_forgot) {
        this.user_is_forgot = user_is_forgot;
    }

    public String getUser_verfied_code() {
        return user_verfied_code;
    }

    public void setUser_verfied_code(String user_verfied_code) {
        this.user_verfied_code = user_verfied_code;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

}
