package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.databinding.FragmentFriendsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {

    FragmentFriendsBinding binding;
    String mailUser;
    private FirebaseDatabase database;

    public static FriendsFragment create(int index) {
        FriendsFragment f = new FriendsFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFriendsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        database = FirebaseDatabase.getInstance();
        mailUser = MainPresenter.getInstance().getMail();
        initRecyclerView();
        initialListOfAllPeople();
        initialListOfFriends();

        binding.subscribe.setOnClickListener(v -> {
            if (binding.actv.getText().toString().equals("")) {
                Toast.makeText(requireContext(), "Введите хотя бы одну букву для поиска! Подписаться не на кого!", Toast.LENGTH_SHORT).show();
            } else  {
            String email = binding.actv.getText().toString();
            addToFriends(email);}
        });
    }

    private void initUpdateAdapter(List list) {
        FriendsAdapter adapter = new FriendsAdapter(list);
        binding.recycleView.setAdapter(adapter);
        adapter.SetOnItemClickListener((view, position) -> {
            Log.d("TAGxxxx", "onDataChange: " + list.get(position));
            Intent intent = new Intent(requireActivity(), HisHerFilms.class);
            intent.putExtra("1",(String) list.get(position));
            startActivity(intent);
        });
    }

    private void initialListOfFriends() {
        DatabaseReference ref = database.getReference("emails").child(mailUser
                .replace(".","dot")).child("friends");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if  (snapshot.getValue() != null) {
                           List list = (ArrayList) snapshot.getValue();
                           initUpdateAdapter(list);
                       }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void addToFriends(String email) {
        DatabaseReference ref = database.getReference("emails")
                .child(mailUser.replace(".", "dot")).child("friends");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("TAG1xxxxx", "onDataChange: ");
                List list = new ArrayList();
                if (snapshot.getValue() != null) {
                    list.addAll((ArrayList) snapshot.getValue());
                }
                if (!list.contains(email) && !email.equals(mailUser) && !email.equals("")){
                list.add(email);} else {
                    Toast.makeText(requireContext(), "Такого Пользователя мы добавить не можем!", Toast.LENGTH_SHORT).show();
                }
                initUpdateAdapter(list);
                Log.d("TAG1xxxxx", "onDataChange: " + list);
                ref.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void initRecyclerView() {
        binding.recycleView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ViewGroup.LayoutParams lp = binding.recycleView.getLayoutParams();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        binding.recycleView.requestLayout();
        binding.recycleView.setLayoutManager(layoutManager);
    }


    private void initialListOfAllPeople() {
        database.getReference("UsersEmail")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            List list = (ArrayList) snapshot.getValue();
                            List list2 = new ArrayList();
                            for (Object o : list) {
                                if (o != null) {
                                    list2.add(o);
                                }
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(),
                                    android.R.layout.simple_list_item_1, list2);
                            binding.actv.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}
