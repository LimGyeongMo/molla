package com.example.daegurobus.network.kakao.response;

import com.google.gson.annotations.SerializedName;
import com.example.daegurobus.network.kakao.model.Document;
import com.example.daegurobus.network.kakao.model.Meta;

import java.util.ArrayList;

public class ResponseKeyword {
    @SerializedName("documents")
    private ArrayList<Document> documents;

    @SerializedName("meta")
    private Meta meta;

    public ArrayList<Document> getDocuments() {
        if (documents == null) {
            documents = new ArrayList<>();
        }

        return documents;
    }

    public void setDocuments(ArrayList<Document> documents) {
        this.documents = documents;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
