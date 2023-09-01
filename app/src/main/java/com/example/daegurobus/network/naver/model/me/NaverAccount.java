package com.example.daegurobus.network.naver.model.me;

import com.google.gson.annotations.SerializedName;

public class NaverAccount {
    @SerializedName("id")
    private String id;

    @SerializedName("nickname")
    private String nickName;

    @SerializedName("profile_image")
    private String profileImage;

    @SerializedName("age")
    private String age;

    @SerializedName("gender")
    private String gender;

    @SerializedName("email")
    private String email;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("mobile_e164")
    private String mobileE164;

    @SerializedName("name")
    private String name;

    @SerializedName("birthday")
    private String birthday;

    @SerializedName("birthyear")
    private String birthyear;

    public String getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getMobileE164() {
        return mobileE164;
    }

    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getBirthyear() {
        return birthyear;
    }
}
