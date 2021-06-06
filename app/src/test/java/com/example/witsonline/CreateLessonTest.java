package com.example.witsonline;

import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
@RunWith(MockitoJUnitRunner.class)
public class CreateLessonTest {
    //Mocks for isEmpty with empty input
    TextInputLayout isEmptyTextLayout_empty= Mockito.mock(TextInputLayout.class);
    EditText isEmptyEditText_empty=Mockito.mock(EditText.class);

    //Mocks for isEmpty with non impty input
    TextInputLayout isEmptyTextLayout_notempty=Mockito.mock(TextInputLayout.class);
    EditText isEmptyEditText_notempty=Mockito.mock(EditText.class);

    //Mocks for isYoutubeURl with valid input
    TextInputLayout isYouTubeURLTextLayout_valid=Mockito.mock(TextInputLayout.class);
    EditText isYouTubeURLEditText_valid=Mockito.mock(EditText.class);

    //Mocks for isYoutubeURl with empty input
    TextInputLayout isYouTubeURLTextLayout_empty=Mockito.mock(TextInputLayout.class);
    EditText isYouTubeURLEditText_empty=Mockito.mock(EditText.class);

    //Mocks for isYoutubeURl with invalid input
    TextInputLayout isYouTubeURLTextLayout_invalid=Mockito.mock(TextInputLayout.class);
    EditText isYouTubeURLEditText_invalid=Mockito.mock(EditText.class);

    @Before
    public void init(){
        // setUP for isEmpty tests
        Mockito.when(isEmptyTextLayout_empty.getEditText()).thenReturn(isEmptyEditText_empty);
        Mockito.when(isEmptyEditText_empty.getText()).thenReturn(new MockEditable(""));

        //setup for isEMpty tets with empty input
        Mockito.when(isEmptyTextLayout_notempty.getEditText()).thenReturn(isEmptyEditText_notempty);
        Mockito.when(isEmptyEditText_notempty.getText()).thenReturn(new MockEditable("123"));

        //setup for is YouTubeUrl with valid input.
        Mockito.when(isYouTubeURLTextLayout_valid.getEditText()).thenReturn(isYouTubeURLEditText_valid);
        Mockito.when(isYouTubeURLEditText_valid.getText()).thenReturn(new MockEditable("https://www.youtube.com/watch?v=eHNXg8CiP5E"));

        //setup for is YouTubeUrl with empty input.
        Mockito.when(isYouTubeURLTextLayout_empty.getEditText()).thenReturn(isYouTubeURLEditText_empty);
        Mockito.when(isYouTubeURLEditText_empty.getText()).thenReturn(new MockEditable(""));

        //setup for is YouTubeUrl with invalid input.
        Mockito.when(isYouTubeURLTextLayout_invalid.getEditText()).thenReturn(isYouTubeURLEditText_invalid);
        Mockito.when(isYouTubeURLEditText_invalid.getText()).thenReturn(new MockEditable("https://www.test.test"));

    }

    @Test
    public void isEmpty_withEmptyInput() {
        CreateLesson temp= new CreateLesson();
        Boolean output=temp.isEmpty(isEmptyTextLayout_empty);
        assertEquals(true,output);
    }
    @Test
    public void isEmpty_withNonEmptyInput() {
        CreateLesson temp= new CreateLesson();
        Boolean output=temp.isEmpty(isEmptyTextLayout_notempty);
        assertEquals(false,output);
    }

    @Test
    public void isYoutubeUrl_valid() {
        CreateLesson temp = new CreateLesson();
        Boolean output=temp.isYoutubeUrl(isYouTubeURLTextLayout_valid);
        assertEquals(true,output);
    }

    @Test
    public void isYoutubeUrl_empty() {
        CreateLesson temp = new CreateLesson();
        Boolean output=temp.isYoutubeUrl(isYouTubeURLTextLayout_empty);
        assertEquals(false,output);
    }

    @Test
    public void isYoutubeUrl_invalid() {
        CreateLesson temp = new CreateLesson();
        Boolean output=temp.isYoutubeUrl(isYouTubeURLTextLayout_invalid);
        assertEquals(false,output);
    }
}