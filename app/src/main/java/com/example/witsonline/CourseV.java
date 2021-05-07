package com.example.witsonline;

public class CourseV {
    private String strCourseName;
    private String strCourseDescription;
    private String strCourseInstructor;
    private String strCourseCode;
    private String strCourseRating;
    private String strCourseOutline;
    private String strImageUrl;


    public void setCourseName(String courseName){
        this.strCourseName = courseName;
    }
    public void setCourseDescription(String courseDescription){ this.strCourseDescription = courseDescription;}
    public void setCourseInstructor(String courseInstructor){ this.strCourseInstructor = courseInstructor;}
    public void setCourseCode(String courseCode){this.strCourseCode = courseCode;}
    public void setCourseRating(String courseRating){ this.strCourseRating = courseRating;}
    public void setCourseOutline(String courseOutline){this.strCourseOutline = courseOutline;};
    public void setImageUrl(String imageUrl){this.strImageUrl = imageUrl;};


    public String getCourseName(){
        return strCourseName;
    }
    public String getCourseDescription(){
        return strCourseDescription;
    }
    public String getCourseInstructor(){
        return strCourseInstructor;
    }
    public String getCourseCode(){ return strCourseCode; }
    public String getImageUrl(){ return strImageUrl; }
    public String getCourseRating(){

        if(!strCourseRating.equals("null")) {
            return strCourseRating;
        }
        else{
            return "0.0";
        }
    }
    public String getCourseOutline(){return strCourseOutline;}
}
