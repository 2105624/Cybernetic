package com.example.witsonline;

public class ReviewV {
    private String strStudentFName;
    private String strStudentLName;
    private String strReviewDescription;
    private String strReviewRating;

    public void setStudentFName(String studentFName){
        this.strStudentFName = studentFName;
    }
    public void setStudentLName(String studentLName){
        this.strStudentLName = studentLName;
    }
    public void setReviewDescription(String reviewDescription){ this.strReviewDescription = reviewDescription;}
    public void setReviewRating(String reviewRating){ this.strReviewRating = reviewRating;}

    public String getStudentFName(){
        return strStudentFName;
    }
    public String getStudentLName(){
        return strStudentLName;
    }
    public String getReviewDescription(){
        return strReviewDescription;
    }
    public String getReviewRating(){
        if(strReviewRating!=null) {
            return strReviewRating;
        }
        else{
            return "0.0";
        }
    }

}
