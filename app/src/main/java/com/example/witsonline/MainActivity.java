package com.example.witsonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import okhttp3.*;
import okio.Utf8;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //page will navigate to Login page on load
        Intent i = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(i);
        finish();
    }


    //------------//
    // NAVIGATION //					//these might be useful, just added in anyways
    //------------//

//    public void toLoginScreen(View v){
//        setContentView(R.layout.login_screen);
//    }
//    public void toCourse(View v){
//	Button theone=(Button)v;
//        if (!(theone.getText().toString().equals("") || theone.getText().toString().equals(null))) {
//            COURSE.CODE = theone.getText().toString();
//	      *grab course info from db and apply to page
//            setContentView(R.layout.course);
//    }

//the function below is for setting up course buttons, unfortunately only static for now
//it's using buttons, depending on how xml file is set up you may need to change. Commenting out for now
//    public void toDashboard(View v){
//        setContentView(R.layout.dashboard);
//        TextView naem=findViewById(R.id.textView1);	//Anri, this is for the welcome section
//        naem.setText(USER.USERNAME);
//        USER.COURSES=new ArrayList<String>();
//        USER.temp="1";					//might be useful later
//
//        Button[] Buttonlist=new Button[6];
//        Buttonlist[0]=findViewById(R.id.course0);
//        Buttonlist[1]=findViewById(R.id.course1);
//        Buttonlist[2]=findViewById(R.id.course2);
//        Buttonlist[3]=findViewById(R.id.course3);
//        Buttonlist[4]=findViewById(R.id.course4);
//        Buttonlist[5]=findViewById(R.id.course5);
//
//        if (USER.STUDENT){
//            settakings(v);
//            try {
//                Thread.sleep(5000);			//this is temporary, will try and see if there is a way to know once it has the data, for now I've just put a 5sec delay
//            }
//            catch(InterruptedException ex){
//                Thread.currentThread().interrupt();
//            }
//            thels(COURSE.temp);
//        }
//        else {
//            setmakings(v);
//            try {
//                Thread.sleep(5000);			//see above comment
//            }
//            catch(InterruptedException ex){
//                Thread.currentThread().interrupt();
//            }
//            thels(COURSE.temp);
//        }
//
//        if (USER.COURSES.size()<6){
//            for (int i=0; i<USER.COURSES.size(); i++){
//                Buttonlist[i].setText(USER.COURSES.get(i));
//            }
//        }
//        else{
//            for (int i=0; i<6; i++){
//                Buttonlist[i].setText(USER.COURSES.get(i));
//            }
//        }
//    }
//
//    //next function for a button for list of courses page change
//    public void next(View v){
//        Button[] Buttonlist=new Button[6];
//        Buttonlist[0]=findViewById(R.id.course0);
//        Buttonlist[1]=findViewById(R.id.course1);
//        Buttonlist[2]=findViewById(R.id.course2);
//        Buttonlist[3]=findViewById(R.id.course3);
//        Buttonlist[4]=findViewById(R.id.course4);
//        Buttonlist[5]=findViewById(R.id.course5);
//
//        int numma=Integer.parseInt(USER.temp);				//told you it might be useful
//        if (USER.COURSES.size()>(numma*6)){
//            if ((USER.COURSES.size()-(numma*6))<6){
//                for (int i=6*numma; i<USER.COURSES.size(); i++){
//                    Buttonlist[i-(6*numma)].setText(USER.COURSES.get(i));
//                }
//                for (int i=USER.COURSES.size(); i<6*(numma+1); i++){
//                    Buttonlist[i-(6*numma)].setText(null);
//                }
//            }
//            else{
//                for (int i=0; i<6; i++){
//                    Buttonlist[i].setText(USER.COURSES.get(i+(6*numma)));
//                }
//            }
//            USER.temp=Integer.toString(numma+1);			//USER.temp change
//        }
//
////@Anri, here's a thing if you want to use it
////        TextView page=findViewById(R.id.textView11);
////        String purge="Page "+USER.temp;
////        page.setText(purge);
//
//    }
//    //previous function for a button for list of courses page change
//    public void previous(View v){
//        Button[] Buttonlist=new Button[6];
//        Buttonlist[0]=findViewById(R.id.course0);
//        Buttonlist[1]=findViewById(R.id.course1);
//        Buttonlist[2]=findViewById(R.id.course2);
//        Buttonlist[3]=findViewById(R.id.course3);
//        Buttonlist[4]=findViewById(R.id.course4);
//        Buttonlist[5]=findViewById(R.id.course5);
//
//        int numma=Integer.parseInt(USER.temp);
//        if (numma>1){
//            for (int i=0; i<6; i++){
//                Buttonlist[i].setText(USER.COURSES.get(i+(6*(numma-2))));
//            }
//            numma=numma-1;
//            USER.temp=Integer.toString(numma);
//
////@Anri, here it is again
////            TextView page=findViewById(R.id.textView11);
////            String purge="Page "+USER.temp;
////            page.setText(purge);
//
//        }
//    }


    //------------------//
    // JSON CONVERSIONS //
    //------------------//

    //Users//

    public String JSON_USERNAME(String json){
        String USERNAME = "";
        String FNAME="";
        String LNAME="";
        try {
            JSONArray all = new JSONArray(json);
            for (int i=0; i<all.length(); i++){
                JSONObject item=all.getJSONObject(i);
                FNAME = FNAME + item.getString("FNAME");
                LNAME = LNAME +item.getString("LNAME");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        USERNAME=FNAME+" "+LNAME;
        return(USERNAME);
    }

    public String JSON_USER_NUM(String json){
        String USERT = "";
        try {
            JSONArray all = new JSONArray(json);
            for (int i=0; i<all.length(); i++){
                JSONObject item=all.getJSONObject(i);
                USERT = USERT + item.getString("USER_NUM");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return(USERT);
    }

    //Courses//

    public String JSON_COURSE_NAME(String json){
        String COURSENAME = "";
        try {
            JSONArray all = new JSONArray(json);
            for (int i=0; i<all.length(); i++){
                JSONObject item=all.getJSONObject(i);
                COURSENAME = COURSENAME + item.getString("COURSE_NAME");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return(COURSENAME);
    }

    public String JSON_COURSE_CODE(String json){
        String CODE = "";
        try {
            JSONArray all = new JSONArray(json);
            for (int i=0; i<all.length(); i++){
                JSONObject item=all.getJSONObject(i);
                CODE = CODE + item.getString("COURSE_CODE");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return(CODE);
    }

    //Sets Of Courses//

    public void thels(String json){
        try {
            JSONArray all = new JSONArray(json);
            for (int i=0; i<all.length(); i++){
                JSONObject item=all.getJSONObject(i);
                USER.COURSES.add(item.getString("COURSE_CODE"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //-------------------//
    // ACCOUNT FUNCTIONS //
    //-------------------//

    //User Info//

//this is here to set up info, will need to be done in login though
//    public void getinf(android.view.View v) throws IOException {

//@Login people, you've done the log in, if you can change your db query to SELECT * from the relevant table this should set up local variables we need
//                if (response.isSuccessful()) {
//                    USER.temp=(response.body().string());
//                } else {
//                    throw new IOException("Unexpected code " + response);
//                }
//insert function for password checking before getting info for security reasons
//you will need to fix this please, not sure what you've used
//	if (*the selection is student*){
//        	USER.STUDENT=true;
//		USER.USER_NUM=JSON_USER_NUM(USER.temp);
//	}
//	else{
//		USER.STUDENT=false;
//		USER.USER_NUM=JSON_INSTRUCTOR_NUM(USER.temp);
//	}
//	USER.USERNAME=JSON_USERNAME(USER.temp);
//    }

    //Course(s) Info//

    public void settakings(View v){

        OkHttpClient client = new OkHttpClient();

        RequestBody addinf=new FormBody.Builder()
                .add("unum", (USER.USER_NUM))
                .build();
        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2105624/lstakers.php")
                .post(addinf)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException exc) {
                exc.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (response.isSuccessful()) {
                    COURSE.temp=(response.body().string());
                } else {
                    throw new IOException("Unexpected code " + response);
                }
            }
        });
    }

    public void setmakings(View v){

        OkHttpClient client = new OkHttpClient();

        RequestBody addinf=new FormBody.Builder()
                .add("unum", (USER.USER_NUM))
                .build();
        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2090273/lsmakers.php")
                .post(addinf)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException exc) {
                exc.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (response.isSuccessful()) {
                    COURSE.temp=(response.body().string());
                } else {
                    throw new IOException("Unexpected code " + response);
                }
            }
        });
    }

}