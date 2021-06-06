package com.example.witsonline;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;
@RunWith(MockitoJUnitRunner.class)
public class MyCoursesTest {
    //Mocks for is last Item Displaying when Not empty
    RecyclerView recyclerViewTest_NotEmpty= Mockito.mock(RecyclerView.class);
    RecyclerView.Adapter adapter_NotEmpty=Mockito.mock((RecyclerView.Adapter.class));
    LinearLayoutManager linearLayoutManager_NotEmpty=Mockito.mock(LinearLayoutManager.class);

    //Mocks for is last Item Displaying when empty
    RecyclerView recyclerViewTest_Empty= Mockito.mock(RecyclerView.class);
    RecyclerView.Adapter adapter_Empty=Mockito.mock((RecyclerView.Adapter.class));

    @Before
    public void init(){
        //set up for last item displaying when not empty
        Mockito.when(recyclerViewTest_NotEmpty.getAdapter()).thenReturn(adapter_NotEmpty);
        Mockito.when(adapter_NotEmpty.getItemCount()).thenReturn(1);
        Mockito.when(recyclerViewTest_NotEmpty.getLayoutManager()).thenReturn(linearLayoutManager_NotEmpty);


        //set up for last item displaying when not empty
        Mockito.when(recyclerViewTest_Empty.getAdapter()).thenReturn(adapter_Empty);
        Mockito.when(adapter_Empty.getItemCount()).thenReturn(0);

    }
    @Test
    public void isLastItemDisplaying_NotEmpty() {
        MyCourses temp = new MyCourses();
        Boolean output=temp.isLastItemDistplaying(recyclerViewTest_NotEmpty);
        assertEquals(true,output);
    }

    @Test
    public void isLastItemDisplaying_empty() {
        MyCourses temp = new MyCourses();
        Boolean output=temp.isLastItemDistplaying(recyclerViewTest_Empty);
        assertEquals(false,output);
    }



}