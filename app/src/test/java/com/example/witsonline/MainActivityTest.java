package com.example.witsonline;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class MainActivityTest {

    @Test
    public void JSON_USERNAME() {
        MainActivity main=new MainActivity();
        String json="[{FNAME:test,LNAME:test}]";
        String output = main.JSON_USERNAME(json);
        assertEquals("test test",output);
    }

    @Test
    public void JSON_USER_NUM() {
        MainActivity main=new MainActivity();
        String json="[{USER_NUM:test}]";
        String output = main.JSON_USER_NUM(json);
        assertEquals("test",output);
    }

    @Test
    public void JSON_COURSE_NAME() {
        MainActivity main=new MainActivity();
        String json="[{COURSE_CODE:test}]";
        String output = main.JSON_COURSE_CODE(json);
        assertEquals("test",output);
    }

    @Test
    public void JSON_COURSE_CODE() {
        MainActivity main=new MainActivity();
        String json="[{COURSE_NAME:test}]";
        String output = main.JSON_COURSE_NAME(json);
        assertEquals("test",output);
    }


}