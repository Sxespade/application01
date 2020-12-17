package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.DAO.FilmsDao;
import com.example.myapplication.databinding.FragmentMainWindowBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainWindowFragment extends Fragment {

    FragmentMainWindowBinding binding;
    private FilmSource filmSource;


    public static MainWindowFragment create(int index) {
        MainWindowFragment f = new MainWindowFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainWindowBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        initDataBase();


        binding.logOut.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(),EnterActivity.class);
            startActivity(intent);
        });

        if (filmSource.getFilms().size() > 0) {
            Film film1 = filmSource.getFilms().get(0);
            binding.title1.setText(film1.title);
            binding.year1.setText(film1.year);

            byte[] bitmapData = film1.image;
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);

            binding.imgFilm1.post(() -> Glide.with(requireContext())
                    .load(bitmap)
                    .apply(new RequestOptions().override(150, 120))
                    .into(binding.imgFilm1));

        }
        if (filmSource.getFilms().size() > 1) {
            Film film2 = filmSource.getFilms().get(1);
            binding.title2.setText(film2.title);
            binding.year2.setText(film2.year);

            byte[] bitmapData = film2.image;
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);

            binding.imgFilm2.post(() -> Glide.with(requireContext())
                    .load(bitmap)
                    .apply(new RequestOptions().override(150, 120))
                    .into(binding.imgFilm2));
        }
        if (filmSource.getFilms().size() > 2) {
            Film film3 = filmSource.getFilms().get(2);
            binding.title3.setText(film3.title);
            binding.year3.setText(film3.year);

            byte[] bitmapData = film3.image;
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);

            binding.imgFilm3.post(() -> Glide.with(requireContext())
                    .load(bitmap)
                    .apply(new RequestOptions().override(150, 120))
                    .into(binding.imgFilm3));
        }

    }

    private void initDataBase() {
        FilmsDao filmDao = App
                .getInstance()
                .getEducationDao();
        filmSource = new FilmSource(filmDao);
    }


}
