package com.example.daegurobus.model;

import com.example.daegurobus.network.DEFINE;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestBus {
    @SerializedName("token")
    private String Token;
    @SerializedName("mcode")
    private String mcode;
    @SerializedName("sendTime")
    private String SendTime;
    @SerializedName("requestData")
    private String RequestData;
    @SerializedName("paramCount")
    private int ParamCount;
    @SerializedName("proc")
    private String Proc;

    private StringBuilder sb;



    public RequestBus(String mcode, String Proc)
    {
        this.Token = DEFINE.USER_TOKEN;
        this.mcode = mcode;
        this.SendTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//        this.RequestData = this.getRequestData();
//       this.ParamCount = this.getRequestData().length();
        this.Proc = Proc;
        sb = new StringBuilder();
    }

    public String getToken(){
        return Token;
    }
    public String getSendTime(){
        return SendTime;
    }
//    public String getRequestData(){
//        return RequestData;
//    }
    public String getMcode() {return mcode;}
    public int getParamCount(){
        return ParamCount;
    }
    public String getProc(){
        return Proc;
    }

    public void addParam(String param) {
        if (ParamCount > 0 ) {
            this.sb.append(DEFINE.DELIMETER);
        }

        this.sb.append(param);
        ParamCount++;
    }

    public void commit() {
        RequestData = sb.toString();
    }

    public String getRequestData() {
        return RequestData;
    }
}
