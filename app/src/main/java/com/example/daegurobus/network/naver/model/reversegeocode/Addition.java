package com.example.daegurobus.network.naver.model.reversegeocode;

import com.google.gson.annotations.SerializedName;

public class Addition {
    // addition0.도로명 주소이고 건물정보가 있는경우 ‘building’
    // addition1 도로명 주소이고 우편번호정보가 있는경우 ‘zipcode’
    // addition2 도로명 주소일경우 ‘roadGroupCode’
    @SerializedName("type")
    private String type;

    // addition0 도로명 주소이고 건물정보가 있는경우 건물명
    // addition1 도로명 주소이고 우편번호정보가 있는경우 우편번호
    // addition2 도로명 주소일경우 도로코드(12자리 road group code)
    @SerializedName("value")
    private String value;

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
