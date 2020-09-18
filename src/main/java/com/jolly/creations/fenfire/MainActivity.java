package com.jolly.creations.fenfire;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements LocationListener {

    double longi=0.0,lati=0.0,tep;
    int numMessages=0;
    String lon,lat,crop="crops",temp="31.00°",lang="english",city="city";
    LocationManager locationManager;
    TextView cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, updatedField,tcrop,wneed,soil,season,mpr,dam,pest,gprd,pcost,totar,totcar1,helpno,fertilizer;
    Typeface weatherFont;
    TextView cityField1,currentTemperatureField1,weatherIcon1, updatedField1,detailsField1,humidity_field1,pressure_field1,season1,soil1,tcrop1,gprd1,pest1,pcost1,wneed1,mpr1,timer,dam1,pestcal,assistant,helpno1,fertllizer1;
    Button crops,eng,tam,hind,b1,weatherIcon,chat,call;
   // String sunny="&#xf00d;",clrnit="&#xf02e;",fogy="&#xf014;",cloudy="&#xf013;",rainy="&#xf019;",snowy="&#xf01b;",thunder="&#xf01e;",drizele="&#xf01c;";
    LinearLayout cropd,maplay;
    PhotoView image;
    Spinner languages;
    WebView web;
    EditText nacre;
    final int TIME_INTERVAL = 2000;
    long mBackPressed = 0;
    int low = 500;
    int high = 700;

    String dtem="20",dhum="20";

    private ArrayList<String> langu = new ArrayList<>();

    DatabaseReference textref;
    DatabaseReference placeref;
    DatabaseReference cropref;
    DatabaseReference senref;

    //for timer
    private static final long START_TIME_IN_MILLIS = 5400000;
    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    private long mEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        textref = FirebaseDatabase.getInstance().getReference("crops");
        placeref = FirebaseDatabase.getInstance().getReference("places");


            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(29, 135, 209))); // set your desired color
        }


        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);



        getLocation();
        temp = temp.replaceAll("°","").trim();
        tep=Double.parseDouble(temp);
        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");


//text feild
        cityField = (TextView)findViewById(R.id.city_field);
        updatedField = (TextView)findViewById(R.id.updated_field);
        detailsField = (TextView)findViewById(R.id.details_field);
        currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
        humidity_field = (TextView)findViewById(R.id.humidity_field);
        pressure_field = (TextView)findViewById(R.id.pressure_field);
        weatherIcon = (Button)findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);
        wneed = (TextView)findViewById(R.id.wneeds);
        soil = (TextView)findViewById(R.id.soil);
        season = (TextView)findViewById(R.id.season);
        mpr = (TextView)findViewById(R.id.mprice);
        dam = (TextView)findViewById(R.id.dams);
        pest = (TextView)findViewById(R.id.pest);
        gprd = (TextView)findViewById(R.id.gperiod);
        pcost = (TextView)findViewById(R.id.pestcost);
        totar=(TextView)findViewById(R.id.tacre1);
        helpno=(TextView)findViewById(R.id.helpno);
        fertilizer=(TextView)findViewById(R.id.fertilizer);

        nacre=(EditText)findViewById(R.id.tacre);
        chat=(Button)findViewById(R.id.chatbot);
        call=(Button)findViewById(R.id.call);

        b1=(Button)findViewById(R.id.button);
        pestcal = (Button) findViewById(R.id.pestcal);

//images view

        image = (PhotoView)findViewById(R.id.paddy);
        image.setImageResource(R.drawable.rice);

//spinner
        languages = (Spinner) findViewById(R.id.lang);

//timer
        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);

//linear layout
        cropd =(LinearLayout)findViewById(R.id.cropd);
        cropd.setVisibility(View.GONE);

// text feilds captions

        cityField1 = (TextView) findViewById(R.id.city_field1);
        updatedField1 = (TextView)findViewById(R.id.updated_field1);
        weatherIcon1 = (TextView)findViewById(R.id.weather_icon1);
        currentTemperatureField1 = (TextView)findViewById(R.id.current_temperature_field1);
        detailsField1 = (TextView) findViewById(R.id.details_field1);
        humidity_field1 = (TextView) findViewById(R.id.humidity_field1);
        pressure_field1 = (TextView) findViewById(R.id.pressure_field1);
        season1 = (TextView) findViewById(R.id.season1);
        soil1 = (TextView) findViewById(R.id.soil1);
        tcrop1 = (TextView) findViewById(R.id.crop1);
        gprd1 = (TextView) findViewById(R.id.gperiod1);
        pest1 = (TextView) findViewById(R.id.pest1);
        pcost1 = (TextView) findViewById(R.id.pestcost1);
        wneed1 = (TextView) findViewById(R.id.wneeds1);
        mpr1 = (TextView) findViewById(R.id.mprice1);
        timer = (TextView) findViewById(R.id.timer1);
        dam1 = (TextView) findViewById(R.id.dams1);
        assistant=(TextView)findViewById(R.id.assistant);
        helpno1 = (TextView) findViewById(R.id.helpno1);
        fertllizer1=(TextView)findViewById(R.id.fertilizer1);

//languaes button
        crops = (Button) findViewById(R.id.crop);
        eng = (Button) findViewById(R.id.englan);
        tam = (Button) findViewById(R.id.tamlan);
        hind = (Button) findViewById(R.id.hindlan);


        /*chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intObj = new Intent(MainActivity.this,ChatActivity.class);
                startActivity(intObj);
                Toast.makeText(MainActivity.this,"Fenna!...",Toast.LENGTH_SHORT).show();

            }
        });*/

        pestcal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rand = new Random();
                int result = rand.nextInt(high-low) + low;
                Double a  = Double.parseDouble(nacre.getText().toString());
                Toast.makeText(MainActivity.this,a+"",Toast.LENGTH_SHORT).show();
                pcost.setText((a*result)+"₹");
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dial = new Intent();
                dial.setAction(Intent.ACTION_DIAL);
                dial.setData(Uri.parse("tel:".concat(helpno.getText().toString())));
                startActivity(dial);
            }
        });


        b1.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                getLocation();

                // custom dialog
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.maplay);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                }

                // set the custom dialog components - text, image and button
                web = (WebView) dialog.findViewById(R.id.web);
                web.setWebViewClient(new MyBrowser());
                web.getSettings().setLoadsImagesAutomatically(true);
                web.getSettings().setJavaScriptEnabled(true);
                web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                web.loadUrl("https://www.google.com/maps/@"+lat+","+lon+",90m/data=!3m1!1e3");
                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

        weatherIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intObj = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intObj);
                Toast.makeText(MainActivity.this,"Weather report",Toast.LENGTH_SHORT).show();
            }
        });




        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }



        crops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropd.setVisibility(cropd.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });



        langu.add("English");
        langu.add("தமிழ்");
        langu.add("हिंदी");
        languages.setAdapter(new ArrayAdapter<String>(getBaseContext(), R.layout.spinner_item, langu));

        languages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
                final int _position = _param3;
                switch(_position)
                {
                    case 0: lang="e";crops(lang); break;
                    case 1: lang="t";crops(lang); break;
                    case 2:  lang="h";crops(lang); break;
                    default:  lang="e"; crops(lang);


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> _param1) {
                languages.setEnabled(false);
                languages.setClickable(false);
                lang="e";
                crops(lang);
            }
        });



        eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang="e";
                crops(lang);
                Toast.makeText(getBaseContext(), "You selected English", Toast.LENGTH_SHORT).show();
            }
        });


        tam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lang="t";
                crops(lang);
                Toast.makeText(getBaseContext(), "நீங்கள் தமிழ் தெரிவு செய்தீர்கள்", Toast.LENGTH_SHORT).show();

            }
        });

        hind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang="h";
                crops(lang);
                Toast.makeText(getBaseContext(), "आपने तमिल का चयन किया", Toast.LENGTH_SHORT).show();
            }
        });



        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });



    }


    void crops(String l)
    {

        if(l.equals("english"))
        {

            settext("english","paddy","Tiruvallur");

            /*------------thiruvallur-------------*/


            if(lati > 13.00 && lati < 13.20 )
            {

                if(tep <= 26.0 && tep > 23.0 )
                {
                    settext("english","cotton","Tiruvallur");
                }
                else
                {
                    settext("english","gnut","Tiruvallur");
                }
                if(tep > 20.0)
                {
                    settext("english","paddy","Tiruvallur");
                }



            }

            if(lati > 13.20 && lati < 13.35)
            {

                if(tep <= 28.0 && tep > 26.0 )
                {
                    settext("english","scane","Tiruvallur");
                }
                else
                {
                    settext("english","wheat","Tiruvallur");
                }
                if(tep > 20.0)
                {
                    settext("english","paddy","Tiruvallur");
                }

            }

            /*------------pondichery-------------*/

            if(lati > 11.90 && lati < 11.94 )
            {

                if(tep <= 26.0 && tep > 23.0 )
                {
                    settext("english","cotton","pondichery");
                }
                else
                {
                    settext("english","gnut","pondichery");
                }
                if(tep > 20.0)
                {
                    settext("english","paddy","pondichery");
                }


            }

            if(lati < 11.97 && lati > 11.94)
            {
                if(tep <= 28.0 && tep > 26.0 )
                {
                    settext("english","scane","pondichery");
                }
                else
                {
                    settext("english","wheat","pondichery");
                }
                if(tep > 20.0)
                {
                    settext("english","paddy","pondichery");
                }
            }

            /*------------chennai-------------*/



            if(lati > 12.92 && lati < 12.98 )
            {
                if(tep <= 26.0 && tep > 23.0 )
                {
                    settext("english","cotton","pondichery");
                }
                else
                {
                    settext("english","gnut","pondichery");
                }
                if(tep > 20.0)
                {
                    settext("english","paddy","pondichery");
                }
            }

            if(lati < 12.9268 && lati > 12.88)
            {
                if(tep <= 28.0 && tep > 26.0 )
                {
                    settext("english","scane","pondichery");
                }
                else
                {
                    settext("english","wheat","pondichery");
                }
                if(tep > 20.0)
                {
                    settext("english","paddy","pondichery");
                }
            }





        }

        if(l.equals("tamil"))
        {
            settext("tamil","paddy","Tiruvallur");
            /*------------thiruvallur-------------*/


            if(lati > 13.00 && lati < 13.20 )
            {

                if(tep <= 26.0 && tep > 23.0 )
                {
                    settext("tamil","cotton","Tiruvallur");
                }
                else
                {
                    settext("tamil","gnut","Tiruvallur");
                }
                if(tep > 20.0)
                {
                    settext("tamil","paddy","Tiruvallur");
                }



            }

            if(lati > 13.20 && lati < 13.35)
            {

                if(tep <= 28.0 && tep > 26.0 )
                {
                    settext("tamil","scane","Tiruvallur");
                }
                else
                {
                    settext("tamil","wheat","Tiruvallur");
                }
                if(tep > 20.0)
                {
                    settext("tamil","paddy","Tiruvallur");
                }

            }

            /*------------pondichery-------------*/

            if(lati > 11.90 && lati < 11.94 )
            {

                if(tep <= 26.0 && tep > 23.0 )
                {
                    settext("tamil","cotton","pondichery");
                }
                else
                {
                    settext("tamil","gnut","pondichery");
                }
                if(tep > 20.0)
                {
                    settext("tamil","paddy","pondichery");
                }


            }

            if(lati < 11.97 && lati > 11.94)
            {
                if(tep <= 28.0 && tep > 26.0 )
                {
                    settext("tamil","scane","pondichery");
                }
                else
                {
                    settext("tamil","wheat","pondichery");
                }
                if(tep > 20.0)
                {
                    settext("tamil","paddy","pondichery");
                }
            }

            /*------------chennai-------------*/



            if(lati > 12.92 && lati < 12.98 )
            {
                if(tep <= 26.0 && tep > 23.0 )
                {
                    settext("tamil","cotton","pondichery");
                }
                else
                {
                    settext("tamil","gnut","pondichery");
                }
                if(tep > 20.0)
                {
                    settext("tamil","paddy","pondichery");
                }
            }

            if(lati < 12.9268 && lati > 12.88)
            {
                if(tep <= 28.0 && tep > 26.0 )
                {
                    settext("tamil","scane","pondichery");
                }
                else
                {
                    settext("tamil","wheat","pondichery");
                }
                if(tep > 20.0)
                {
                    settext("tamil","paddy","pondichery");
                }
            }





        }

        if(l.equals("hindi"))
        {
            settext("hindi","paddy","Tiruvallur");
            /*------------thiruvallur-------------*/


            if(lati > 13.00 && lati < 13.20 )
            {

                if(tep <= 26.0 && tep > 23.0 )
                {
                    settext("hindi","cotton","Tiruvallur");
                }
                else
                {
                    settext("hindi","gnut","Tiruvallur");
                }
                if(tep > 20.0)
                {
                    settext("hindi","paddy","Tiruvallur");
                }



            }

            if(lati > 13.20 && lati < 13.35)
            {

                if(tep <= 28.0 && tep > 26.0 )
                {
                    settext("hindi","scane","Tiruvallur");
                }
                else
                {
                    settext("hindi","wheat","Tiruvallur");
                }
                if(tep > 20.0)
                {
                    settext("hindi","paddy","Tiruvallur");
                }

            }

            /*------------pondichery-------------*/

            if(lati > 11.90 && lati < 11.94 )
            {

                if(tep <= 26.0 && tep > 23.0 )
                {
                    settext("hindi","cotton","pondichery");
                }
                else
                {
                    settext("hindi","gnut","pondichery");
                }
                if(tep > 20.0)
                {
                    settext("hindi","paddy","pondichery");
                }


            }

            if(lati < 11.97 && lati > 11.94)
            {
                if(tep <= 28.0 && tep > 26.0 )
                {
                    settext("hindi","scane","pondichery");
                }
                else
                {
                    settext("hindi","wheat","pondichery");
                }
                if(tep > 20.0)
                {
                    settext("hindi","paddy","pondichery");
                }
            }

            /*------------chennai-------------*/



            if(lati > 12.92 && lati < 12.98 )
            {
                if(tep <= 26.0 && tep > 23.0 )
                {
                    settext("hindi","cotton","pondichery");
                }
                else
                {
                    settext("hindi","gnut","pondichery");
                }
                if(tep > 20.0)
                {
                    settext("hindi","paddy","pondichery");
                }
            }

            if(lati < 12.9268 && lati > 12.88)
            {
                if(tep <= 28.0 && tep > 26.0 )
                {
                    settext("hindi","scane","pondichery");
                }
                else
                {
                    settext("hindi","wheat","pondichery");
                }
                if(tep > 20.0)
                {
                    settext("hindi","paddy","pondichery");
                }
            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.tamil:
                lang="tamil";
                crops(lang);
                Toast.makeText(getBaseContext(), "நீங்கள் தமிழ் தெரிவு செய்தீர்கள்", Toast.LENGTH_SHORT).show();
                break;

            case R.id.hindi:
                lang="hindi";
                crops(lang);
                Toast.makeText(getBaseContext(), "आपने तमिल का चयन किया", Toast.LENGTH_SHORT).show();
                break;

            default:
                lang="english";
                crops(lang);
                Toast.makeText(getBaseContext(), "You selected English", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;

    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(final Location location) {
        //locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
        Function.placeIdTask asyncTask =new Function.placeIdTask(new Function.AsyncResponse() {
            public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise) {

                cityField.setText(weather_city);
                city=weather_city;
                updatedField.setText(weather_updatedOn);
                detailsField.setText(weather_description);
                //currentTemperatureField.setText(weather_temperature);
                String tmpl = temp.replaceAll("°", "").trim();
                double tmp = Double.parseDouble(dtem);
                double hum = Double.parseDouble(dhum);
                String humrec = weather_humidity;
                humrec.replaceAll("%","").trim();
                temp=weather_temperature;
                currentTemperatureField.setText(temp);
                //humidity_field.setText(dhum);
                humidity_field.setText(""+weather_humidity);
                pressure_field.setText(""+weather_pressure);
                weatherIcon.setText(Html.fromHtml(weather_iconText));

                try {
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    cityField.setText(cityField.getText() + "\n"+addresses.get(0).getAddressLine(0)+", ");//+
                    // addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
                }catch(Exception e)
                {
                    Toast.makeText(MainActivity.this,""+e , Toast.LENGTH_SHORT).show();
                }

            }
        });

        longi=location.getLongitude();
        lati=location.getLatitude();
        lon=""+location.getLongitude();
        lat=""+location.getLatitude();
        asyncTask.execute(lat,lon);
        crops(lang);//  asyncTask.execute("Latitude", "Longitude")
        addNotification();
        displayNotification();

        Toast.makeText(MainActivity.this,"longi"+lon , Toast.LENGTH_SHORT).show();
        Toast.makeText(MainActivity.this, "lati"+lat, Toast.LENGTH_SHORT).show();

    }

    public void settext(final String language , String crop , String place)
    {
        try
        {
            textref = FirebaseDatabase.getInstance().getReference("crops").child(language);
            textref.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    cityField1.setText(String.format("%s", snapshot.child("location_text").getValue()));
                    updatedField1.setText(String.format("%s", snapshot.child("time_updation_text").getValue()));
                    weatherIcon1.setText(String.format("%s", snapshot.child("weather_text").getValue()));
                    currentTemperatureField1.setText(String.format("%s", snapshot.child("temperature_text").getValue()));
                    detailsField1.setText(String.format("%s", snapshot.child("weather_details_text").getValue()));
                    humidity_field1.setText(String.format("%s", snapshot.child("humidity_text").getValue()));
                    pressure_field1.setText(String.format("%s", snapshot.child("pressure_text").getValue()));
                    season1.setText(String.format("%s", snapshot.child("season_text").getValue()));
                    tcrop1.setText(String.format("%s", snapshot.child("profitable_crop_text").getValue()));
                    soil1.setText(String.format("%s", snapshot.child("soil_present_text").getValue()));
                    gprd1.setText(String.format("%s", snapshot.child("Average_growth_period_text").getValue()));
                    mpr1.setText(String.format("%s", snapshot.child("Market_price_text").getValue()));
                    pest1.setText(String.format("%s", snapshot.child("pesticides_text").getValue()));
                    pcost1.setText(String.format("%s", snapshot.child("Average_pesticides_cost_text").getValue()));
                    totar.setText(String.format("%s", snapshot.child("total_acre_text").getValue()));
                    wneed1.setText(String.format("%s", snapshot.child("water_needs_text").getValue()));
                    timer.setText(String.format("%s", snapshot.child("time_limit_text").getValue()));
                    dam1.setText(String.format("%s", snapshot.child("nearest_dam_text").getValue()));
                    assistant.setText(String.format("%s", snapshot.child("assistant_text").getValue()));
                    helpno1.setText(String.format("%s", snapshot.child("help_line_number_text").getValue()));
                    //fertllizer1.setText(String.format("%s", snapshot.child("fertilizer_text").getValue()));

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            cropref = FirebaseDatabase.getInstance().getReference("crops").child(language).child(crop);
            cropref.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    season.setText(String.format("%s", snapshot.child("season").getValue()));
                    Glide.with(MainActivity.this).load(String.format("%s", snapshot.child("crop_grow_pic").getValue())).into(image);
                    //image.setImageBitmap(getBitmapFromURL(String.format("%s", snapshot.child("crop_grow_pic").getValue())));
                    //byte[] decodedString = Base64.decode(String.format("%s", snapshot.child("crop_grow_pic").getValue()), Base64.DEFAULT);
                    // Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    //image.setImageBitmap(bmp);
                    soil.setText(String.format("%s", snapshot.child("soil").getValue()));
                    crops.setText(String.format("%s", snapshot.child("crop_name").getValue()));
                    wneed.setText(String.format("%s", snapshot.child("crop_water_needs").getValue()));
                    mpr.setText(String.format("%s", snapshot.child("market_price").getValue()));
                    pcost.setText(String.format("%s", snapshot.child("average_pesticide_cost_max").getValue()+"-"+snapshot.child("average_pesticide_cost_min").getValue()));
                    low= Integer.parseInt(snapshot.child("average_pesticide_cost_min").getValue().toString().replace("₹",""));
                    high=Integer.parseInt(snapshot.child("average_pesticide_cost_max").getValue().toString().replace("₹",""));
                    pest.setText(String.format("%s", snapshot.child("pesticides").getValue()));
                    gprd.setText(String.format("%s", snapshot.child("average_growth_period").getValue()));
                    fertilizer.setText(String.format("%s", snapshot.child("fertilizer").getValue()));

                    //Toast.makeText(getBaseContext(), "cloud loaded", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {



                }
            });

            placeref= FirebaseDatabase.getInstance().getReference("places").child(place);
            placeref.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    dam.setText(String.format("%s", snapshot.child(language).child("nearest_dams").getValue()));
                    helpno.setText(String.format("%s", snapshot.child("agri_helpline_number").getValue()));



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {



                }
            });

         /*   senref= FirebaseDatabase.getInstance().getReference();
            senref.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    currentTemperatureField.setText(String.format("%s", snapshot.child("ctemp").getValue())+"°C");
                    humidity_field.setText(String.format("%s", snapshot.child("chumi").getValue())+"%");



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {



                }
            });*/
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_LONG).show();
        }


        Toast.makeText(getBaseContext(), "Loaded", Toast.LENGTH_SHORT).show();
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src", src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap", "returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
            return null;
        }
    }

    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("Notifications Example")
                        .setContentText("This is a test notification");

        Intent notificationIntent = new Intent(MainActivity.this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
    protected void displayNotification() {
        Log.i("Start", "notification");

        /* Invoking the default notification service */
        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setContentTitle("Plantation Weather:");
        mBuilder.setContentText("Weather updates");
        mBuilder.setTicker("New Message Alert!");

        //Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.paddy_small);


        mBuilder.setSmallIcon(R.mipmap.ic_launcher);

        /* Increase notification number every time a new notification arrives */

        mBuilder.setNumber(++numMessages);

        /* Add Big View Specific Configuration */
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        String[] events = new String[6];
        events[0] = new String("Temperature: "+temp);
        events[1] = new String("Locality: "+city);
        events[2] = new String("Suggested crop :"+crop);
        events[3] = new String("Time: "+mTimeLeftInMillis);
        // Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle("Plantation Weather:");

        // Moves events into the big view
        for (int i=0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }

        mBuilder.setStyle(inboxStyle);

        /* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);

        /* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder.setContentIntent(resultPendingIntent);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        /* notificationID allows you to update the notification later on. */
        manager.notify(0, mBuilder.build());
    }


    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    //timer functions

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateButtons();
            }
        }.start();

        mTimerRunning = true;
        updateButtons();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateButtons();
    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        updateButtons();
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void updateButtons() {
        if (mTimerRunning) {
            mButtonStartPause.setBackgroundResource(R.drawable.ic_pause);
        } else {
            mButtonStartPause.setBackgroundResource(R.drawable.ic_power_button_outline);

            if (mTimeLeftInMillis < START_TIME_IN_MILLIS) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        updateButtons();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateButtons();
            } else {
                startTimer();
            }
        }
    }

    @Override
    public void onBackPressed() {


        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(getBaseContext(), "Tap back button again in order to exit", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }

    class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }



}