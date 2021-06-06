package com.example.witsonline;

import android.text.TextUtils;
import android.widget.EditText;
import com.google.android.material.textfield.TextInputLayout;
import android.util.Patterns;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;

import org.mockito.junit.MockitoJUnitRunner;


import java.io.IOException;
import java.util.ArrayList;


import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RegisterActivityTest {
    //isEmpty Mocks
     private final TextInputLayout isEmptyTextLayout_empty=Mockito.mock(TextInputLayout.class);
     private final EditText isEmptyEditText_empty=Mockito.mock(EditText.class);

     private final TextInputLayout isEmptyTextLayout_notempty=Mockito.mock(TextInputLayout.class);;
     private final EditText isEmptyEditText_notempty=Mockito.mock(EditText.class);
     //ValidatePassword Mocks
    private final TextInputLayout PasswordTextLayout_validInput=Mockito.mock(TextInputLayout.class);
    private final EditText PasswordEditText_validInput=Mockito.mock(EditText.class);

    private final TextInputLayout ConfirmPasswordTextLayout_validInput=Mockito.mock(TextInputLayout.class);
    private final EditText ConfirmPasswordEditText_validInput=Mockito.mock(EditText.class);

    private final TextInputLayout PasswordTextLayout_short=Mockito.mock(TextInputLayout.class);
    private final EditText PasswordEditText_short=Mockito.mock(EditText.class);

    private final TextInputLayout ConfirmPasswordTextLayout_short=Mockito.mock(TextInputLayout.class);
    private final EditText ConfirmPasswordEditText_short=Mockito.mock(EditText.class);

    private final TextInputLayout PasswordTextLayout_notMatching=Mockito.mock(TextInputLayout.class);
    private final EditText PasswordEditText_notMatching=Mockito.mock(EditText.class);

    private final TextInputLayout ConfirmPasswordTextLayout_notMatching=Mockito.mock(TextInputLayout.class);
    private final EditText ConfirmPasswordEditText_notMatching=Mockito.mock(EditText.class);

    private final TextInputLayout ConfirmPasswordTextLayout_emptyConfirm=Mockito.mock(TextInputLayout.class);
    private final EditText ConfirmPasswordEditText_emptyConfirm=Mockito.mock(EditText.class);

    //ValidateEmail Mocks
    private final TextInputLayout EmailTextLayout_validInput=Mockito.mock(TextInputLayout.class);
    private final EditText EmailEditText_validInput=Mockito.mock(EditText.class);

    private final TextInputLayout EmailTextLayout_EmptyInput=Mockito.mock(TextInputLayout.class);
    private final EditText EmailEditText_EmptyInput=Mockito.mock(EditText.class);

    private final TextInputLayout EmailTextLayout_BadPattern=Mockito.mock(TextInputLayout.class);
    private final EditText EmailEditText_BadPattern=Mockito.mock(EditText.class);

     //UserExists Mocks
     private final TextInputLayout ExistsTextLayout_validInstructor=Mockito.mock(TextInputLayout.class);
     private final EditText ExistsEditText_validInstructor=Mockito.mock(EditText.class);

     private final TextInputLayout ExistsTextLayout_InvalidInstructor=Mockito.mock(TextInputLayout.class);
     private final EditText ExistsEditText_InvalidInstructor=Mockito.mock(EditText.class);

     private final TextInputLayout ExistsTextLayout_validStudent=Mockito.mock(TextInputLayout.class);
     private final EditText ExistsEditText_validStudent=Mockito.mock(EditText.class);

     private final TextInputLayout ExistsTextLayout_InvalidStudent=Mockito.mock(TextInputLayout.class);
     private final EditText ExistsEditText_InvalidStudent=Mockito.mock(EditText.class);

     //ProcessInfo Mocks
     private final TextInputLayout UserTextLayout=Mockito.mock(TextInputLayout.class);
     private final EditText UserEditText=Mockito.mock(EditText.class);

     private final TextInputLayout NameTextLayout=Mockito.mock(TextInputLayout.class);
     private final EditText NameEditText=Mockito.mock(EditText.class);

     private final TextInputLayout SurNameTextLayout=Mockito.mock(TextInputLayout.class);
     private final EditText SurNameEditText=Mockito.mock(EditText.class);

     private final TextInputLayout EmailTextLayoutProcess=Mockito.mock(TextInputLayout.class);
     private final EditText EmailEditTextProcess=Mockito.mock(EditText.class);

     private final TextInputLayout PassTextLayout=Mockito.mock(TextInputLayout.class);
     private final EditText PassEditText=Mockito.mock(EditText.class);

     private final TextInputLayout ConfirmPassTextLayout=Mockito.mock(TextInputLayout.class);
     private final EditText ConfirmPassEditText=Mockito.mock(EditText.class);

     private final TextInputLayout ConfirmPassTextLayout_invalid=Mockito.mock(TextInputLayout.class);
     private final EditText ConfirmPassEditText_invalid=Mockito.mock(EditText.class);

     private final TextInputLayout UserTextLayout_invalid=Mockito.mock(TextInputLayout.class);
     private final EditText UserEditText_invalid=Mockito.mock(EditText.class);

     private final TextInputLayout NameTextLayout_invalid=Mockito.mock(TextInputLayout.class);
     private final EditText NameEditText_invalid=Mockito.mock(EditText.class);

     private final TextInputLayout SurNameTextLayout_invalid=Mockito.mock(TextInputLayout.class);
     private final EditText SurNameEditText_invalid=Mockito.mock(EditText.class);

     private final TextInputLayout PassTextLayout_invalid=Mockito.mock(TextInputLayout.class);
     private final EditText PassEditText_invalid=Mockito.mock(EditText.class);

     private final TextInputLayout EmailTextLayoutProcess_invalid=Mockito.mock(TextInputLayout.class);
     private final EditText EmailEditTextProcess_invalid=Mockito.mock(EditText.class);
     @Before
    public void init(){
        // setUP for isEmpty tests
        Mockito.when(isEmptyTextLayout_empty.getEditText()).thenReturn(isEmptyEditText_empty);
        Mockito.when(isEmptyTextLayout_notempty.getEditText()).thenReturn(isEmptyEditText_notempty);
        Mockito.when(isEmptyEditText_empty.getText()).thenReturn(new MockEditable(""));
        Mockito.when(isEmptyEditText_notempty.getText()).thenReturn(new MockEditable("123"));

        //setUP for Validate Password Tests
        Mockito.when(PasswordTextLayout_validInput.getEditText()).thenReturn(PasswordEditText_validInput);
        Mockito.when(PasswordEditText_validInput.getText()).thenReturn(new MockEditable("TestPassword"));
        Mockito.when(ConfirmPasswordTextLayout_validInput.getEditText()).thenReturn(ConfirmPasswordEditText_validInput);
        Mockito.when(ConfirmPasswordEditText_validInput.getText()).thenReturn(new MockEditable("TestPassword"));
        Mockito.when(PasswordTextLayout_short.getEditText()).thenReturn(PasswordEditText_short);
        Mockito.when(PasswordEditText_short.getText()).thenReturn(new MockEditable("Test"));
        Mockito.when(ConfirmPasswordTextLayout_short.getEditText()).thenReturn(ConfirmPasswordEditText_short);
        Mockito.when(ConfirmPasswordEditText_short.getText()).thenReturn(new MockEditable("Test"));
        Mockito.when(PasswordTextLayout_notMatching.getEditText()).thenReturn(PasswordEditText_notMatching);
        Mockito.when(PasswordEditText_notMatching.getText()).thenReturn(new MockEditable("TestPassword"));
        Mockito.when(ConfirmPasswordTextLayout_notMatching.getEditText()).thenReturn(ConfirmPasswordEditText_notMatching);
        Mockito.when(ConfirmPasswordEditText_notMatching.getText()).thenReturn(new MockEditable("TestNotMatching"));
        Mockito.when(ConfirmPasswordTextLayout_emptyConfirm.getEditText()).thenReturn(ConfirmPasswordEditText_emptyConfirm);
        Mockito.when(ConfirmPasswordEditText_emptyConfirm.getText()).thenReturn(new MockEditable(""));

        //setUp for ValidateEmail Tests
        Mockito.when(EmailTextLayout_validInput.getEditText()).thenReturn(EmailEditText_validInput);
        Mockito.when(EmailEditText_validInput.getText()).thenReturn(new MockEditable("Test@gmail.com"));
        Mockito.when(EmailTextLayout_EmptyInput.getEditText()).thenReturn(EmailEditText_EmptyInput);
        Mockito.when(EmailEditText_EmptyInput.getText()).thenReturn(new MockEditable(""));
        Mockito.when(EmailTextLayout_BadPattern.getEditText()).thenReturn(EmailEditText_BadPattern);
        Mockito.when(EmailEditText_BadPattern.getText()).thenReturn(new MockEditable("Test@@.gmail..com"));

        //setU for UserExists tests
        Mockito.when(ExistsTextLayout_validInstructor.getEditText()).thenReturn(ExistsEditText_validInstructor);
        Mockito.when(ExistsEditText_validInstructor.getText()).thenReturn(new MockEditable("test"));
        Mockito.when(ExistsTextLayout_InvalidInstructor.getEditText()).thenReturn(ExistsEditText_InvalidInstructor);
        Mockito.when(ExistsEditText_InvalidInstructor.getText()).thenReturn(new MockEditable("test2"));
        Mockito.when(ExistsTextLayout_validStudent.getEditText()).thenReturn(ExistsEditText_validStudent);
        Mockito.when(ExistsEditText_validStudent.getText()).thenReturn(new MockEditable("test"));
        Mockito.when(ExistsTextLayout_InvalidStudent.getEditText()).thenReturn(ExistsEditText_InvalidStudent);
        Mockito.when(ExistsEditText_InvalidStudent.getText()).thenReturn(new MockEditable("test2"));

        //setUp for ProcessInfo Tests
         Mockito.when(UserTextLayout.getEditText()).thenReturn(UserEditText);
         Mockito.when(UserEditText.getText()).thenReturn(new MockEditable("testUser"));

         Mockito.when(NameTextLayout.getEditText()).thenReturn(NameEditText);
         Mockito.when(NameEditText.getText()).thenReturn(new MockEditable("testName"));

         Mockito.when(SurNameTextLayout.getEditText()).thenReturn(SurNameEditText);
         Mockito.when(SurNameEditText.getText()).thenReturn(new MockEditable("testSurName"));

         Mockito.when(EmailTextLayoutProcess.getEditText()).thenReturn(EmailEditTextProcess);
         Mockito.when(EmailEditTextProcess.getText()).thenReturn(new MockEditable("test@gmail.com"));

         Mockito.when(PassTextLayout.getEditText()).thenReturn(PassEditText);
         Mockito.when(PassEditText.getText()).thenReturn(new MockEditable("Password"));

         Mockito.when(ConfirmPassTextLayout.getEditText()).thenReturn(ConfirmPassEditText);
         Mockito.when(ConfirmPassEditText.getText()).thenReturn(new MockEditable("Password"));

         Mockito.when(ConfirmPassTextLayout_invalid.getEditText()).thenReturn(ConfirmPassEditText_invalid);
         Mockito.when(ConfirmPassEditText_invalid.getText()).thenReturn(new MockEditable("Password1"));

         Mockito.when(UserTextLayout_invalid.getEditText()).thenReturn(UserEditText_invalid);
         Mockito.when(UserEditText_invalid.getText()).thenReturn(new MockEditable(""));

         Mockito.when(NameTextLayout_invalid.getEditText()).thenReturn(NameEditText_invalid);
         Mockito.when(NameEditText_invalid.getText()).thenReturn(new MockEditable(""));

         Mockito.when(SurNameTextLayout_invalid.getEditText()).thenReturn(SurNameEditText_invalid);
         Mockito.when(SurNameEditText_invalid.getText()).thenReturn(new MockEditable(""));

         Mockito.when(PassTextLayout_invalid.getEditText()).thenReturn(PassEditText_invalid);
         Mockito.when(PassEditText_invalid.getText()).thenReturn(new MockEditable(""));

         Mockito.when(EmailTextLayoutProcess_invalid.getEditText()).thenReturn(EmailEditTextProcess_invalid);
         Mockito.when(EmailEditTextProcess_invalid.getText()).thenReturn(new MockEditable(""));


    }
    @Test
    public void isEmpty_withEmptyInput() {
        RegisterActivity temp = new RegisterActivity();
        Boolean output=temp.isEmpty(isEmptyTextLayout_empty);
        assertEquals(true,output);
    }
    @Test
    public void isEmpty_withNonEmptyInput() {
        RegisterActivity temp = new RegisterActivity();
        Boolean output=temp.isEmpty(isEmptyTextLayout_notempty);
        assertEquals(false,output);
    }
    @Test
    public void userExists_validInstructor() {
        RegisterActivity temp = new RegisterActivity();
        ArrayList<String> instructor=new ArrayList<String>();
        instructor.add("test2");
        ArrayList<String> student=new ArrayList<String>();
        student.add("test2");
        Boolean check =true;
        Boolean output = temp.userExists(ExistsTextLayout_validInstructor,instructor,student,check);
        assertEquals(false,output);
    }
    @Test
    public void userExists_InvalidInstructor() {
        RegisterActivity temp = new RegisterActivity();
        ArrayList<String> instructor=new ArrayList<String>();
        instructor.add("test2");
        ArrayList<String> student=new ArrayList<String>();
        student.add("test2");
        Boolean check =true;
        Boolean output = temp.userExists(ExistsTextLayout_InvalidInstructor,instructor,student,check);
        assertEquals(true,output);
    }
    @Test
    public void userExists_validStudent() {
        RegisterActivity temp = new RegisterActivity();
        ArrayList<String> instructor=new ArrayList<String>();
        instructor.add("test2");
        ArrayList<String> student=new ArrayList<String>();
        student.add("test2");
        Boolean check =false;
        Boolean output = temp.userExists(ExistsTextLayout_validStudent,instructor,student,check);
        assertEquals(false,output);
    }
    @Test
    public void userExists_InvalidStudent() {
        RegisterActivity temp = new RegisterActivity();
        ArrayList<String> instructor=new ArrayList<String>();
        instructor.add("test2");
        ArrayList<String> student=new ArrayList<String>();
        student.add("test2");
        Boolean check =false;
        Boolean output = temp.userExists(ExistsTextLayout_InvalidStudent,instructor,student,check);
        assertEquals(true,output);
    }
    @Test
    public void validateEmail_withValidInput() {
        RegisterActivity temp = new RegisterActivity();
        Boolean output=temp.validateEmail(EmailTextLayout_validInput);
        assertEquals(true,output);
    }
    @Test
    public void validateEmail_withEmptyInput() {
        RegisterActivity temp = new RegisterActivity();
        Boolean output=temp.validateEmail(EmailTextLayout_EmptyInput);
        assertEquals(false,output);
    }
    @Test
    public void validateEmail_withNonMatchingPattern() {
        RegisterActivity temp = new RegisterActivity();
        Boolean output=temp.validateEmail(EmailTextLayout_BadPattern);
        assertEquals(false,output);
    }
    @Test
    public void validatePassword_withValidInput() {
        RegisterActivity temp = new RegisterActivity();
        Boolean output=temp.validatePassword(PasswordTextLayout_validInput,ConfirmPasswordTextLayout_validInput);
        assertEquals(true,output);
    }
    @Test
    public void validatePassword_withShortInput() {
        RegisterActivity temp = new RegisterActivity();
        Boolean output=temp.validatePassword(PasswordTextLayout_short,ConfirmPasswordTextLayout_short);
        assertEquals(false,output);
    }
    @Test
    public void validatePassword_withNotMatchingInput() {
        RegisterActivity temp = new RegisterActivity();
        Boolean output=temp.validatePassword(PasswordTextLayout_notMatching,ConfirmPasswordTextLayout_notMatching);
        assertEquals(false,output);
    }
    @Test
    public void validatePassword_emptyConfirm() {
        RegisterActivity temp = new RegisterActivity();
        Boolean output=temp.validatePassword(PasswordTextLayout_notMatching,ConfirmPasswordTextLayout_emptyConfirm);
        assertEquals(false,output);
    }
    @Test
    public void isDigits() {
     }
    @Test
    public void processInfo_valid() throws IOException {
         RegisterActivity temp = new RegisterActivity();
         ArrayList<String> instructor=new ArrayList<String>();
         instructor.add("test2");
         ArrayList<String> student= new ArrayList<>();
         student.add("test2");
         Boolean check =true;
         String php = "test.php";
         String userType="Instructor";
         Boolean output=temp.processInfo(UserTextLayout,NameTextLayout,SurNameTextLayout,EmailTextLayoutProcess,PassTextLayout,ConfirmPassTextLayout,php,userType,instructor,student,check);
         assertEquals(true,output);
     }
    @Test
    public void processInfo_EmptyUser() throws IOException {
        RegisterActivity temp = new RegisterActivity();
        ArrayList<String> instructor=new ArrayList<String>();
        instructor.add("test2");
        ArrayList<String> student= new ArrayList<>();
        student.add("test2");
        Boolean check =true;
        String php = "test.php";
        String userType="Instructor";
        Boolean output=temp.processInfo(UserTextLayout_invalid,NameTextLayout,SurNameTextLayout,EmailTextLayoutProcess,PassTextLayout,ConfirmPassTextLayout,php,userType,instructor,student,check);
        assertEquals(true,output);
    }
    @Test
    public void processInfo_EmptyName() throws IOException {
        RegisterActivity temp = new RegisterActivity();
        ArrayList<String> instructor=new ArrayList<String>();
        instructor.add("test2");
        ArrayList<String> student= new ArrayList<>();
        student.add("test2");
        Boolean check =true;
        String php = "test.php";
        String userType="Instructor";
        Boolean output=temp.processInfo(UserTextLayout,NameTextLayout_invalid,SurNameTextLayout,EmailTextLayoutProcess,PassTextLayout,ConfirmPassTextLayout,php,userType,instructor,student,check);
        assertEquals(true,output);
    }
    @Test
    public void processInfo_EmptySurName() throws IOException {
        RegisterActivity temp = new RegisterActivity();
        ArrayList<String> instructor=new ArrayList<String>();
        instructor.add("test2");
        ArrayList<String> student= new ArrayList<>();
        student.add("test2");
        Boolean check =true;
        String php = "test.php";
        String userType="Instructor";
        Boolean output=temp.processInfo(UserTextLayout,NameTextLayout,SurNameTextLayout_invalid,EmailTextLayoutProcess,PassTextLayout,ConfirmPassTextLayout,php,userType,instructor,student,check);
        assertEquals(true,output);
    }
    @Test
    public void processInfo_EmptyPass() throws IOException {
        RegisterActivity temp = new RegisterActivity();
        ArrayList<String> instructor=new ArrayList<String>();
        instructor.add("test2");
        ArrayList<String> student= new ArrayList<>();
        student.add("test2");
        Boolean check =true;
        String php = "test.php";
        String userType="Instructor";
        Boolean output=temp.processInfo(UserTextLayout,NameTextLayout,SurNameTextLayout,EmailTextLayoutProcess,PassTextLayout_invalid,ConfirmPassTextLayout,php,userType,instructor,student,check);
        assertEquals(false,output);
    }
    @Test
    public void processInfo_UserExists() throws IOException {
        RegisterActivity temp = new RegisterActivity();
        ArrayList<String> instructor=new ArrayList<String>();
        instructor.add("testUser");
        ArrayList<String> student= new ArrayList<>();
        student.add("test2");
        Boolean check =true;
        String php = "test.php";
        String userType="Instructor";
        Boolean output=temp.processInfo(UserTextLayout,NameTextLayout,SurNameTextLayout,EmailTextLayoutProcess,PassTextLayout,ConfirmPassTextLayout,php,userType,instructor,student,check);
        assertEquals(true,output);
    }
    @Test
    public void processInfo_UnMatchingPasswords() throws IOException {
        RegisterActivity temp = new RegisterActivity();
        ArrayList<String> instructor=new ArrayList<String>();
        instructor.add("test2");
        ArrayList<String> student= new ArrayList<>();
        student.add("test2");
        Boolean check =true;
        String php = "test.php";
        String userType="Instructor";
        Boolean output=temp.processInfo(UserTextLayout,NameTextLayout,SurNameTextLayout,EmailTextLayoutProcess,PassTextLayout,ConfirmPassTextLayout_invalid,php,userType,instructor,student,check);
        assertEquals(false,output);
    }
    @Test
    public void processInfo_InvalidEmail() throws IOException {
        RegisterActivity temp = new RegisterActivity();
        ArrayList<String> instructor=new ArrayList<String>();
        instructor.add("test2");
        ArrayList<String> student= new ArrayList<>();
        student.add("test2");
        Boolean check =true;
        String php = "test.php";
        String userType="Instructor";
        Boolean output=temp.processInfo(UserTextLayout,NameTextLayout,SurNameTextLayout,EmailTextLayoutProcess_invalid,PassTextLayout,ConfirmPassTextLayout_invalid,php,userType,instructor,student,check);
        assertEquals(false,output);
    }

}