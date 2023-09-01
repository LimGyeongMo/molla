package com.example.daegurobus.network.naver.model.reversegeocode;

import com.google.gson.annotations.SerializedName;

public class Land {
    //    지번주소의 경우 지적 타입.
//    예) 대한민국 지번 주소인 경우 1: 일반토지, 2: 산
//    도로명주소의 경우 reserved
    @SerializedName("type")
    private String type;

    //    상세 명칭.
//    예) 대한민국 지번 주소인 경우 reserved
//    대한민국 도로명 주소인 경우 도로명
    @SerializedName("name")
    private String name;

    //    상세 번호 1.
//    예) 대한민국 지번 주소인 경우 토지 본번호
//    대한민국 도로명 주소인 경우 상세주소
    @SerializedName("number1")
    private String number1;

    //    상세 번호 2.
//    예) 대한민국 지번 주소인 경우 토지 부번호
//    대한민국 도로명 주소인 경우 reserved
    @SerializedName("number2")
    private String number2;

    //    추가정보
//    지번 주소인 경우 reserved
//    도로명 주소인 경우 건물정보
    @SerializedName("addition0")
    private Addition addition0;

    //    추가정보
//    지번 주소인 경우 reserved
//    도로명 주소인 경우 우편번호
    @SerializedName("addition1")
    private Addition addition1;

    //    추가정보
//    지번 주소인 경우 reserved
//    도로명 주소인 경우 도로코드
    @SerializedName("addition2")
    private Addition addition2;

    public String getType() {
        if (type == null) {
            type = "";
        }

        return type;
    }

    public String getName() {
        if (name == null) {
            name = "";
        }

        return name;
    }

    public String getNumber1() {
        if (number1 == null) {
            number1 = "";
        }

        return number1;
    }

    public String getNumber2() {
        if (number2 == null) {
            number2 = "";
        }

        return number2;
    }

    public Addition getAddition0() {
        if (addition0 == null) {
            addition0 = new Addition();
        }

        return addition0;
    }

    public Addition getAddition1() {
        if (addition1 == null) {
            addition1 = new Addition();
        }

        return addition1;
    }

    public Addition getAddition2() {
        if (addition2 == null) {
            addition2 = new Addition();
        }

        return addition2;
    }
}
