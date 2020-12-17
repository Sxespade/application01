package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myapplication.databinding.ActivityEnterBinding;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EnterActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 111;
    private ActivityEnterBinding binding;
    GoogleSignInAccount account;
    GoogleSignInClient googleSignInClient;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        database = FirebaseDatabase.getInstance();

        binding.enterBtn.setVisibility(View.GONE);

        checkUserLoggedIn();
        initGoogleAuth();

        binding.enterBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("mail", account.getEmail());
            startActivity(intent);
        });

        binding.singOutButton.setOnClickListener(v -> {
            binding.enterBtn.setVisibility(View.GONE);
            binding.sign.setVisibility(View.VISIBLE);
            binding.sign.setEnabled(true);
            signOut();
        });

        binding.sign.setOnClickListener(v -> {
            signIn();
        });

        if (binding.enterBtn.getVisibility() == View.VISIBLE) {
            binding.sign.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void checkUserLoggedIn() {
        enableSign();
        account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            binding.enterBtn.setVisibility(View.VISIBLE);
            MainPresenter.getInstance().setMail(account.getEmail());
            disableSign();
            binding.sign.setEnabled(false);
            binding.enterBtn.setVisibility(View.VISIBLE);
        }
    }


    private void enableSign() {
        binding.sign.setEnabled(true);
        binding.singOutButton.setEnabled(false);
    }


    private void initGoogleAuth() {
        binding.sign.setVisibility(View.VISIBLE);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        binding.singOutButton.setOnClickListener(v -> signOut());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Log.d("TAG111", "handleSignInResult1: " + data.getData());
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        disableSign();
        binding.sign.setVisibility(View.GONE);
    }

    private void initTakeUserMAil(String mail) {
        DatabaseReference ref = database.getReference().child("UsersEmail");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List list2 = new ArrayList();
                if (snapshot.getValue() != null) {
                    List list = (ArrayList) snapshot.getValue();
                    list2.addAll(list);
                }
                if (!list2.contains(mail)) {
                list2.add(mail);}
                Log.d("TAG1111", "onDataChange: " + list2);
                ref.setValue(list2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);
            MainPresenter.getInstance().setMail(account.getEmail());
            Log.d("TAG111", "handleSignInResult: ");
            binding.enterBtn.setVisibility(View.VISIBLE);
            initTakeUserMAil(account.getEmail());
            disableSign();
        } catch (ApiException e) {
            Log.w("TAG222", "signInResult:failed code=" + e.getStatusCode());
        }

    }


    private void signOut() {
        googleSignInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    MainPresenter.getInstance().setMail("mail");
                    enableSign();
                });
    }


    private void disableSign() {
        binding.sign.setEnabled(false);
        binding.singOutButton.setEnabled(true);
    }


}