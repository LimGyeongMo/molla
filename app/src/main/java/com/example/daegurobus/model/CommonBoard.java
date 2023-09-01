package com.example.daegurobus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonBoard {
    public static final String BOARD_NOTICE = "1";
    public static final String BOARD_EVENT = "3";
    public static final String BOARD_POPUP_EVENT = "5";

    public static final String MOVE_BRAND_LIST = "11";  // 브랜드목록으로 이동하는 기능 때문에 추가

    @SerializedName("seq")
    @Expose
    private String seq;

    @SerializedName("boardGbn")
    @Expose
    private String boardGbn;

    @SerializedName("frDate")
    @Expose
    private String frDate;

    @SerializedName("toDate")
    @Expose
    private String toDate;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("contents")
    @Expose
    private String contents;

    @SerializedName("url1")
    @Expose
    private String url1;    // 베너 이미지 링크

    @SerializedName("url2")
    @Expose
    private String url2;    // 상세페이지 링크

    @SerializedName("url3")
    @Expose
    private String url3;

    @SerializedName("extUrlYn")
    @Expose
    private String extUrlYn;

    @SerializedName("insDate")
    @Expose
    private String insDate;

    @SerializedName("readYn")
    @Expose
    private String readYn;

    @SerializedName("moveProject")
    @Expose
    private String moveProject;

    @SerializedName("moveBoardGbn")
    @Expose
    private String moveBoardGbn;

    @SerializedName("moveSeq")
    @Expose
    private String moveSeq;

    public CommonBoard(String seq) {
        this.seq = seq;
    }

    public String getSeq() {
        if (seq == null) {
            seq = "";
        }

        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getBoardGbn() {
        if (boardGbn == null) {
            boardGbn = "";
        }

        return boardGbn;
    }

    public void setBoardGbn(String boardGbn) {
        this.boardGbn = boardGbn;
    }

    public String getFrDate() {
        if (frDate == null) {
            frDate = "";
        }

        return frDate;
    }

    public void setFrDate(String frDate) {
        this.frDate = frDate;
    }

    public String getToDate() {
        if (toDate == null) {
            toDate = "";
        }

        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getTitle() {
        if (title == null) {
            title = "";
        }

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        if (contents == null) {
            contents = "";
        }

        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getUrl1() {
        if (url1 == null) {
            url1 = "";
        }

         return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getUrl2() {
        if (url2 == null) {
            url2 = "";
        }

        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public String getUrl3() {
        if (url3 == null) {
            url3 = "";
        }

        return url3;
    }

    public void setUrl3(String url3) {
        this.url3 = url3;
    }

    public String getExtUrlYn() {
        if (extUrlYn == null) {
            extUrlYn = "";
        }

        return extUrlYn;
    }

    public void setExtUrlYn(String extUrlYn) {
        this.extUrlYn = extUrlYn;
    }

    public String getInsDate() {
        if (insDate == null) {
            insDate = "";
        }

        return insDate;
    }

    public void setInsDate(String insDate) {
        this.insDate = insDate;
    }

    public String getReadYn() {
        if (readYn == null) {
            readYn = "";
        }

        return readYn;
    }

    public void setReadYn(String readYn) {
        this.readYn = readYn;
    }

    public String getMoveProject() {
        if (moveProject == null) {
            moveProject = "";
        }

        return moveProject;
    }

    public void setMoveProject(String moveProject) {
        this.moveProject = moveProject;
    }

    public String getMoveBoardGbn() {
        if (moveBoardGbn == null) {
            moveBoardGbn = "";
        }

        return moveBoardGbn;
    }

    public void setMoveBoardGbn(String moveBoardGbn) {
        this.moveBoardGbn = moveBoardGbn;
    }

    public String getMoveSeq() {
        if (moveSeq == null) {
            moveSeq = "";
        }

        return moveSeq;
    }

    public void setMoveSeq(String moveSeq) {
        this.moveSeq = moveSeq;
    }
}