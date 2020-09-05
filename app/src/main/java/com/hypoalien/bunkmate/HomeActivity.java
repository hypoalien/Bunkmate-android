package com.hypoalien.bunkmate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.blurry.Blurry;

import static com.hypoalien.bunkmate.LoginActivity.APINEW;
import static com.hypoalien.bunkmate.LoginActivity.LOGIN;
import static com.hypoalien.bunkmate.LoginActivity.MY_PREFS_NAME;
import static com.hypoalien.bunkmate.LoginActivity.MY_SOCKET_TIMEOUT_MS;


public class HomeActivity extends AppCompatActivity {

    public static String PIN,PASS;
    Button logout,appDev;
    FloatingActionButton floatingActionButton;

    RecyclerView graphrecycleView,recyclerView,tempRecyclerView;
    FrameLayout frameLayout ;
    ConstraintLayout fToday, fTodayAttendance,fEvents,fBacklogs,fAboutapp,fProfile,fTemp1,mainLayout;
    LinearLayout linearLayout;

    TextView name,pin,branch,semester,semAttendance,abtApp,update,tempTextView;
    boolean sTodayAttendance,sEvents,sBacklogs,state = false;
    double tempWidth,d1,d2,d3,d4,d5,d6;
    int tempText,width,height;
    String login,weekName,todayTimeTable,backlogs,todayAttendance;

    JSONObject apiobj,apiObj;

    Adapter adapter;
    AdapterTodayAttendance adapterTodayAttendance;
    AdapterBacklogs adapterBacklogs;

    CardView load;

    public static   ArrayList<String> aBioName = new ArrayList<String>();
    public static ArrayList<String> aBioValue= new ArrayList<String>();

    public static ArrayList<String> aSubName = new ArrayList<String>();
    public static  ArrayList<String> aSubValue= new ArrayList<>();

    public static  ArrayList<String> aSemName = new ArrayList<String>();
    public static  ArrayList<String> aSemValue= new ArrayList<>();

    private ArrayList<String> mtime = new ArrayList<>();
    private ArrayList<String> msubjects= new ArrayList<>();

    private ArrayList<String> tSubject= new ArrayList<>();
    private ArrayList<String> tStatus= new ArrayList<>();

    private ArrayList<String> bSubject = new ArrayList<>();
    private ArrayList<String> bSem= new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        logout = findViewById(R.id.button);
        appDev=findViewById(R.id.viewDev);
        abtApp=findViewById(R.id.nAboutapp);
        load=findViewById(R.id.mConnect);
        update=findViewById(R.id.textView15);
        tempText=R.id.nTodayAtt;

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        PIN=prefs.getString("pin",null);
        PASS=prefs.getString("password",null);
        boolean bool = prefs.getBoolean("loginStatus",false);
        if(bool){
            load.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeInDown)
                    .duration(800)
                    .repeat(0)
                    .playOn(load);
            update();

        }else{
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putBoolean("loginStatus", true);
            editor.apply();
        }


        //notification
        Intent i= new Intent(getApplicationContext(), ScheduledService.class);
        getApplicationContext().startService(i);

        //binding for bio
        name=findViewById(R.id.vName);
        pin=findViewById(R.id.vPin);
        branch=findViewById(R.id.vBranch);
        semester=findViewById(R.id.vSemester);
        semAttendance=findViewById(R.id.textView8);

        //binding week name for timetable
        TextView textView =findViewById(R.id.textView);

        //binding for side navigation
        fToday=findViewById(R.id.frame1);
        fTodayAttendance=findViewById(R.id.frame2);
        fEvents=findViewById(R.id.frame3);
        fBacklogs=findViewById(R.id.frame4);
        fAboutapp=findViewById(R.id.frame5);
        fProfile=findViewById(R.id.frame6);

        frameLayout= findViewById(R.id.home_layout);
        floatingActionButton =findViewById(R.id.floatingActionButton3);
        mainLayout= findViewById(R.id.main_layout);
        linearLayout=findViewById(R.id.linearLayout);

        //binding temp recycler view
        tempRecyclerView=findViewById(R.id.today_recyclerView);
        //binding temp text view
        tempTextView=findViewById(R.id.nTodayAtt);

        //getting the display dimensions
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        d1=0;
        d2= width*0.1;
        d3= width*0.2;
        d4= width*0.3;
        d5= width*0.4;
        d6= width*0.5;

        //init temp width
        tempWidth=d1;
        //init temp constrain layouts
        fTemp1=fToday;

        //reading login data
        login=readFromFile(LOGIN);
        //get today week-name and init week-name
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SUNDAY:
                // Current day is Sunday
                weekName="Sunday";

                break;
            case Calendar.MONDAY:
                weekName="Monday";
                // Current day is Monday
                break;
            case Calendar.TUESDAY:
                // etc.
                weekName="Tuesday";
                break;
            case Calendar.WEDNESDAY:
                // Current day is Monday
                weekName="Wednesday";
                break;
            case Calendar.THURSDAY:
                // Current day is Monday
                weekName="Thursday";
                break;
            case Calendar.FRIDAY:
                // Current day is Monday
                weekName="Friday";
                break;
            case Calendar.SATURDAY:
                // Current day is Monday
                weekName="Saturday";
                break;
        }
        textView.setText("TODAY :"+" "+weekName );

        //extracting JSON from saved .txt file
        try {
             apiobj=new JSONObject(login);


            aBioName=getArrayListKey(apiobj.getJSONObject("bio").toString());
            aBioValue=getArrayListValue(apiobj.getJSONObject("bio").toString());

            aSemName=getArrayListKey(apiobj.getJSONObject("sem").toString());
            aSemValue=getArrayListValue(apiobj.getJSONObject("sem").toString());

            aSubName=getArrayListKey(apiobj.getJSONObject("sub").toString());
            aSubValue=getArrayListValue(apiobj.getJSONObject("sub").toString());

            JSONObject str = apiobj.getJSONObject("timetable");
            mtime=getArrayListKey(str.getJSONObject(weekName).toString());
            msubjects=getArrayListValue(str.getJSONObject(weekName).toString());

            todayAttendance=apiobj.getJSONObject("today_att").toString();
            backlogs=apiobj.getJSONObject("backlogs").toString();

            tSubject=getArrayListKey(apiobj.getJSONObject("today_att").toString());
            tStatus=getArrayListValue(apiobj.getJSONObject("today_att").toString());

            bSubject=getArrayListKey(apiobj.getJSONObject("backlogs").toString());
            bSem=getArrayListValue(apiobj.getJSONObject("backlogs").toString());


            Log.d("filereadtoobj",aSemValue.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("emptyobj",tSubject.toString()+tStatus.toString());
        }

        appDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDev();
            }
        });

        //init side navigation
        initSide();

        //init onClick listener on fab Button
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                YoYo.with(Techniques.RubberBand)
                        .duration(700)
                        .repeat(0)
                        .playOn(floatingActionButton);
                if (!state){

                    state=true;
                    frameChange(0,1);
                    YoYo.with(Techniques.BounceInLeft)
                            .duration(700)
                            .repeat(0)
                            .playOn(linearLayout);
                    //mainLayout.setVisibility(View.GONE);
                    Blurry.with(getApplicationContext())
                            .radius(10)
                            .sampling(8)
                            .color(Color.argb(45, 0, 0, 0))
                            .async()
                            .onto(mainLayout);


                }
                else{
                    state=false;
                    //mainLayout.setVisibility(View.VISIBLE);

                    Blurry.delete(mainLayout);
                    frameChange(0,1);

                }
            }
        });
        //init log out button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Logging out..",Toast.LENGTH_LONG  ).show();
                logout();
            }
        });
    }
    public void logout(){
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();

        File dir = getFilesDir();
        File file = new File(dir, LOGIN);
        Boolean deleted = file.delete();
        file = new File(dir, APINEW);
        deleted = file.delete();

        Intent i= new Intent(getApplicationContext(), ScheduledService.class);
        getApplicationContext().stopService(i);

        openLoginActivity();

    }
    public void openLoginActivity(){

        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public  void initSide(){

        //subject attendance graph
        graphrecycleView=findViewById(R.id.graph_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,4);
        graphrecycleView.setLayoutManager(gridLayoutManager);
        graphrecycleView.setAdapter(new AdapterGraph(this,aSubName,aSubValue));

        //semester attendance graph
        semAttendance.setText((aSemValue.get(0)));


        //init timetable
        recyclerView= findViewById(R.id.today_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter= new Adapter(this,mtime,msubjects);
        recyclerView.setAdapter(adapter);

        //init today attendance
        if (todayAttendance!="{}"){
            recyclerView= findViewById(R.id.today_att_rv);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapterTodayAttendance= new AdapterTodayAttendance(this,tSubject,tStatus);
            recyclerView.setAdapter(adapterTodayAttendance);
            sTodayAttendance =true;
        }
        else {
            sTodayAttendance=false;
            tempText=R.id.nTodayAtt;

        }

        //init backlogs
        if (backlogs!="{}"){
            recyclerView= findViewById(R.id.backlogs_rv);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapterBacklogs= new AdapterBacklogs(this,bSubject,bSem);
            recyclerView.setAdapter(adapterBacklogs);
            sBacklogs=true;
        }
        else {
            sBacklogs=false;
            tempText=R.id.nBacklogs;

        }


        //init events
        sEvents=false;

        //init about app

        //init profile


        fToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fTemp1 == fToday){
                    // Toast.makeText(getApplicationContext(),"TODAY",Toast.LENGTH_SHORT).show();
                }

                else{
                    collapse();
                    hideText(tempText);
                    hideView(tempRecyclerView.getId());
                    hideText(abtApp.getId());

                    expand(fToday,d1);
                    showView(R.id.today_recyclerView);

                    tempRecyclerView=findViewById(R.id.today_recyclerView);

                    YoYo.with(Techniques.RubberBand)
                            .duration(700)
                            .repeat(0)
                            .playOn(fToday);

                }
            }
        });

        fTodayAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fTemp1==fTodayAttendance){
                    //Toast.makeText(getApplicationContext(),"TODAY'S ATTENDANCE",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(sTodayAttendance){
                        collapse();
                        hideText(tempText);
                        hideView(tempRecyclerView.getId());
                        hideText(abtApp.getId());
                        showView(R.id.today_att_rv);
                        tempRecyclerView=findViewById(R.id.today_att_rv);
                        expand(fTodayAttendance,d2);
                        //tempTextView.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.RubberBand)
                                .duration(700)
                                .repeat(0)
                                .playOn(fTodayAttendance);
                    }
                    else {
                        collapse();
                        hideView(tempRecyclerView.getId());
                        hideText(abtApp.getId());
                        hideText(tempText);
                        showText(R.id.nTodayAtt);
                        tempText=R.id.nTodayAtt;
                        expand(fTodayAttendance,d2);
                        //tempTextView.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.RubberBand)
                                .duration(700)
                                .repeat(0)
                                .playOn(fTodayAttendance);

                    }

                // setDimensions(fTodayAttendance,R.id.imageView2,R.id.textView2,R.id.frame2);

                }
            }
        });

        fEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fTemp1==fEvents){
                    //Toast.makeText(getApplicationContext(),"EVENTS",Toast.LENGTH_SHORT).show();
                }
                else{
                    collapse();
                    hideText(tempText);
                    hideView(tempRecyclerView.getId());
                    hideText(abtApp.getId());
                    expand(fEvents,d3);


                    showText(R.id.nEvents);
                    tempText=R.id.nEvents;

                    YoYo.with(Techniques.RubberBand)
                            .duration(700)
                            .repeat(0)
                            .playOn(fEvents);
                }
            }
        });

        fBacklogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(fTemp1==fBacklogs){
                    //Toast.makeText(getApplicationContext(),"BACKLOGS",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (sBacklogs) {

                        collapse();
                        hideText(tempText);
                        hideView(tempRecyclerView.getId());
                        hideText(abtApp.getId());
                        showView(R.id.backlogs_rv);
                        tempRecyclerView = findViewById(R.id.backlogs_rv);
                        expand(fBacklogs, d4);


                        YoYo.with(Techniques.RubberBand)
                                .duration(700)
                                .repeat(0)
                                .playOn(fBacklogs);

                    }
                    else {
                        collapse();
                        hideView(tempRecyclerView.getId());
                        hideText(tempText);
                        hideText(abtApp.getId());
                        showText(R.id.nBacklogs);
                        tempText=R.id.nBacklogs;
                        expand(fBacklogs,d4);
                        //tempTextView.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.RubberBand)
                                .duration(700)
                                .repeat(0)
                                .playOn(fBacklogs);
                    }
                }
            }
        });

        fAboutapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fTemp1==fAboutapp){
                    //Toast.makeText(getApplicationContext(),"ABOUT APP",Toast.LENGTH_SHORT).show();
                }
                else{
                    collapse();
                    hideText(tempText);
                    hideView(tempRecyclerView.getId());
                    showText(abtApp.getId());

                    expand(fAboutapp,d5);

                    YoYo.with(Techniques.RubberBand)
                            .duration(700)
                            .repeat(0)
                            .playOn(fAboutapp);

                }
            }
        });

        fProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fTemp1==fProfile){
                    // Toast.makeText(getApplicationContext(),"PROFILE",Toast.LENGTH_SHORT).show();
                }
                if(fTemp1!=fProfile){
                    collapse();
                    hideText(tempText);
                    hideView(tempRecyclerView.getId());
                    hideText(abtApp.getId());


                    setProfile();
                    expand(fProfile,d6);
                    YoYo.with(Techniques.RubberBand)
                            .duration(700)
                            .repeat(0)
                            .playOn(fProfile);

                }

            }
        });
    }

    private void collapse(){
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,0,1.0f
        );
        params2.setMargins(0, 0, (int) tempWidth, 0);
        fTemp1.setLayoutParams(params2);

    }
    private void expand(ConstraintLayout constraintLayout,double width){
        //expand
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,0,5.0f
        );

        params.setMargins(0, 0, (int)width, 0);
        constraintLayout.setLayoutParams(params);
        //fToday.getLayoutParams().height=(int)h1;

        //init temp var
        fTemp1=constraintLayout;
        tempWidth=width;
    }
    public void frameChange(int fromindex,int toindex){

        View tempFrom = frameLayout.getChildAt(fromindex);
        View tempTo   = frameLayout.getChildAt(toindex);
// first remove the view which is above in the parent's stack
// otherwise, if you remove the other child you'll call the `removeViewAt`
// method with the wrong index and the view which was supposed to be detached
// from the parent is still attached to it
        frameLayout.removeViewAt(toindex);
        frameLayout.removeViewAt(fromindex);
// first add the child which is lower in the hierarchy so you add the views
// in the correct order
        frameLayout.addView(tempTo,fromindex);
        frameLayout.addView(tempFrom, toindex);
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
    public void setProfile(){
        name.setText(aBioValue.get(0));
        pin.setText(aBioValue.get(1));
        branch.setText(aBioValue.get(3));
        semester.setText(aBioValue.get(2));

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
    private void hideText(int id){
        tempTextView=findViewById(id);
        tempTextView.setVisibility(View.INVISIBLE);
    }
    private void showText(int id){
        tempTextView=findViewById(id);
        tempTextView.setVisibility(View.VISIBLE);
    }
    private void hideView(int viewID){
        RecyclerView recyclerView;
        recyclerView=findViewById(viewID);
        recyclerView.setVisibility(View.INVISIBLE);
    }
    private void showView(int viewID){
        RecyclerView recyclerView;
        recyclerView=findViewById(viewID);
        recyclerView.setVisibility(View.VISIBLE);
    }
    void showDev(){
        DevDialog exampleDialog = new DevDialog();
        exampleDialog.show(getSupportFragmentManager(), "Developer dialog");

    }
    private  void update(){

        // String url = "  https://192.168.31.238:5000/login/"+pin+"+"+pass;
        //String url = "  https://127.0.0.1:5000/login/"+pin+"+"+pass;
        String url = "  https://hypoalien-bunkmate-flask-api.glitch.me//home/"+PIN+"+"+PASS;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response   
                        load.setCardBackgroundColor(Color.rgb(44,218,157));
                        update.setText("Connected");
                        YoYo.with(Techniques.FadeInDown)
                                .duration(700)
                                .repeat(0)
                                .playOn(update);

                        YoYo.with(Techniques.FadeInUp)
                                .duration(700)
                                .repeat(0)
                                .playOn(update);
                        final Handler handler = new Handler();
                        //Do something after 100ms
                        handler.postDelayed(HomeActivity.this::invisible, 2000);

                        try {
                            apiObj=new JSONObject(login);
                            Log.d("json",apiObj.toString());
                            apiObj.put("sem",response.get("sem"));
                            apiObj.put("sub",response.get("sub"));
                            apiObj.put("backlogs",response.get("backlogs"));
                            apiObj.put("today_att",response.get("today_att"));
                            Log.d("jsonobject",apiObj.toString()+"\n"+response.toString());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        writeToFile(apiObj.toString(),LOGIN);
                        Toast.makeText(getApplicationContext(),"Refreshed..",Toast.LENGTH_LONG  ).show();

                        // LoginStatus.setUserName(getApplicationContext(),pin);

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(),"Please check your Pin or Password"+error.toString(),Toast.LENGTH_LONG).show();
                        Log.d("clicked and started",error.toString());
                        update();
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
    public void invisible(){
        YoYo.with(Techniques.FadeInUp)
                .duration(1500)
                .repeat(0)
                .playOn(load);
        load.setVisibility(View.INVISIBLE);
    }
}
