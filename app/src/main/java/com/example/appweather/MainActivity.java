package com.example.appweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

public class MainActivity extends AppCompatActivity {
    EditText editTextSeach;
    Button buttonSeach;
    TextView    textViewName,textViewCountry,textTemp,textStatus,textViewHumdity,
            textViewSubCloud,textViewWind,textViewDay;
    ImageView imgCloud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Tools();
        buttonSeach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = editTextSeach.getText().toString();
                getWeatherData(city);
            } // get data từ text
        }); // bắt sự kiện button seach
    }
    private void Tools() {
        editTextSeach   = (EditText) findViewById(R.id.editTextSeach);
        buttonSeach     = (Button)   findViewById(R.id.buttonSeach);

        textViewName    =   (TextView) findViewById(R.id.textViewName);
        textViewCountry    = (TextView) findViewById(R.id.textViewCountry);
        textTemp        =      (TextView) findViewById(R.id.textTemp);
        textStatus    = (TextView) findViewById(R.id.textStatus);
        textViewHumdity    = (TextView) findViewById(R.id.textViewHumdity);
        textViewSubCloud    = (TextView) findViewById(R.id.textViewSubCloud);
        textViewWind    = (TextView) findViewById(R.id.textViewWind);
        textViewDay    = (TextView) findViewById(R.id.textViewDay);

        imgCloud    = (ImageView) findViewById(R.id.imgCloud);
    } //Khai báo tên biến
    private void getWeatherData(String  data) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this); //thực thi yêu cầu khi mà mình sẽ gửi đi
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&appid=689354842d50d005cc13a72c2f0dfdd9";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject =new JSONObject(response);
                                String day = jsonObject.getString("dt");
                                String name = jsonObject.getString("name");
                                textViewName.setText("Thành Phố : " + name);

                                long l=Long.valueOf(day);
                                Date date   = new Date(l*1000);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy HH-mm");
                                String Day = simpleDateFormat.format(date);
                                textViewDay.setText(Day);


                            JSONArray  jsonArrayWeather = jsonObject.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                    String icon = jsonObjectWeather.getString("icon");
                                    String status = jsonObjectWeather.getString("main");
                                    Picasso.get()
                                            .load("http://openweathermap.org/img/wn/"+icon+".png")
                                            .into(imgCloud);
                                    textStatus.setText(status);


                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                                String nhietdo = jsonObjectMain.getString("temp");
                                String doam =   jsonObjectMain.getString("humidity");

                                Double a = Double.valueOf(nhietdo); //làm tròn độ c
                                String NhietDo = String.valueOf(a.intValue());
                                textTemp.setText( NhietDo  +"°C");
                                textViewHumdity.setText(doam +"%");


                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                                String gio = jsonObjectWind.getString("speed");
                                textViewWind.setText(gio + "m/s");

                            JSONObject jsonObjectCloud = jsonObject.getJSONObject("clouds");
                                String may = jsonObjectCloud.getString("all");
                                textViewSubCloud.setText(may + "%");

                            JSONObject jsonObjectSys =jsonObject.getJSONObject("sys");
                                String country = jsonObjectSys.getString("country");
                                textViewCountry.setText("Quốc Gia : "+ country);

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                //trả về kết quả các giá trị trong JSON được lưu vào reponse
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
                //báo lỗi khi không được thực thi
        );
        requestQueue.add(stringRequest); // thực thi
    }
}