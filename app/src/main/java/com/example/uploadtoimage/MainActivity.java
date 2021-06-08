package com.example.uploadtoimage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button browser,upload;
    ImageView imageView;
    final int code_gallery_code=999;
    String url="http://192:168.0.2/Android_Lab13/upload.php";
    Bitmap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        browser=(Button) findViewById(R.id.brw);
        upload=(Button) findViewById(R.id.upl);
        imageView=(ImageView) findViewById(R.id.img);

        browser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==browser){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},code_gallery_code);
        }
        if(v==upload){
            StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(MainActivity.this,response,Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String>params=new HashMap<>();
                    String imgData=imageToString(map);
                    params.put("image",imgData);
                    return super.getHeaders();
                }
            };
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == code_gallery_code){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent.createChooser(intent,"Select Image"),requestCode);
            }else{
                Toast.makeText(MainActivity.this,"Premission Denited",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == code_gallery_code && resultCode == RESULT_OK && data != null){
            Uri filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                map= BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(map);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imageBytes=outputStream.toByteArray();
        String encodeImage= Base64.encodeToString(imageBytes,Base64.DEFAULT);
        return  encodeImage;
    }
}