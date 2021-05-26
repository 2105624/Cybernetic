package com.example.witsonline;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

public class MD5HashTest {

    @Test
    public void md5_WithNonNullInput() {
        MD5Hash temp=new MD5Hash();
        String expected="098f6bcd4621d373cade4e832627b4f6";
        String output=temp.md5("test");
        assertEquals(expected,output);
    }

}