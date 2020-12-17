package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.DAO.FilmsDao;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.databinding.ActivitySelectFilmBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SelectFilm extends AppCompatActivity {

    OpenFilm openFilm;
    ActivitySelectFilmBinding binding;
    private final String APIKEY = "294fc5fe";
    private FilmSource filmSource;
    private String userMail = MainPresenter.getInstance().getMail();
    private FirebaseDatabase database;
    private String num;
    private String image;
    private String startFilm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectFilmBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        startFilm = binding.title.getText().toString();


        binding.imageButton2.setOnClickListener(v -> {
            onBackPressed();
        });


        Glide.with(getApplicationContext())
                .load(R.drawable.loader)
                .apply(new RequestOptions().override(150, 120))
                .into(binding.imageButton);


        database = FirebaseDatabase.getInstance();

        num = (String) getIntent().getExtras().get("num");

        FilmsDao filmDao = App
                .getInstance()
                .getEducationDao();
        filmSource = new FilmSource(filmDao);


        binding.find.setOnClickListener(v -> {
            if (binding.title != null && !binding.title.equals("")) {
                filmSource.removeFilmByTitle(binding.title.getText().toString());
            }


            if (binding.autoCompleteTextView != null && !binding.autoCompleteTextView.equals("")) {
                initRetorfit();
                requestRetrofit(binding.autoCompleteTextView.getText().toString(), APIKEY);
                binding.title.setVisibility(View.VISIBLE);
                binding.year.setVisibility(View.VISIBLE);
                binding.imageButton.setVisibility(View.VISIBLE);
                binding.button.setVisibility(View.VISIBLE);
            }
            hideKeyboard();

            binding.button.setVisibility(View.INVISIBLE);
            binding.button.setVisibility(View.VISIBLE);
        });

        byte[] b = (byte[]) getIntent().getExtras().get("image");
        if (b != null) {
            Bitmap bitmp = BitmapFactory.decodeByteArray(b, 0, b.length);
            Glide.with(this).load(bitmp).into(binding.imageButton);
            binding.title.setVisibility(View.VISIBLE);
            binding.title.setText(getIntent().getExtras().get("title").toString());
            binding.year.setVisibility(View.VISIBLE);
            binding.year.setText(getIntent().getExtras().get("year").toString());
        }

        binding.addDel.setOnClickListener(v -> {
            if (binding.title != null && !binding.title.equals("")) {
                filmSource.removeFilmByTitle(binding.title.getText().toString());
            }
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("key1", "value1");
            startActivity(intent);
        });

        binding.button.setOnClickListener((v -> {

            Film film = new Film();
            film.title = binding.title.getText().toString();
            film.year = binding.year.getText().toString();


            Drawable d = binding.imageButton.getDrawable();

            if (d != null && !binding.autoCompleteTextView.equals("")) {
                Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                film.image = stream.toByteArray();

                int n = 0;

                for (Film filmSourceFilm : filmSource.getFilms()) {
                    if (filmSourceFilm.title.equals(film.title)) {
                        n++;
                    }
                }

                Log.d("TAGxxx", "onCreate: " + n);

                if (n == 0) {
                    filmSource.addFilm(film);
                    DatabaseReference ref = database.getReference().child("emails").child(userMail.replace(".", "dot"))
                            .child("rec_film" + num).child("title");
                    ref.setValue(film.title);

                    DatabaseReference ref2 = database.getReference().child("emails").child(userMail.replace(".", "dot"))
                            .child("rec_film" + num).child("year");
                    ref2.setValue(film.year);

                    DatabaseReference ref3 = database.getReference().child("emails").child(userMail.replace(".", "dot"))
                            .child("rec_film" + num).child("image");
                    ref3.setValue(image);
                }
            }


            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("key1", "value1");

            startActivity(intent);

        }));


    }


    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    private void initRetorfit() {
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl("http://omdbapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        openFilm = retrofit.create(OpenFilm.class);

    }

    private void requestRetrofit(String m, String keyApi) {
        openFilm.loadFilmName(m, keyApi)
                .enqueue(new Callback<FilmRequest>() {
                    @Override
                    public void onResponse(Call<FilmRequest> call, Response<FilmRequest> response) {
                        if (response.body() != null)
                            binding.title.setVisibility(View.VISIBLE);
                        binding.year.setVisibility(View.VISIBLE);
                        binding.title.setText(response.body().getTitle());
                        binding.year.setText(response.body().getYear());
                        image = response.body().getPoster();

                        new Thread(() -> {
                            Bitmap bitmap = null;
                            try {
                                bitmap = BitmapFactory.decodeStream((InputStream) new URL(response.body().getPoster()).getContent());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Bitmap finalBitmap = bitmap;
                            binding.imageButton.post(() -> Glide.with(getApplicationContext())
                                    .load(finalBitmap)
                                    .apply(new RequestOptions().override(150, 120))
                                    .into(binding.imageButton));


                        }).start();

                    }

                    @Override
                    public void onFailure(Call<FilmRequest> call, Throwable t) {
                        Log.d("TAG444", "onFailure: " + t);
                        binding.title.setText("Error");
                    }
                });

    }

}