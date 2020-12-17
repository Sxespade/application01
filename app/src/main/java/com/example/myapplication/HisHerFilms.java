package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.exifinterface.media.ExifInterface;

import com.bumptech.glide.Glide;
import com.example.myapplication.databinding.ActivityHisHerFilmsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HisHerFilms extends AppCompatActivity {

    private FirebaseDatabase database;
    ActivityHisHerFilmsBinding binding;
    List listOfFilmsTitles = new ArrayList();
    public static final String GSTORAGE = "gs://horrors-8ac6c.appspot.com/";
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_his_her_films);

        binding = ActivityHisHerFilmsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.imageButton2.setOnClickListener(v -> {onBackPressed();});

        binding.constr1.setVisibility(View.INVISIBLE);
        binding.constr2.setVisibility(View.INVISIBLE);
        binding.constr3.setVisibility(View.INVISIBLE);

        database = FirebaseDatabase.getInstance();


        if (getIntent().getExtras().get("1") != null) {
            str = (String) getIntent().getExtras().get("1");
            binding.textView23.setText(str);
        }

        initDrawFilms(str,1,binding.image1,binding.title1,binding.constr1);
        initDrawFilms(str,2,binding.image2,binding.title2,binding.constr2);
        initDrawFilms(str,3,binding.image3,binding.title3,binding.constr3);
    }

    private void initDrawFilms(String str,int numOfRecFilm,ImageView imgPoster, TextView textView, LinearLayout cl) {
        DatabaseReference ref = database.getReference().child("emails")
                .child(str.replace(".", "dot"))
                .child("rec_film" + numOfRecFilm);
        Log.d("xxxxx", "onDataChange: " + ref);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("xxxxx", "onDataChange: ");
                if (snapshot.getValue() != null) {
                    Map map = (HashMap) snapshot.getValue();
                    cl.setVisibility(View.VISIBLE);
                    Log.d("xxxxx", "onDataChange: " + map.get("title"));
                    textView.setText(map.get("title").toString());
                    String str = (String) map.get("image");
                    Glide.with(getApplicationContext())
                            .load(str)
                            .into(imgPoster);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}