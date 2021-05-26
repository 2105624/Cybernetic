package com.example.witsonline;

import org.junit.Test;

import static org.junit.Assert.*;

public class TagVTest {

    @Test
    public void setTag_notNull() {
        TagV temp =new TagV();
        String expected="test";
        temp.setTag("test");
        assertEquals(expected,temp.getTag());
    }
    @Test
    public void setTag_Null() {
        TagV temp =new TagV();
        String expected="test";
        temp.setTag(null);
        assertEquals(null,temp.getTag());
    }

    @Test
    public void getTag() {
        TagV temp=new TagV();
        String expected="test";
        temp.setTag("test");
        assertEquals(expected,temp.getTag());
    }
}