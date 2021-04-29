package com.example.witsonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.text.TextUtils;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private RadioGroup rgRegister;
    private TextInputEditText userText;
    private RadioButton rbStudent;
    private RadioButton rbInstructor;
    private TextInputLayout username, firstName, lastName, email, password, confirmPass;
    private Button register;
    private ArrayList<String> studentNumbers = new ArrayList<>();
    private ArrayList<String> instructorUsernames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Bundle extras = getIntent().getExtras();
        studentNumbers = extras.getStringArrayList("students");
        instructorUsernames = extras.getStringArrayList("instructors");

        rbStudent = findViewById(R.id.student);
        rbInstructor = findViewById(R.id.instructor);
        userText = (TextInputEditText) findViewById(R.id.RegisterText);
        userText.setHint("Student Number");
        rgRegister = findViewById((R.id.rbUserType));

        rgRegister.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.student) {
                    userText.setHint("Student Number");
                    userText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    userText.setText("");
                    username.setCounterMaxLength(10);
                    userText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
                } else {
                    userText.setHint("Username");
                    userText.setInputType(InputType.TYPE_CLASS_TEXT);
                    username.setCounterMaxLength(20);
                    userText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
                }
            }
        });

        username = findViewById(R.id.username);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirmPassword);
        register = (Button) findViewById(R.id.buttonRegister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Remove errors on input layouts if errors are fixed
                username.setErrorEnabled(false);
                firstName.setErrorEnabled(false);
                lastName.setErrorEnabled(false);
                email.setErrorEnabled(false);
                password.setErrorEnabled(false);
                confirmPass.setErrorEnabled(false);

                String[] phpFile = {"studentRegister.php"}; // php file for students
                String[] userType = {"studentNumber"}; // students use student number to register
                if (rbInstructor.isChecked()) {
                    phpFile[0] = "instructorRegister.php";// php file for instructor
                    userType[0] = "username";//instructors use username to register
                    Log.d("HERE", phpFile[0]);
                } else {
                    phpFile[0] = "studentRegister.php";// php file for instructor
                    userType[0] = "studentNumber";//instructors use username to register
                    Log.d("HERE", phpFile[0]);
                }

                try {
                    processInfo(username, firstName, lastName, email, password, confirmPass, phpFile[0], userType[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    //Post request function
    private void doPostRequest(final TextInputLayout user, TextInputLayout name, TextInputLayout surname, TextInputLayout emailAdd, TextInputLayout pass, String phpFile, String userType) throws IOException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2105624/" + phpFile).newBuilder();
        urlBuilder.addQueryParameter(userType, user.getEditText().getText().toString());
        urlBuilder.addQueryParameter("firstname", name.getEditText().getText().toString());
        urlBuilder.addQueryParameter("lastname", surname.getEditText().getText().toString());
        urlBuilder.addQueryParameter("email", emailAdd.getEditText().getText().toString());
        urlBuilder.addQueryParameter("password", pass.getEditText().getText().toString());
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                RegisterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("HERE", responseData);
                        if (responseData.equals("Successful")) {
                            Toast toast = Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_LONG);
                            toast.show();
                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            user.setError(" User already exists");
                        }
                    }
                });
            }
        });
    }

    // This function checks if a required text is empty or not
    public boolean isEmpty(TextInputLayout text) {
        boolean empty = false;
        if (text.getEditText().getText().toString().isEmpty()) {
            text.setError("Field can't be empty");
            empty = true;
        }

        return empty;
    }

    public boolean userExists() {
        String usernameInput = username.getEditText().getText().toString().trim();
        boolean exists = false;
        if (rbInstructor.isChecked()) {
            for (int i = 0; i < instructorUsernames.size(); i++) {
                if (usernameInput.equals(instructorUsernames.get(i))) {
                    username.setError("Username already exists");
                    exists = true;
                }
            }
        } else {
            for (int i = 0; i < studentNumbers.size(); i++) {
                if (usernameInput.equals(studentNumbers.get(i))) {
                    username.setError("Username already exists");
                    exists = true;
                }
            }
        }
        return exists;
    }

    //this function validates email
    private boolean validateEmail() {
        boolean valid = true;
        String emailInput = email.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            email.setError("Field can't be empty");
            valid =  false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Please enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
            valid = true;
        }
        return valid;
    }



    //This function validates password only
    public boolean validatePassword(TextInputLayout password, TextInputLayout confirmPass){
        boolean valid = true;
        if(confirmPass.getEditText().getText().toString().isEmpty()){
            confirmPass.setError("Confirm your password");
            valid = false;
        }
        if(password.getEditText().getText().toString().length()<5){
            password.setError("Password must be at least 5 characters long");
            valid = false;
        }
        else if(!password.getEditText().getText().toString().equals(confirmPass.getEditText().getText().toString())){
            confirmPass.setError("Passwords don't match");
            valid = false;
        }
        return valid;
    }

    //This function checks if a text contains digits only
    boolean isDigits(TextInputLayout text){
        return TextUtils.isDigitsOnly(text.getEditText().getText().toString());
    }

    //This function processes data for registration
    public boolean processInfo(TextInputLayout user, TextInputLayout name, TextInputLayout surname, TextInputLayout emailAdd, TextInputLayout pass, TextInputLayout confirmPass,String phpFile, String userType) throws IOException {
        boolean valid = true;
        isEmpty(user);
        isEmpty(name);
        isEmpty(surname);
        isEmpty(emailAdd);
        isEmpty(pass);
        userExists();
        validateEmail();
        if(!(isEmpty(user) && isEmpty(name) && isEmpty(surname) && isEmpty(pass) && userExists()) && validatePassword(pass,confirmPass) && validateEmail() ){
            doPostRequest(user, name, surname, emailAdd, pass, phpFile, userType);
        }
        else{
            valid = false;
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(i);
        finish();

    }

}
