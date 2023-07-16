package com.example.workoutplanner.ui.profile;



import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.workoutplanner.R;
import com.example.workoutplanner.ReadWriteUserDetails;
import com.example.workoutplanner.databinding.FragmentProfileBinding;
import com.example.workoutplanner.trainingData.GlobalTrainingData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ProfileFragment extends Fragment {
    private FirebaseUser firebaseUser;
    private String userId;
    private FragmentProfileBinding binding;
    TextView profileName,profileEmail,profileDOB,profileUpcomingSessions;

    final private static String TAG = "ProfileFragment";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser != null ? firebaseUser.getUid() : null;
        profileName = view.findViewById(R.id.profileName);
        profileEmail= view.findViewById(R.id.profileEmail);
        profileDOB=view.findViewById(R.id.profileDOB);
        profileUpcomingSessions = view.findViewById(R.id.profileUpcomingSessions);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

            referenceProfile.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ReadWriteUserDetails userDetails = dataSnapshot.getValue(ReadWriteUserDetails.class);

                    if (userDetails != null) {

                        String fullName = userDetails.getFullName();
                        String doB = userDetails.getDoB();
                        String email = userDetails.getEmail();
                        ArrayList<Integer> trainingIds = userDetails.getTrainingIds();
                        int upcomingSessions = trainingIds.size()-1;

                        profileName.setText(fullName);
                        profileEmail.setText(email);
                        profileDOB.setText(doB);
                        profileUpcomingSessions.setText(""+upcomingSessions);

                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GlobalTrainingData.getInstance().removeListener();
        binding = null;
    }

}