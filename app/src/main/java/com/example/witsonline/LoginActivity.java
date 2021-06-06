package com.example.witsonline;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity {

    private RadioGroup rgLogin;
    private TextInputLayout user;
    private TextInputLayout password;
    private TextInputEditText userText;
    private Button btnLogin;
    private RadioButton rbStudent;
    private RadioButton rbInstructor;
    ArrayList<ArrayList<String> >  student = new ArrayList<>();
    private ArrayList<String> studentNumbers = new ArrayList<>();
    private ArrayList<String> instructorUsernames = new ArrayList<>();
    private ArrayList<ArrayList<String> > instructor = new ArrayList<>();

    String URL = "https://lamp.ms.wits.ac.za/home/s2105624/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rbStudent = findViewById(R.id.student);
        rbInstructor = findViewById(R.id.instructor);
        userText = (TextInputEditText)findViewById(R.id.loginText);
        userText.setHint("Student Number");
        rgLogin = findViewById((R.id.rgLogin));

        rgLogin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.student){
                    userText.setHint("Student Number");
                    userText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    userText.setText("");
                    user.setCounterMaxLength(10);
                    userText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
                }else{
                    userText.setHint("Username");
                    userText.setInputType(InputType.TYPE_CLASS_TEXT);
                    user.setCounterMaxLength(20);
                    userText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
                }
            }
        });


        TextView signUp = findViewById(R.id.textView);

        String text = "New user? Register";

        SpannableString ss = new SpannableString(text);

        ClickableSpan cs = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                i.putStringArrayListExtra("students", studentNumbers);
                i.putStringArrayListExtra("instructors", instructorUsernames);
                startActivity(i);
                finish();
            }
        };
        ss.setSpan(cs, 10, 18, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        signUp.setText(ss);
        signUp.setMovementMethod(LinkMovementMethod.getInstance());


        PHPRequest req = new PHPRequest("https://lamp.ms.wits.ac.za/home/s2105624/");

        req.doRequest(LoginActivity.this, "studentLogin",
                new ResponseHandler() {
                    @Override
                    public void processResponse(String response) {
                        getStudentLogin(response);
                    }
                });
        req.doRequest(LoginActivity.this, "instructorLogin",
                new ResponseHandler() {
                    @Override
                    public void processResponse(String response) {
                        getInstructorLogin(response);
                    }
                });




        btnLogin = findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbStudent.isChecked()) {

                    if (!validateStudentUsername(user,student) | !validateStudentPassword(password,student,studIndex)) {
                        //error messages will be shown
                    } else {
                        USER.USERNAME = user.getEditText().getText().toString().trim();

                        Intent i = new Intent(LoginActivity.this, Dashboard.class);
                        startActivity(i);
                        finish();
                    }


                } else {
                    if (!validateInstructorUsername(user, instructor) | !validateInstructorPassword(password,instructor,instructorIndex)) {
                        //error messages will be shown
                    } else {
                        USER.USERNAME = user.getEditText().getText().toString().trim();

                        Intent i = new Intent(LoginActivity.this, Dashboard.class);
                        startActivity(i);
                        finish();
                    }
                }
            }
        });

        user = findViewById(R.id.loginUser);
        password = findViewById(R.id.loginPassword);

    }

    int studIndex = -1;
    int instructorIndex = -1;
    MD5Hash m;

    public boolean validateInstructorUsername(TextInputLayout userText,ArrayList<ArrayList<String>> instructorList) {
        String usernameInput = userText.getEditText().getText().toString().trim();
        boolean userExists = false;
        for (int i = 0; i < instructorList.size(); i++) {
            if (usernameInput.equals(instructorList.get(i).get(0))) {
                userExists = true;
                instructorIndex = i;
                USER.USER_NUM=instructorList.get(i).get(0);
                USER.STUDENT=false;
            }
        }
        if (usernameInput.isEmpty()) {
            userText.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 20) {
            userText.setError("Username too long");
            return false;
        } else if (!userExists) {
            userText.setError("Username does not exist");
            return false;
        } else {
            userText.setError(null);
            return true;
        }
    }

    public boolean validateInstructorPassword(TextInputLayout passwordText,ArrayList<ArrayList<String>> instructorList,int index) {
        String passwordInput = passwordText.getEditText().getText().toString().trim();
        boolean correctPassword = false;
        if (!passwordInput.isEmpty() && index != -1){
            if (m.md5(passwordInput).equals(instructorList.get(index).get(1))) {
                correctPassword = true;
            }
        }
        if (passwordInput.isEmpty()) {
            passwordText.setError("Field can't be empty");
            return false;
        }
        else if(!correctPassword){
            passwordText.setError("Incorrect password");
            return false;
        }
        else {
            passwordText.setError(null);
            return true;
        }
    }



    public boolean validateStudentUsername(TextInputLayout userText,ArrayList<ArrayList<String>> StudentList) {
        String usernameInput = userText.getEditText().getText().toString().trim();
        boolean userExists = false;
        for (int i = 0; i < StudentList.size(); i++) {
            if (usernameInput.equals(StudentList.get(i).get(0))) {
                userExists = true;
                studIndex = i;
                USER.USER_NUM=StudentList.get(i).get(0);
                USER.STUDENT=true;
            }
        }
        if (usernameInput.isEmpty()) {
            userText.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 20) {
            userText.setError("Username too long");
            return false;
        } else if (!userExists) {
            userText.setError("Username does not exist");
            return false;
        } else {
            userText.setError(null);
            return true;
        }
    }

    public boolean validateStudentPassword(TextInputLayout passwordText,ArrayList<ArrayList<String>> StudentList,int index) {
        String passwordInput = passwordText.getEditText().getText().toString().trim();
        boolean correctPassword = false;
        if (!passwordInput.isEmpty() && index != -1 ){
            if (m.md5(passwordInput).equals(StudentList.get(index).get(1))) {
                correctPassword = true;
            }
        }

        if (passwordInput.isEmpty()) {
            passwordText.setError("Field can't be empty");
            return false;
        }
        else if(!correctPassword){
            passwordText.setError("Incorrect password");
            return false;
        }
        else {
            passwordText.setError(null);
            return true;
        }
    }



    public void getStudentLogin(String json){

        try {
            JSONArray all = new JSONArray(json);
            for (int i = 0; i < all.length();i++){
                ArrayList<String>studDetails = new ArrayList<>(2);
                JSONObject obj = all.getJSONObject(i);
                String username = obj.getString("Student_Number");
                String password = obj.getString("Student_Password");
                studentNumbers.add(username);
                studDetails.add(username);
                studDetails.add(password);
                student.add(studDetails);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
    //For testing
    public ArrayList<ArrayList<String> > getStudent(){
        return student;
    }

    public void getInstructorLogin(String json){

        try {
            JSONArray all = new JSONArray(json);
            for (int i = 0; i < all.length();i++){
                ArrayList<String>instructorDetails = new ArrayList<>(2);
                JSONObject obj = all.getJSONObject(i);
                String username = obj.getString("Instructor_Username");
                String password = obj.getString("Instructor_Password");
                instructorUsernames.add(username);
                instructorDetails.add(username);
                instructorDetails.add(password);
                instructor.add(instructorDetails);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }



}
