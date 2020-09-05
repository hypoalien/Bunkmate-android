package com.hypoalien.bunkmate;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.hypoalien.bunkmate.App.CHANNEL_1_ID;
import static com.hypoalien.bunkmate.App.CHANNEL_2_ID;
import static com.hypoalien.bunkmate.LoginActivity.APINEW;
import static com.hypoalien.bunkmate.LoginActivity.MY_PREFS_NAME;



public class ScheduledService extends Service {

    String PIN,PASS;
    boolean aBoolean=false;

    private Timer timer = new Timer();
    ArrayList<String> notifyNames= new ArrayList<>();
    ArrayList<String> notifyValues= new ArrayList<>();
    ArrayList<Double> notifyNetValues= new ArrayList<>();

    String semNotifyNames;
    double semNotifyValues;
    double semNotifyNetValues;

    String subNotifyNames;
    Double subNotifyValues;
    Double subNotifyNetValues;

    String title;
    String message;


    public JSONObject semObj=new JSONObject();
    public JSONObject subObj=new JSONObject();
    public JSONObject semObjfile=new JSONObject();
    public JSONObject subObjfile=new JSONObject();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate()
    {
        super.onCreate();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        PIN = prefs.getString("pin",null);
        PASS = prefs.getString("password",null);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //Your code here
                apiCalls();

            }
        }, 0, 49*60*1000);//5 Minutes
    }

    private void writeToFile(String data,String filename) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(String filename) {

        String ret = "";

        try {
            InputStream inputStream = getApplicationContext().openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private void apiCalls(){
        String url = " https://hypoalien-bunkmate-flask-api.glitch.me//api_call/"+PIN+"+"+PASS;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response   
                        String apiNew =readFromFile(APINEW);
                        if(apiNew.equals(response.toString())){

                            Log.d("backcalls","same responce");

                        }else {
                            Log.d("backcalls","not same responce"+response.toString());
                            diff(response);
                            writeToFile(response.toString(),APINEW);
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("schudle service",error.toString());
                    }
                }
        );
        // add it to the RequestQueue  
        queue.add(getRequest);
    }

    private ArrayList<String> getArrayListValue(String obj){
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> value = new ArrayList<>();

        try {
            JSONObject jsonObject= new JSONObject(obj);
            Iterator<String> keysItr = jsonObject.keys();
            while (keysItr.hasNext()){
                String key =keysItr.next();
                name.add(key);
                try {
                    value.add(jsonObject.get(key).toString());
                } catch (JSONException e) {
                    Log.d("error","couldnt load bio value");
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("file get arrayL name",name.toString());
        return value;
    }
    private ArrayList<Double> getDArrayListValue(String obj){
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> value = new ArrayList<>();

        try {
            JSONObject jsonObject= new JSONObject(obj);
            Iterator<String> keysItr = jsonObject.keys();
            while (keysItr.hasNext()){
                String key =keysItr.next();
                name.add(key);
                try {
                    value.add(jsonObject.get(key).toString());
                } catch (JSONException e) {
                    Log.d("error","couldnt load bio value");
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList<Double> data = new ArrayList<>();
        if (value != null) {
            for (int i = 0; i < value.size(); i++) {

                data.add(i, Double.parseDouble(value.get(i).trim().replace("%", "")));
            }

        }
        Log.d("file get arrayL name",name.toString());
        return data;
    }
    private ArrayList<String> getArrayListKey(String obj){
        ArrayList<String> aSemName = new ArrayList<>();

        try {
            JSONObject jsonObject= new JSONObject(obj);
            Iterator<String> keysItr = jsonObject.keys();
            while (keysItr.hasNext()){
                String key =keysItr.next();
                aSemName.add(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("file get array list key",aSemName.toString());
        return aSemName;
    }

    public void diff(JSONObject jsonObject){
        double semAtt;
        double mSemAtt;

       String fileContent = readFromFile(APINEW);
        try {
            JSONObject apiobj = new JSONObject(fileContent);
            semAtt=Double.parseDouble(getArrayListValue(apiobj.getJSONObject("sem").toString()).get(0).trim().replace("%", ""));
            mSemAtt=Double.parseDouble(getArrayListValue(jsonObject.getJSONObject("sem").toString()).get(0).trim().replace("%", ""));
            if(semAtt!=mSemAtt){
                semNotifyNames="Semester";
                semNotifyValues =mSemAtt;
                semNotifyNetValues= mSemAtt-semAtt;
                sendOnChannel1();
            }
            Log.d("notify",notifyNames.toString()+notifyNetValues.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void sendOnChannel1() {
        if(semNotifyNetValues>0){
             title = "Semester Attendance : "+semNotifyValues+"%";
             message = "Attendance Increased"+" "+": +"+semNotifyNetValues+"%"+"";
        }
        else{
             title = "Semester Attendance  : "+semNotifyValues+"%";
             message = "Attendance Decreased"+" "+":"+semNotifyNetValues+"%";
        }

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID )
                .setSmallIcon(R.drawable.logo_small)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[] { 1000, 1000 })
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(1, notification);
    }

    public void sendOnChannel2() {
        if(subNotifyNetValues>0){
            title = subNotifyNames+"Attendance: "+subNotifyValues+"%"+" "+":"+"+"+subNotifyNetValues+"%";
            message = "Attendance Increased";
        }
        else{
            title = subNotifyNames+"Attendance: "+subNotifyValues+"%"+" "+":"+subNotifyNetValues+"%";
            message = "Attendance Decreased";
        }


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID )
                .setSmallIcon(R.drawable.logo_small)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[] { 1000, 1000 })
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(1, notification);
    }

    @Override
    public void onDestroy()
    {
        timer.cancel();
        Log.d("service","stopped");
        super.onDestroy();
    }

}
