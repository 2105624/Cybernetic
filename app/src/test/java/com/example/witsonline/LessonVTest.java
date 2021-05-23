package com.example.witsonline;

import org.junit.Test;

import static org.junit.Assert.*;

public class LessonVTest {

    @Test
    public void setLessonName() {
        LessonV temp=new LessonV();
        String expected="test";
        temp.setLessonName("test");
        String output=temp.getLessonName();
        assertEquals(expected,output);
    }

    @Test
    public void setLessonCourse() {
        LessonV temp=new LessonV();
        String expected="test";
        temp.setLessonCourse("test");
        String output=temp.getLessonCourse();
        assertEquals(expected,output);
    }

    @Test
    public void setLessonText() {
        LessonV temp=new LessonV();
        String expected="test";
        temp.setLessonText("test");
        String output=temp.getLessonText();
        assertEquals(expected,output);
    }

    @Test
    public void setLessonResource() {
        LessonV temp=new LessonV();
        String expected="test";
        temp.setLessonResource("test");
        String output=temp.getLessonResource();
        assertEquals(expected,output);
    }

    @Test
    public void setLessonUrl() {
        LessonV temp=new LessonV();
        String expected="test";
        temp.setLessonUrl("test");
        String output=temp.getLessonUrl();
        assertEquals(expected,output);
    }

    @Test
    public void getLessonName_WithNullValue() {
        LessonV temp=new LessonV();
        String expected =null;
        String output=temp.getLessonName();
        assertEquals(expected,output);
    }
    @Test
    public void getLessonName_WithNonNullValue() {
        LessonV temp=new LessonV();
        String expected ="test";
        temp.setLessonName("test");
        String output=temp.getLessonName();
        assertEquals(expected,output);
    }

    @Test
    public void getLessonCourse_WithNullValue() {
        LessonV temp=new LessonV();
        String expected =null;
        String output=temp.getLessonCourse();
        assertEquals(expected,output);
    }
    @Test
    public void getLessonCourse_WithNonNullValue() {
        LessonV temp=new LessonV();
        String expected ="test";
        temp.setLessonCourse("test");
        String output=temp.getLessonCourse();
        assertEquals(expected,output);
    }

    @Test
    public void getLessonText_WithNullValue() {
        LessonV temp=new LessonV();
        String expected =null;
        String output=temp.getLessonText();
        assertEquals(expected,output);
    }
    @Test
    public void getLessonText_WithNonNullValue() {
        LessonV temp=new LessonV();
        String expected ="test";
        temp.setLessonText("test");
        String output=temp.getLessonText();
        assertEquals(expected,output);
    }

    @Test
    public void getLessonResource_WithNullValue() {
        LessonV temp=new LessonV();
        String expected =null;
        String output=temp.getLessonResource();
        assertEquals(expected,output);
    }
    @Test
    public void getLessonResource_WithNonNullValue() {
        LessonV temp=new LessonV();
        String expected ="test";
        temp.setLessonResource("test");
        String output=temp.getLessonResource();
        assertEquals(expected,output);
    }

    @Test
    public void getLessonUrl_WithNullValue() {
        LessonV temp=new LessonV();
        String expected =null;
        String output=temp.getLessonUrl();
        assertEquals(expected,output);
    }
    @Test
    public void getLessonUrl_WithNonNullValue() {
        LessonV temp=new LessonV();
        String expected ="test";
        temp.setLessonUrl("test");
        String output=temp.getLessonUrl();
        assertEquals(expected,output);
    }


}