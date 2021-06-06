package com.example.witsonline;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    //Mocks for is last Item Displaying when Not empty
    RecyclerView recyclerViewTest_NotEmpty= Mockito.mock(RecyclerView.class);
    RecyclerView.Adapter adapter_NotEmpty=Mockito.mock((RecyclerView.Adapter.class));
    LinearLayoutManager linearLayoutManager_NotEmpty=Mockito.mock(LinearLayoutManager.class);

    //Mocks for is last Item Displaying when empty
    RecyclerView recyclerViewTest_Empty= Mockito.mock(RecyclerView.class);
    RecyclerView.Adapter adapter_Empty=Mockito.mock((RecyclerView.Adapter.class));


    //Mocks for ValidCourseCode
    TextInputLayout CourseCodeTextLayout_AlreadyExists=Mockito.mock(TextInputLayout.class);
    EditText CourseCodeEditText_AlreadyExists=Mockito.mock(EditText.class);
    TextInputLayout CourseCodeTextLayout_valid=Mockito.mock(TextInputLayout.class);
    EditText CourseCodeEditText_valid=Mockito.mock(EditText.class);
    TextInputLayout CourseCodeTextLayout_empty=Mockito.mock(TextInputLayout.class);
    EditText CourseCodeEditText_empty=Mockito.mock(EditText.class);

    //Mocks for validateCourseOutlineorTag when empty
    TextInputLayout validateCourseOutlineOrTagTextLayout_empty=Mockito.mock(TextInputLayout.class);
    EditText validateCourseOutlineOrTagEditText_empty=Mockito.mock(EditText.class);

    //Mocks for validateCourseOutlineorTag when not empty and valid
    TextInputLayout validateCourseOutlineOrTagTextLayout_valid=Mockito.mock(TextInputLayout.class);
    EditText validateCourseOutlineOrTagEditText_valid=Mockito.mock(EditText.class);

    //Mocks for validateCourseOutlineorTag when not empty and valid
    TextInputLayout validateCourseOutlineOrTagTextLayout_notvalid=Mockito.mock(TextInputLayout.class);
    EditText validateCourseOutlineOrTagEditText_notvalid=Mockito.mock(EditText.class);

    //Mocks for ValidateOutLineAndTag with Valid input
    TextInputLayout validateCourseOutlineAndTag_OutLineTextLayoutValid=Mockito.mock(TextInputLayout.class);
    TextView validateCourseOutLineAndTag_OutLineTextViewValid=Mockito.mock(TextView.class);
    TextInputLayout validateCourseOutlineAndTag_TagTextLayoutValid=Mockito.mock(TextInputLayout.class);
    TextView validateCourseOutLineAndTag_TagTextViewValid=Mockito.mock(TextView.class);

    //Mocks for ValidateOutLineAndTag with InValid input
    TextInputLayout validateCourseOutlineAndTag_OutLineTextLayoutInValid=Mockito.mock(TextInputLayout.class);
    TextView validateCourseOutLineAndTag_OutLineTextViewInValid=Mockito.mock(TextView.class);
    TextInputLayout validateCourseOutlineAndTag_TagTextLayoutInValid=Mockito.mock(TextInputLayout.class);
    TextView validateCourseOutLineAndTag_TagTextViewInValid=Mockito.mock(TextView.class);


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

        //set up for last item displaying when not empty
        Mockito.when(recyclerViewTest_NotEmpty.getAdapter()).thenReturn(adapter_NotEmpty);
        Mockito.when(adapter_NotEmpty.getItemCount()).thenReturn(1);
        Mockito.when(recyclerViewTest_NotEmpty.getLayoutManager()).thenReturn(linearLayoutManager_NotEmpty);

        //set up for last item displaying when not empty
        Mockito.when(recyclerViewTest_Empty.getAdapter()).thenReturn(adapter_Empty);
        Mockito.when(adapter_Empty.getItemCount()).thenReturn(0);

        //set up for validateCourseOutLineOrTag when empty
        Mockito.when(validateCourseOutlineOrTagTextLayout_empty.getEditText()).thenReturn(validateCourseOutlineOrTagEditText_empty);
        Mockito.when(validateCourseOutlineOrTagEditText_empty.getText()).thenReturn(new MockEditable(""));

        //set up for validateCourseOutLineOrTag when not empty and valid
        Mockito.when(validateCourseOutlineOrTagTextLayout_valid.getEditText()).thenReturn(validateCourseOutlineOrTagEditText_valid);
        Mockito.when(validateCourseOutlineOrTagEditText_valid.getText()).thenReturn(new MockEditable("test"));

        //set up for validateCourseOutLineOrTag when not empty and not valid
        Mockito.when(validateCourseOutlineOrTagTextLayout_notvalid.getEditText()).thenReturn(validateCourseOutlineOrTagEditText_notvalid);
        Mockito.when(validateCourseOutlineOrTagEditText_notvalid.getText()).thenReturn(new MockEditable("test"));

        //Set up for ValidateCourseOutlineAndTag with Valid input
        Mockito.when(validateCourseOutLineAndTag_OutLineTextViewValid.getText()).thenReturn(new MockEditable("0"));
        Mockito.when(validateCourseOutLineAndTag_TagTextViewValid.getText()).thenReturn(new MockEditable("0"));

        //Set up for ValidateCourseOutlineAndTag with InValid input
        Mockito.when(validateCourseOutLineAndTag_OutLineTextViewInValid.getText()).thenReturn(new MockEditable("1"));
        Mockito.when(validateCourseOutLineAndTag_TagTextViewInValid.getText()).thenReturn(new MockEditable("1"));

    }
    @Test
    public void onNavigationItemSelected() {
    }
    @Test
    public void validateCourseOutLineAndTag_valid(){
        CreateCourse temp = new CreateCourse();
        boolean output=temp.validateOutlineAndTag(validateCourseOutLineAndTag_OutLineTextViewValid,validateCourseOutlineAndTag_OutLineTextLayoutValid,validateCourseOutLineAndTag_TagTextViewValid,validateCourseOutlineAndTag_TagTextLayoutValid);
        assertEquals(true,output);
    }
    @Test
    public void validateCourseOutLineAndTag_Invalid(){
        CreateCourse temp = new CreateCourse();
        boolean output=temp.validateOutlineAndTag(validateCourseOutLineAndTag_OutLineTextViewInValid,validateCourseOutlineAndTag_OutLineTextLayoutInValid,validateCourseOutLineAndTag_TagTextViewInValid,validateCourseOutlineAndTag_TagTextLayoutInValid);
        assertEquals(false,output);
    }

    @Test
    public void validateCourseOutlineOrTagTest_empty(){
        CreateCourse temp = new CreateCourse();
        ArrayList<String> outline=new ArrayList<>();
        Boolean output=temp.validateCourseOutlineOrTag(validateCourseOutlineOrTagTextLayout_empty,outline);
        assertEquals(true,output);
    }

    @Test
    public void validateCourseOutlineOrTagTest_NonEmptyValid(){
        CreateCourse temp = new CreateCourse();
        ArrayList<String> outline=new ArrayList<>();
        String t="test";
        outline.add(t);
        Boolean output=temp.validateCourseOutlineOrTag(validateCourseOutlineOrTagTextLayout_valid,outline);
        assertEquals(true,output);
    }
    @Test
    public void validateCourseOutlineOrTagTest_NonEmptyNotValid(){
        CreateCourse temp = new CreateCourse();
        ArrayList<String> outline=new ArrayList<>();
        String t="test1";
        outline.add(t);
        Boolean output=temp.validateCourseOutlineOrTag(validateCourseOutlineOrTagTextLayout_notvalid,outline);
        assertEquals(false,output);
    }

    @Test
    public void isLastItemDisplaying_Notempty() {
        CreateCourse temp = new CreateCourse();
        Boolean output=temp.isLastItemDistplaying(recyclerViewTest_NotEmpty);
        assertEquals(true,output);
    }

    @Test
    public void isLastItemDisplaying_empty() {
        CreateCourse temp = new CreateCourse();
        Boolean output=temp.isLastItemDistplaying(recyclerViewTest_Empty);
        assertEquals(false,output);
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

    @Test
    public void TestGetCourseCodes(){
        CreateCourse course=Mockito.spy(new CreateCourse());
        String json="[{Course_Code:test}]";
        List<String> temp;
        course.getCourseCodes(json);
        temp=course.getCourseCodeList();
        assertNotNull(temp);
    }
    @Test
    public void TestaddFaculty(){
        CreateCourse course=Mockito.spy(new CreateCourse());
        String json="[{Faculty_ID:1,Faculty_Name:test}]";
        List<String> tempName;
        List<Integer> tempID;
        course.addFacultyNames(json);
        tempName=course.getFaculties();
        tempID=course.getFacultyIDs();
        assertNotNull(tempName);
        assertNotNull(tempID);
    }
    @Test
    public void TestConvert_sizeof1(){
        CreateCourse temp=new CreateCourse();
        ArrayList<String> list=new ArrayList<>();
        list.add("test");
        String output=temp.convert(list);
        assertEquals("test",output);
    }
    @Test
    public void TestConvert_sizegreaterthan1(){
        CreateCourse temp=new CreateCourse();
        ArrayList<String> list=new ArrayList<>();
        list.add("test1");
        list.add("test2");
        String output=temp.convert(list);
        assertEquals("test1;test2",output);
    }
    @Test
    public void TestConvert_sizegreaterthan2(){
        CreateCourse temp=new CreateCourse();
        ArrayList<String> list=new ArrayList<>();
        list.add("test1");
        list.add("test2");
        list.add("test3");
        String output=temp.convert(list);
        assertEquals("test1;test2;test3",output);
    }

}