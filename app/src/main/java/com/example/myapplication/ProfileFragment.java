package com.example.myapplication;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.DAO.FilmsDao;
import com.example.myapplication.databinding.FragmentProfileBinding;

import org.w3c.dom.Text;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    FragmentProfileBinding binding;
    String mail = MainPresenter.getInstance().getMail();
    private FilmSource filmSource;

    public static ProfileFragment create(int index) {
        ProfileFragment f = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        threadWithEmptyPict();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        initDataBase();


        takePictFromDatabase();
        onClickThis();
    }

    private void initDataBase() {
        FilmsDao filmDao = App
                .getInstance()
                .getEducationDao();
        filmSource = new FilmSource(filmDao);
    }


    private void onClickThis() {
        binding.addFilm1.setOnClickListener(this);
        binding.addFilm2.setOnClickListener(this);
        binding.addFilm3.setOnClickListener(this);
        binding.mainLinear.setOnClickListener(this);
    }

    private void threadWithEmptyPict() {
        initEmptyFilm();

    }

    private void takePictFromDatabase() {
        Thread.yield();
        initDataBase();
        List<Film> films = filmSource.getFilms();
        if (films != null && films.size() != 0) {
            if (films.get(0) != null) {
            byte[] bitmapData = films.get(0).image;
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);

            binding.addFilm1.post(() -> Glide.with(requireContext())
                    .load(bitmap)
                    .apply(new RequestOptions().override(150, 120))
                    .into(binding.addFilm1));

            binding.title1.setText(films.get(0).title);
            binding.year1.setText(films.get(0).year);}

            if (films.size() > 1 && films.get(1) != null) {
                byte[] bitmapData = films.get(1).image;
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);

                binding.addFilm2.post(() -> Glide.with(requireContext())
                        .load(bitmap)
                        .apply(new RequestOptions().override(150, 120))
                        .into(binding.addFilm2));

                binding.title2.setText(films.get(1).title);
                binding.year2.setText(films.get(1).year);}


        if (films.size() > 2 && films.get(2) != null) {
            byte[] bitmapData = films.get(2).image;
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);

            binding.addFilm3.post(() -> Glide.with(requireContext())
                    .load(bitmap)
                    .apply(new RequestOptions().override(150, 120))
                    .into(binding.addFilm3));

            binding.title3.setText(films.get(2).title);
            binding.year3.setText(films.get(2).year);}
        }
    }


    private void initEmptyFilm() {
        takePickEmptyFilmInto(binding.addFilm1);
        takePickEmptyFilmInto(binding.addFilm2);
        takePickEmptyFilmInto(binding.addFilm3);
    }

    private void takePickEmptyFilmInto(ImageView imageView) {
        Glide.with(requireContext())
                .load(R.drawable.add_film)
                .into(imageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addFilm1:
                runIntent("1",binding.title1,binding.year1);
                Log.d("TAGppp", "onClick: ");
                break;
            case R.id.addFilm2:
                runIntent("2",binding.title2,binding.year2);
                break;
            case R.id.addFilm3:
                runIntent("3",binding.title3,binding.year3);
                break;
//            case R.id.main_linear:
//                binding.addFilm1.setImageAlpha(0);
//                break;
        }

    }

    private void runIntent(String s, TextView title, TextView year) {
        Intent intent = new Intent(requireContext(), SelectFilm.class);
        Film film = filmSource.selectByTitle(title.getText().toString());
        intent.putExtra("mail", mail);
        intent.putExtra("num", s);
        if (film != null && film.image != null) {
        intent.putExtra("image", film.image);}
        intent.putExtra("title",title.getText().toString());
        intent.putExtra("year",year.getText().toString());
        startActivity(intent);
    }
}