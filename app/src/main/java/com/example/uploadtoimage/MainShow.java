package com.example.uploadtoimage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainShow extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Model> models;
    private String json_url="http://192:168.0.2/Android_Lab13/file.php";
    private String img_url="http://192:168.0.2/Android_Lab13/images/";
    Adapter adapter;
    JSONArray jsonArray;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_show);

        recyclerView = findViewById(R.id.Recycle);
        models=new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, json_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    jsonArray=new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++){
                        Model model=new Model();
                        jsonObject =jsonObject.getJSONObject(String.valueOf(i));
                        String img=img_url+jsonObject.getString("images");
                        model.SerURL(img);
                        models.add(model);
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter=new Adapter(getApplicationContext(),models);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainShow.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueu = Volley.newRequestQueue(MainShow.this);
        requestQueu.add(stringRequest);
    }
}