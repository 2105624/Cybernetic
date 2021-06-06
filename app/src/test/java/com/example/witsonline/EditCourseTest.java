package com.example.witsonline;

import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(MockitoJUnitRunner.class)
public class EditCourseTest {
    //Mocks for isEmpty test when input is empty
    TextInputLayout isEmptyTextLayout_empty= Mockito.mock(TextInputLayout.class);
     EditText isEmptyEditText_empty=Mockito.mock(EditText.class);;

    //Mocks for validateCourseOutlineorTag when empty
    TextInputLayout validateCourseOutlineOrTagTextLayout_empty=Mockito.mock(TextInputLayout.class);
    EditText validateCourseOutlineOrTagEditText_empty=Mockito.mock(EditText.class);

    //Mocks for validateCourseOutlineorTag when not empty and valid
    TextInputLayout validateCourseOutlineOrTagTextLayout_valid=Mockito.mock(TextInputLayout.class);
    EditText validateCourseOutlineOrTagEditText_valid=Mockito.mock(EditText.class);

    //Mocks for validateCourseOutlineorTag when not empty and valid
    TextInputLayout validateCourseOutlineOrTagTextLayout_notvalid=Mockito.mock(TextInputLayout.class);
    EditText validateCourseOutlineOrTagEditText_notvalid=Mockito.mock(EditText.class);

     //Mocks for isEmpty test when in put is not empty
    TextInputLayout isEmptyTextLayout_notempty=Mockito.mock(TextInputLayout.class);;
    EditText isEmptyEditText_notempty=Mockito.mock(EditText.class);;
    @Before
    public void init(){

        // setUP for isEmpty tests with empty input
        Mockito.when(isEmptyTextLayout_empty.getEditText()).thenReturn(isEmptyEditText_empty);
        Mockito.when(isEmptyEditText_empty.getText()).thenReturn(new MockEditable(""));

        //Setup for isEmpty tests with non empty input
        Mockito.when(isEmptyTextLayout_notempty.getEditText()).thenReturn(isEmptyEditText_notempty);
        Mockito.when(isEmptyEditText_notempty.getText()).thenReturn(new MockEditable("123"));

        //set up for validateCourseOutLineOrTag when empty
        Mockito.when(validateCourseOutlineOrTagTextLayout_empty.getEditText()).thenReturn(validateCourseOutlineOrTagEditText_empty);
        Mockito.when(validateCourseOutlineOrTagEditText_empty.getText()).thenReturn(new MockEditable(""));

        //set up for validateCourseOutLineOrTag when not empty and valid
        Mockito.when(validateCourseOutlineOrTagTextLayout_valid.getEditText()).thenReturn(validateCourseOutlineOrTagEditText_valid);
        Mockito.when(validateCourseOutlineOrTagEditText_valid.getText()).thenReturn(new MockEditable("test"));

        //set up for validateCourseOutLineOrTag when not empty and not valid
        Mockito.when(validateCourseOutlineOrTagTextLayout_notvalid.getEditText()).thenReturn(validateCourseOutlineOrTagEditText_notvalid);
        Mockito.when(validateCourseOutlineOrTagEditText_notvalid.getText()).thenReturn(new MockEditable("test"));

    }
    @Test
    public void noFacultySelected() {
    }

    @Test
    public void validateOutlineAndTag() {
    }

    @Test
    public void TestConvert_sizeof1(){
        EditCourse temp=new EditCourse();
        ArrayList<String> list=new ArrayList<>();
        list.add("test");
        String output=temp.convert(list);
        assertEquals("test",output);
    }
    @Test
    public void TestConvert_sizegreaterthan1(){
        EditCourse temp=new EditCourse();
        ArrayList<String> list=new ArrayList<>();
        list.add("test1");
        list.add("test2");
        String output=temp.convert(list);
        assertEquals("test1;test2",output);
    }
    @Test
    public void TestConvert_sizegreaterthan2(){
        EditCourse temp=new EditCourse();
        ArrayList<String> list=new ArrayList<>();
        list.add("test1");
        list.add("test2");
        list.add("test3");
        String output=temp.convert(list);
        assertEquals("test1;test2;test3",output);
    }

    @Test
    public void isEmpty_withEmptyInput() {
        EditCourse temp = new EditCourse();
        Boolean output=temp.isEmpty(isEmptyTextLayout_empty);
        assertEquals(true,output);
    }
    @Test
    public void isEmpty_withNonEmptyInput() {
        EditCourse temp = new EditCourse();
        Boolean output=temp.isEmpty(isEmptyTextLayout_notempty);
        assertEquals(false,output);
    }

    @Test
    public void validateCourseOutlineOrTagTest_empty(){
        EditCourse temp = new EditCourse();
        ArrayList<String> outline=new ArrayList<>();
        Boolean output=temp.validateCourseOutlineOrTag(validateCourseOutlineOrTagTextLayout_empty,outline);
        assertEquals(true,output);
    }

    @Test
    public void validateCourseOutlineOrTagTest_NonEmptyValid(){
        EditCourse temp = new EditCourse();
        ArrayList<String> outline=new ArrayList<>();
        String t="test";
        outline.add(t);
        Boolean output=temp.validateCourseOutlineOrTag(validateCourseOutlineOrTagTextLayout_valid,outline);
        assertEquals(true,output);
    }
    @Test
    public void validateCourseOutlineOrTagTest_NonEmptyNotValid(){
        EditCourse temp = new EditCourse();
        ArrayList<String> outline=new ArrayList<>();
        String t="test1";
        outline.add(t);
        Boolean output=temp.validateCourseOutlineOrTag(validateCourseOutlineOrTagTextLayout_notvalid,outline);
        assertEquals(false,output);
    }

    @Test
    public void TestGetCourseCodes(){
        EditCourse course = new EditCourse();
        String json="[{Course_Code:test}]";
        List<String> temp;
        course.getCourseCodes(json);
        temp=course.getCourseCodeList();
        assertNotNull(temp);
    }
}