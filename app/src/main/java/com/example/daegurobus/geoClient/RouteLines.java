package com.example.daegurobus.geoClient;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RouteLines {
    @SerializedName("code")
    private String code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private List<Content> data;


    @Override
    public String toString() {
        return "RouteLines{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Content> getData() {
        return data;
    }

    public void setData(List<Content> data) {
        this.data = data;
    }

      class Content {
        public String type;
        public ArrayList<ArrayList<Double>> coordinates;

          public ArrayList<ArrayList<Double>> getCoordinates() {
              return coordinates;
          }

          public void setCoordinates(ArrayList<ArrayList<Double>> coordinates) {
              this.coordinates = coordinates;
          }

          public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }


}
