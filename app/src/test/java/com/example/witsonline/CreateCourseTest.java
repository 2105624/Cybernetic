package com.example.witsonline;

import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.apache.tools.ant.taskdefs.MacroDef;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(MockitoJUnitRunner.class)
public class CreateCourseTest {

    //Mocks for isEmpty
    TextInputLayout EmptyTextLayout_empty=Mockito.mock(TextInputLayout.class);
    EditText EmptyEditText_empty=Mockito.mock(EditText.class);
    TextInputLayout EmptyTextLayout_Nonempty=Mockito.mock(TextInputLayout.class);
    EditText EmptyEditText_Nonempty=Mockito.mock(EditText.class);

    //Mocks for noFaculty selected


    //Mocks for ValidCourseCode
    TextInputLayout CourseCodeTextLayout_AlreadyExists=Mockito.mock(TextInputLayout.class);
    EditText CourseCodeEditText_AlreadyExists=Mockito.mock(EditText.class);
    TextInputLayout CourseCodeTextLayout_valid=Mockito.mock(TextInputLayout.class);
    EditText CourseCodeEditText_valid=Mockito.mock(EditText.class);
    TextInputLayout CourseCodeTextLayout_empty=Mockito.mock(TextInputLayout.class);
    EditText CourseCodeEditText_empty=Mockito.mock(EditText.class);


    @Before
 public void init(){
     //setUp for isEmpty test
    Mockito.when(EmptyTextLayout_empty.getEditText()).thenReturn(EmptyEditText_empty);
    Mockito.when(EmptyEditText_empty.getText()).thenReturn(new MockEditable(""));
    Mockito.when(EmptyTextLayout_Nonempty.getEditText()).thenReturn(EmptyEditText_Nonempty);
    Mockito.when(EmptyEditText_Nonempty.getText()).thenReturn(new MockEditable("123"));

    //setUp for ValidCourseCode test
        Mockito.when(CourseCodeTextLayout_AlreadyExists.getEditText()).thenReturn(CourseCodeEditText_AlreadyExists);
        Mockito.when(CourseCodeEditText_AlreadyExists.getText()).thenReturn(new MockEditable("123"));
        Mockito.when(CourseCodeTextLayout_valid.getEditText()).thenReturn(CourseCodeEditText_valid);
        Mockito.when(CourseCodeEditText_valid.getText()).thenReturn(new MockEditable("321"));
        Mockito.when(CourseCodeTextLayout_empty.getEditText()).thenReturn(CourseCodeEditText_empty);
        Mockito.when(CourseCodeEditText_empty.getText()).thenReturn(new MockEditable(""));
    }
 @Test
    public void onNavigationItemSelected() {
    }

    @Test
    public void isEmpty_withEmptyInput() {
        CreateCourse temp = new CreateCourse();
        Boolean output=temp.isEmpty(EmptyTextLayout_empty);
        assertEquals(true,output);
    }
    @Test
    public void isEmpty_withNonEmptyInput() {
        CreateCourse temp = new CreateCourse();
        Boolean output=temp.isEmpty(EmptyTextLayout_Nonempty);
        assertEquals(false,output);
    }

    @Test
    public void noFacultySelected_nonSelected() {

    }
    @Test
    public void noFacultySelected_Selected() {
        CreateCourse temp = new CreateCourse();
        Boolean check=true;
        assertEquals(false,temp.noFacultySelected(check));
    }

    @Test
    public void validCourseCode_AlreadyExists() {
        List<String> tempList=new ArrayList<>();
        tempList.add("123");
        CreateCourse temp = new CreateCourse();
        Boolean output= temp.validCourseCode(CourseCodeTextLayout_AlreadyExists,tempList);
        assertEquals(false,output);
    }
    @Test
    public void validCourseCode_Valid() {
        List<String> tempList=new ArrayList<>();
        tempList.add("123");
        CreateCourse temp = new CreateCourse();
        Boolean output= temp.validCourseCode(CourseCodeTextLayout_valid,tempList);
        assertEquals(true,output);
    }
    @Test
    public void validCourseCode_empty() {
        List<String> tempList=new ArrayList<>();
        tempList.add("123");
        CreateCourse temp = new CreateCourse();
        Boolean output= temp.validCourseCode(CourseCodeTextLayout_empty,tempList);
        assertEquals(false,output);
    }

    @Test
    public void getPath() {
    }
}