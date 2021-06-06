package com.example.witsonline;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.textfield.TextInputLayout;
import com.google.common.base.Verify;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;
@RunWith(MockitoJUnitRunner.class)


public class LoginActivityTest {
    //Mocks for ValidateInstructorUserName
    private final TextInputLayout InstructorTextLayout_userDoesNotExist=Mockito.mock(TextInputLayout.class);
    private final EditText InstructorEditText_userDoesNotExist=Mockito.mock(EditText.class);
    private final TextInputLayout InstructorTextLayout_empty=Mockito.mock(TextInputLayout.class);
    private final EditText InstructorEditText_empty=Mockito.mock(EditText.class);
    private final TextInputLayout InstructorTextLayout_tooLong=Mockito.mock(TextInputLayout.class);
    private final EditText InstructorEditText_tooLong=Mockito.mock(EditText.class);
    private final TextInputLayout InstructorTextLayout_valid=Mockito.mock(TextInputLayout.class);
    private final EditText InstructorEditText_valid=Mockito.mock(EditText.class);

    //Mocks for ValidateInstructorPassword
    private final TextInputLayout InstructorPasswordTextLayout_empty=Mockito.mock(TextInputLayout.class);
    private final EditText InstructorPasswordEditText_empty=Mockito.mock(EditText.class);
    private final TextInputLayout InstructorPasswordTextLayout_valid=Mockito.mock(TextInputLayout.class);
    private final EditText InstructorPasswordEditText_valid=Mockito.mock(EditText.class);
    private final TextInputLayout InstructorPasswordTextLayout_wrong=Mockito.mock(TextInputLayout.class);
    private final EditText InstructorPasswordEditText_wrong=Mockito.mock(EditText.class);

    //Mocks for ValidateStudentUserName
    private final TextInputLayout StudentTextLayout_userDoesNotExist=Mockito.mock(TextInputLayout.class);
    private final EditText StudentEditText_userDoesNotExist=Mockito.mock(EditText.class);
    private final TextInputLayout StudentTextLayout_empty=Mockito.mock(TextInputLayout.class);
    private final EditText StudentEditText_empty=Mockito.mock(EditText.class);
    private final TextInputLayout StudentTextLayout_tooLong=Mockito.mock(TextInputLayout.class);
    private final EditText StudentEditText_tooLong=Mockito.mock(EditText.class);
    private final TextInputLayout StudentTextLayout_valid=Mockito.mock(TextInputLayout.class);
    private final EditText StudentEditText_valid=Mockito.mock(EditText.class);

    //Mocks for ValidateStudentPassword
    private final TextInputLayout StudentPasswordTextLayout_empty=Mockito.mock(TextInputLayout.class);
    private final EditText StudentPasswordEditText_empty=Mockito.mock(EditText.class);
    private final TextInputLayout StudentPasswordTextLayout_valid=Mockito.mock(TextInputLayout.class);
    private final EditText StudentPasswordEditText_valid=Mockito.mock(EditText.class);
    private final TextInputLayout StudentPasswordTextLayout_wrong=Mockito.mock(TextInputLayout.class);
    private final EditText StudentPasswordEditText_wrong=Mockito.mock(EditText.class);

    //Mock for Oncreate





    @Before
    public void init(){
        //SetUP for ValidateInstructorUserName
        Mockito.when(InstructorTextLayout_userDoesNotExist.getEditText()).thenReturn(InstructorEditText_userDoesNotExist);
        Mockito.when(InstructorEditText_userDoesNotExist.getText()).thenReturn(new MockEditable("test"));
        Mockito.when(InstructorTextLayout_empty.getEditText()).thenReturn(InstructorEditText_empty);
        Mockito.when(InstructorEditText_empty.getText()).thenReturn(new MockEditable(""));
        Mockito.when(InstructorTextLayout_tooLong.getEditText()).thenReturn(InstructorEditText_tooLong);
        Mockito.when(InstructorEditText_tooLong.getText()).thenReturn(new MockEditable("012345678901234567890"));
        Mockito.when(InstructorTextLayout_valid.getEditText()).thenReturn(InstructorEditText_valid);
        Mockito.when(InstructorEditText_valid.getText()).thenReturn(new MockEditable("test"));

        //SetUP for ValidateInstructorPassword
        Mockito.when(InstructorPasswordTextLayout_empty.getEditText()).thenReturn(InstructorPasswordEditText_empty);
        Mockito.when(InstructorPasswordEditText_empty.getText()).thenReturn(new MockEditable(""));
        Mockito.when(InstructorPasswordTextLayout_valid.getEditText()).thenReturn(InstructorPasswordEditText_valid);
        Mockito.when(InstructorPasswordEditText_valid.getText()).thenReturn(new MockEditable("password"));
        Mockito.when(InstructorPasswordTextLayout_wrong.getEditText()).thenReturn(InstructorPasswordEditText_wrong);
        Mockito.when(InstructorPasswordEditText_wrong.getText()).thenReturn(new MockEditable("password2"));

        //SetUP for ValidateStudentUserName
        Mockito.when(StudentTextLayout_userDoesNotExist.getEditText()).thenReturn(StudentEditText_userDoesNotExist);
        Mockito.when(StudentEditText_userDoesNotExist.getText()).thenReturn(new MockEditable("test"));
        Mockito.when(StudentTextLayout_empty.getEditText()).thenReturn(StudentEditText_empty);
        Mockito.when(StudentEditText_empty.getText()).thenReturn(new MockEditable(""));
        Mockito.when(StudentTextLayout_tooLong.getEditText()).thenReturn(StudentEditText_tooLong);
        Mockito.when(StudentEditText_tooLong.getText()).thenReturn(new MockEditable("012345678901234567890"));
        Mockito.when(StudentTextLayout_valid.getEditText()).thenReturn(StudentEditText_valid);
        Mockito.when(StudentEditText_valid.getText()).thenReturn(new MockEditable("test"));

        //setUP for ValidateStudentPassword
        Mockito.when(StudentPasswordTextLayout_empty.getEditText()).thenReturn(StudentPasswordEditText_empty);
        Mockito.when(StudentPasswordEditText_empty.getText()).thenReturn(new MockEditable(""));
        Mockito.when(StudentPasswordTextLayout_valid.getEditText()).thenReturn(StudentPasswordEditText_valid);
        Mockito.when(StudentPasswordEditText_valid.getText()).thenReturn(new MockEditable("password"));
        Mockito.when(StudentPasswordTextLayout_wrong.getEditText()).thenReturn(StudentPasswordEditText_wrong);
        Mockito.when(StudentPasswordEditText_wrong.getText()).thenReturn(new MockEditable("password2"));

        //setup





    }
    @Test
    public void TestGetSrudentLogin(){
        LoginActivity login = Mockito.spy(new LoginActivity());
        String json="[{Student_Number:test,Student_Password:test}]";
        ArrayList<ArrayList<String>> temp;
        login.getStudentLogin(json);
        temp=login.getStudent();
        assertNotNull(temp);
    }

    @Test
    public void TestGetInstructorLogin(){
        LoginActivity login = Mockito.spy(new LoginActivity());
        String json="[{Instructor_Username:test,Instructor_Password:test}]";
        ArrayList<ArrayList<String>> temp;
        login.getInstructorLogin(json);
        temp=login.getStudent();
        assertNotNull(temp);
    }


    @Test
    public void validateInstructorUsername_valid() {
        LoginActivity temp = new LoginActivity();
        ArrayList<ArrayList<String>> instructor = new ArrayList<>();
        ArrayList<String> tempList=new ArrayList<>();
        tempList.add("test");
        tempList.add("5f4dcc3b5aa765d61d8327deb882cf99");
        instructor.add(tempList);
        Boolean output=temp.validateInstructorUsername(InstructorTextLayout_valid,instructor);
        assertEquals(true,output);
    }
    @Test
    public void validateInstructorUsername_empty() {
        LoginActivity temp = new LoginActivity();
        ArrayList<ArrayList<String>> instructor = new ArrayList<>();
        ArrayList<String> tempList=new ArrayList<>();
        tempList.add("test");
        tempList.add("5f4dcc3b5aa765d61d8327deb882cf99");
        instructor.add(tempList);
        Boolean output=temp.validateInstructorUsername(InstructorTextLayout_empty,instructor);
        assertEquals(false,output);
    }
    @Test
    public void validateInstructorUsername_userExists() {
        LoginActivity temp = new LoginActivity();
        ArrayList<ArrayList<String>> instructor = new ArrayList<>();
        ArrayList<String> tempList=new ArrayList<>();
        tempList.add("test2");
        tempList.add("5f4dcc3b5aa765d61d8327deb882cf99");
        instructor.add(tempList);
        Boolean output=temp.validateInstructorUsername(InstructorTextLayout_userDoesNotExist,instructor);
        assertEquals(false,output);
    }
    @Test
    public void validateInstructorUsername_tooLong() {
        LoginActivity temp = new LoginActivity();
        ArrayList<ArrayList<String>> instructor = new ArrayList<>();
        ArrayList<String> tempList=new ArrayList<>();
        tempList.add("test");
        tempList.add("5f4dcc3b5aa765d61d8327deb882cf99");
        instructor.add(tempList);
        Boolean output=temp.validateInstructorUsername(InstructorTextLayout_tooLong,instructor);
        assertEquals(false,output);
    }
    @Test
    public void validateInstructorPassword_valid() {
        LoginActivity temp = new LoginActivity();
        ArrayList<ArrayList<String>> instructor = new ArrayList<>();
        ArrayList<String> tempList=new ArrayList<>();
        tempList.add("test");
        tempList.add("5f4dcc3b5aa765d61d8327deb882cf99");
        instructor.add(tempList);
        Boolean output=temp.validateInstructorPassword(InstructorPasswordTextLayout_valid,instructor,0);
        assertEquals(true,output);
    }
    @Test
    public void validateInstructorPassword_empty() {
        LoginActivity temp = new LoginActivity();
        ArrayList<ArrayList<String>> instructor = new ArrayList<>();
        ArrayList<String> tempList=new ArrayList<>();
        tempList.add("test");
        tempList.add("5f4dcc3b5aa765d61d8327deb882cf99");
        instructor.add(tempList);
        Boolean output=temp.validateInstructorPassword(InstructorPasswordTextLayout_empty,instructor,0);
        assertEquals(false,output);
    }
    @Test
    public void validateInstructorPassword_WrongPassword() {
        LoginActivity temp = new LoginActivity();
        ArrayList<ArrayList<String>> instructor = new ArrayList<>();
        ArrayList<String> tempList=new ArrayList<>();
        tempList.add("test");
        tempList.add("5f4dcc3b5aa765d61d8327deb882cf99");
        instructor.add(tempList);
        Boolean output=temp.validateInstructorPassword(InstructorPasswordTextLayout_wrong,instructor,0);
        assertEquals(false,output);
    }
    @Test
    public void validateInstructorPassword_wrongIndex() {
        LoginActivity temp = new LoginActivity();
        ArrayList<ArrayList<String>> instructor = new ArrayList<>();
        ArrayList<String> tempList=new ArrayList<>();
        tempList.add("test");
        tempList.add("5f4dcc3b5aa765d61d8327deb882cf99");
        instructor.add(tempList);
        Boolean output=temp.validateInstructorPassword(InstructorPasswordTextLayout_valid,instructor,-1);
        assertEquals(false,output);
    }
    @Test
    public void validateStudentUsername_valid() {
        LoginActivity temp = new LoginActivity();
        ArrayList<ArrayList<String>> instructor = new ArrayList<>();
        ArrayList<String> tempList=new ArrayList<>();
        tempList.add("test");
        tempList.add("5f4dcc3b5aa765d61d8327deb882cf99");
        instructor.add(tempList);
        Boolean output=temp.validateStudentUsername(StudentTextLayout_valid,instructor);
        assertEquals(true,output);
    }
    @Test
    public void validateStudentUsername_empty() {
        LoginActivity temp = new LoginActivity();
        ArrayList<ArrayList<String>> instructor = new ArrayList<>();
        ArrayList<String> tempList=new ArrayList<>();
        tempList.add("test");
        tempList.add("5f4dcc3b5aa765d61d8327deb882cf99");
        instructor.add(tempList);
        Boolean output=temp.validateStudentUsername(StudentTextLayout_empty,instructor);
        assertEquals(false,output);
    }
    @Test
    public void validateStudentUsername_userExists() {
        LoginActivity temp = new LoginActivity();
        ArrayList<ArrayList<String>> instructor = new ArrayList<>();
        ArrayList<String> tempList=new ArrayList<>();
        tempList.add("test2");
        tempList.add("5f4dcc3b5aa765d61d8327deb882cf99");
        instructor.add(tempList);
        Boolean output=temp.validateStudentUsername(StudentTextLayout_userDoesNotExist,instructor);
        assertEquals(false,output);
    }
    @Test
    public void validateStudentUsername_tooLong() {
        LoginActivity temp = new LoginActivity();
        ArrayList<ArrayList<String>> instructor = new ArrayList<>();
        ArrayList<String> tempList=new ArrayList<>();
        tempList.add("test");
        tempList.add("5f4dcc3b5aa765d61d8327deb882cf99");
        instructor.add(tempList);
        Boolean output=temp.validateStudentUsername(StudentTextLayout_tooLong,instructor);
        assertEquals(false,output);
    }
    @Test
    public void validateStudentPassword_valid() {
        LoginActivity temp = new LoginActivity();
        ArrayList<ArrayList<String>> instructor = new ArrayList<>();
        ArrayList<String> tempList=new ArrayList<>();
        tempList.add("test");
        tempList.add("5f4dcc3b5aa765d61d8327deb882cf99");
        instructor.add(tempList);
        Boolean output=temp.validateStudentPassword(StudentPasswordTextLayout_valid,instructor,0);
        assertEquals(true,output);
    }
    @Test
    public void validateStudentPassword_empty() {
        LoginActivity temp = new LoginActivity();
        ArrayList<ArrayList<String>> instructor = new ArrayList<>();
        ArrayList<String> tempList=new ArrayList<>();
        tempList.add("test");
        tempList.add("5f4dcc3b5aa765d61d8327deb882cf99");
        instructor.add(tempList);
        Boolean output=temp.validateStudentPassword(StudentPasswordTextLayout_empty,instructor,0);
        assertEquals(false,output);
    }
    @Test
    public void validateStudentPassword_WrongPassword() {
        LoginActivity temp = new LoginActivity();
        ArrayList<ArrayList<String>> instructor = new ArrayList<>();
        ArrayList<String> tempList=new ArrayList<>();
        tempList.add("test");
        tempList.add("5f4dcc3b5aa765d61d8327deb882cf99");
        instructor.add(tempList);
        Boolean output=temp.validateStudentPassword(StudentPasswordTextLayout_wrong,instructor,0);
        assertEquals(false,output);
    }
    @Test
    public void validateStudentPassword_wrongIndex() {
        LoginActivity temp = new LoginActivity();
        ArrayList<ArrayList<String>> instructor = new ArrayList<>();
        ArrayList<String> tempList=new ArrayList<>();
        tempList.add("test");
        tempList.add("5f4dcc3b5aa765d61d8327deb882cf99");
        instructor.add(tempList);
        Boolean output=temp.validateStudentPassword(StudentPasswordTextLayout_valid,instructor,-1);
        assertEquals(false,output);
    }


    @Test
    public void testonChekedChanged(){
        LoginActivity loginActivity=Mockito.spy(new LoginActivity());
        View mockView=Mockito.mock(View.class);

    }
    @Test
    public void testButton()throws Exception{
     //   LoginActivity temp=new LoginActivity();
        //View view=loginActivity.findViewById(R.layout.activity_login);
       // assertNotNull(view);

    }
}