package com.example.workoutplanner.ui.logOut;



import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.example.workoutplanner.R;
import com.example.workoutplanner.WelcomeActivity;
import com.google.firebase.auth.FirebaseAuth;


public class LogOutFragment extends Fragment {

    private FirebaseAuth authProfiles;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        authProfiles = FirebaseAuth.getInstance();
        View root = inflater.inflate(R.layout.fragment_log_out, container, false);
        Button logOutButton = root.findViewById(R.id.button_logOut);
        logOutButton.setOnClickListener(v -> {
            authProfiles.signOut();

            Intent intent = new Intent(getActivity(), WelcomeActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return root;
    }
}