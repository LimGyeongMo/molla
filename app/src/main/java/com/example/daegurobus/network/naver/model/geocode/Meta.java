package com.example.daegurobus.network.naver.model.geocode;

import com.google.gson.annotations.SerializedName;

public class Meta {
    @SerializedName("totalCount")
    private int totalCount;

    @SerializedName("page")
    private int page;

    @SerializedName("count")
    private int count;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
