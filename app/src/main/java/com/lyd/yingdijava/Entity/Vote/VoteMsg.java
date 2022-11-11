package com.lyd.yingdijava.Entity.Vote;

public class VoteMsg {
    private String viewPoint1;

    private String viewPoint_line1;//这个一般是一个数字带一个百分号 eg:25%

    private String viewPoint2;

    private String viewPoint_line2;

    public String getViewPoint1() {
        return viewPoint1;
    }

    public void setViewPoint1(String viewPoint1) {
        this.viewPoint1 = viewPoint1;
    }

    public String getViewPoint_line1() {
        return viewPoint_line1;
    }

    public void setViewPoint_line1(String viewPoint_line1) {
        this.viewPoint_line1 = viewPoint_line1;
    }

    public String getViewPoint2() {
        return viewPoint2;
    }

    public void setViewPoint2(String viewPoint2) {
        this.viewPoint2 = viewPoint2;
    }

    public String getViewPoint_line2() {
        return viewPoint_line2;
    }

    public void setViewPoint_line2(String viewPoint_line2) {
        this.viewPoint_line2 = viewPoint_line2;
    }

    @Override
    public String toString() {
        return "VoteMsg{" +
                "vote_text_1='" + viewPoint1 + '\'' +
                ", vote_line_1='" + viewPoint_line1 + '\'' +
                ", vote_text_2='" + viewPoint2 + '\'' +
                ", vote_line_2='" + viewPoint_line2 + '\'' +
                '}';
    }
}
