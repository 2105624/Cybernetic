package com.example.witsonline;

public class LessonV {
    private String strLessonName;
    private String strLessonCourse;
    private String strLessonText;
    private String strLessonResource;
    private String strLessonUrl;


    public void setLessonName(String lessonName){
        this.strLessonName = lessonName;
    }
    public void setLessonCourse(String lessonCourse){ this.strLessonCourse = lessonCourse;}
    public void setLessonText(String lessonText){ this.strLessonText = lessonText;}
    public void setLessonResource(String lessonResource){this.strLessonResource = lessonResource;}
    public void setLessonUrl(String lessonUrl){this.strLessonUrl = lessonUrl;};


    public String getLessonName(){ return strLessonName; }
    public String getLessonCourse(){
        return strLessonCourse;
    }
    public String getLessonText(){
        return strLessonText;
    }
    public String getLessonResource(){ return strLessonResource; }
    public String getLessonUrl(){ return strLessonUrl; }
}
