package com.example.witsonline;

import org.junit.Test;

import static org.junit.Assert.*;

public class CourseVTest {

    @Test
    public void setCourseName() {
        CourseV temp=new CourseV();
        String expected="test";
        temp.setCourseName("test");
        String output=temp.getCourseName();
        assertEquals(expected,output);
    }

    @Test
    public void setCourseDescription() {
        CourseV temp=new CourseV();
        String expected="test";
        temp.setCourseDescription("test");
        String output=temp.getCourseDescription();
        assertEquals(expected,output);
    }

    @Test
    public void setCourseInstructor() {
        CourseV temp=new CourseV();
        String expected="test";
        temp.setCourseInstructor("test");
        String output=temp.getCourseInstructor();
        assertEquals(expected,output);
    }

    @Test
    public void setCourseCode() {
        CourseV temp=new CourseV();
        String expected="test";
        temp.setCourseCode("test");
        String output=temp.getCourseCode();
        assertEquals(expected,output);
    }

    @Test
    public void setCourseRating() {
        CourseV temp=new CourseV();
        String expected="test";
        temp.setCourseRating("test");
        String output=temp.getCourseRating();
        assertEquals(expected,output);
    }

    @Test
    public void setCourseOutline() {
        CourseV temp=new CourseV();
        String expected="test";
        temp.setCourseOutline("test");
        String output=temp.getCourseOutline();
        assertEquals(expected,output);
    }

    @Test
    public void setImageUrl() {
        CourseV temp=new CourseV();
        String expected="test";
        temp.setImageUrl("test");
        String output=temp.getImageUrl();
        assertEquals(expected,output);
    }

    @Test
    public void getCourseNameWithNullValue() {
        CourseV temp=new CourseV();
        String expected=null;
        String output=temp.getCourseName();
        assertEquals(expected,output);
    }
    @Test
    public void getCourseNameWithNonNullValue() {
        CourseV temp=new CourseV();
        String expected="test";
        temp.setCourseName("test");
        String output=temp.getCourseName();
        assertEquals(expected,output);
    }

    @Test
    public void getCourseDescriptionWithNullValue() {
        CourseV temp=new CourseV();
        String expected=null;
        String output=temp.getCourseDescription();
        assertEquals(expected,output);
    }
    @Test
    public void getCourseDescriptionWithNonNullValue() {
        CourseV temp=new CourseV();
        String expected="test";
        temp.setCourseDescription("test");
        String output=temp.getCourseDescription();
        assertEquals(expected,output);
    }

    @Test
    public void getCourseInstructorWithNullValue() {
        CourseV temp=new CourseV();
        String expected=null;
        String output=temp.getCourseInstructor();
        assertEquals(expected,output);
    }
    @Test
    public void getCourseInstructorWithNonNullValue() {
        CourseV temp=new CourseV();
        String expected="test";
        temp.setCourseInstructor("test");
        String output=temp.getCourseInstructor();
        assertEquals(expected,output);
    }

    @Test
    public void getCourseCodeWithNullValue() {
        CourseV temp=new CourseV();
        String expected=null;
        String output=temp.getCourseCode();
        assertEquals(expected,output);
    }
    @Test
    public void getCourseCodeWithNonNullValue() {
        CourseV temp=new CourseV();
        String expected="test";
        temp.setCourseCode("test");
        String output=temp.getCourseCode();
        assertEquals(expected,output);
    }

    @Test
    public void getImageUrlWithNullValue() {
        CourseV temp=new CourseV();
        String expected=null;
        String output=temp.getImageUrl();
        assertEquals(expected,output);
    }
    @Test
    public void getImageUrlWithNonNullValue() {
        CourseV temp=new CourseV();
        String expected="test";
        temp.setImageUrl("test");
        String output=temp.getImageUrl();
        assertEquals(expected,output);
    }

    @Test
    public void getCourseRatingWithNullValue() {
        CourseV temp=new CourseV();
        String expected="0.0";
        String output=temp.getCourseRating();
        assertEquals(expected,output);
    }
    @Test
    public void getCourseRatingWithNonNullValue() {
        CourseV temp=new CourseV();
        String expected="test";
        temp.setCourseRating("test");
        String output=temp.getCourseRating();
        assertEquals(expected,output);
    }

    @Test
    public void getCourseOutlineWithNullValue() {
        CourseV temp=new CourseV();
        String expected=null;
        String output=temp.getCourseOutline();
        assertEquals(expected,output);
    }
    @Test
    public void getCourseOutlineWithNonNullValue() {
        CourseV temp=new CourseV();
        String expected="test";
        temp.setCourseOutline("test");
        String output=temp.getCourseOutline();
        assertEquals(expected,output);
    }
}