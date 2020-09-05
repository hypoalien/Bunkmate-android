package com.hypoalien.bunkmate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "loginStatus";
    TextInputEditText mpin ,mpass;
    public static String pin,pass;

    public static String LOGIN ="login.txt";

    public static String APINEW ="apinew.txt";
    public static int MY_SOCKET_TIMEOUT_MS=15000;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        textView=findViewById(R.id.mIncorrect);

        final Button loginButton = findViewById(R.id.bLogin);
        mpin =findViewById(R.id.input_pin);
        mpass =findViewById(R.id.input_pwd);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((mpin.getText().toString()!="") && (mpass.getText().toString()!="")){
                    pin = mpin.getText().toString();
                    pass = mpass.getText().toString();
                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("pin", pin);
                    editor.putString("password",pass);
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"Logging in..Please wait",Toast.LENGTH_LONG).show();
                    loginAttempt();
                }
                else{
                    textView.setText("Please enter correct pin and password");
                    textView.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeInDown)
                            .duration(700)
                            .repeat(0)
                            .playOn(textView);
                }

            }
        });

    }


    private  void loginAttempt(){
        String url = "  https://hypoalien-bunkmate-flask-api.glitch.me//login/"+pin+"+"+pass;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,

                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response   
                        Toast.makeText(getApplicationContext(),"Login successful..",Toast.LENGTH_SHORT  ).show();

                        writeToFile(response.toString(),LOGIN);
                        writeToFile("{\"sem\": {\"semester Attendance\": \"00.00\"}, \"sub\": {}}",APINEW);
                        openHomeActivity();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText("Please check your Pin or Password");
                        textView.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.FadeInDown)
                                .duration(700)
                                .repeat(0)
                                .playOn(textView);

                        Log.d("Login fail",error.toString());
                    }
                }
        );
        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // add it to the RequestQueue  
        queue.add(getRequest);
    }

    private void writeToFile(String data, String filename) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    public void openHomeActivity(){
        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }


    }


