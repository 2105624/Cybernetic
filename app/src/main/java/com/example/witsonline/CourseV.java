package com.example.witsonline;

public class CourseV {
    private String strCourseName;
    private String strCourseDescription;
    private String strCourseInstructor;


    public void setCourseName(String courseName){
        this.strCourseName = courseName;
    }
    public void setCourseDescription(String courseDescription){ this.strCourseDescription = courseDescription;}
    public void setCourseInstructor(String courseInstructor){ this.strCourseInstructor = courseInstructor;}

    public String getCourseName(){
        return strCourseName;
    }
    public String getCourseDescription(){
        return strCourseDescription;
    }
    public String getCourseInstructor(){
        return strCourseInstructor;
    }
}
