package com.example.workoutplanner.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.workoutplanner.R;
import com.example.workoutplanner.databinding.FragmentHomeBinding;
import com.example.workoutplanner.trainingData.GlobalTrainingData;
import com.example.workoutplanner.trainingData.Training;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    TextView textViewHomeProgramName,textViewProgramHomeDescription,textViewHomeProgramTime;
    private FirebaseUser firebaseUser;
    private String userId;
    private FragmentHomeBinding binding;
    GlobalTrainingData globalTrainingData;
    private int trainingId;
    List<Integer> trainingIds1 = new ArrayList<>();
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Registered Users");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textViewHomeProgramName= root.findViewById(R.id.textViewHomeProgramName);
        textViewProgramHomeDescription = root.findViewById(R.id.textViewProgramHomeDescription);
        textViewHomeProgramTime = root.findViewById(R.id.textViewHomeProgramTime);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser != null ? firebaseUser.getUid() : null;
        DatabaseReference userTrainingRef = databaseReference.child(userId).child("trainingIds");

        userTrainingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Integer> trainingIds = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int trainingId = snapshot.getValue(Integer.class);
                    trainingIds.add(trainingId);
                }
                Integer minId = findMinAfterMinusOne(trainingIds);
                if (minId != null) {
                    trainingId = minId;
                }
                trainingIds1 = trainingIds;
            }

            private Integer findMinAfterMinusOne(List<Integer> trainingIds) {
                return trainingIds.stream()
                        .filter(id -> id > -1)
                        .min(Integer::compareTo)
                        .orElse(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error handling
            }
        });


        globalTrainingData = GlobalTrainingData.getInstance();
        globalTrainingData.getTrainingsLiveData().observe(getActivity(), new Observer<List<Training>>() {
            @Override
            public void onChanged(List<Training> trainings) {
                Integer desiredTrainingId = findMinAfterMinusOne(trainingIds1);
                if (desiredTrainingId != null) {
                    for (Training training : trainings) {
                        if (training.getId() == desiredTrainingId) {
                            textViewHomeProgramName.setText(training.getName());
                            textViewProgramHomeDescription.setText(training.getDescription());
                            textViewHomeProgramTime.setText(training.getDateTime().toString());
                            break; // Exit the loop since we found the desired training
                        }
                    }
                }
            }

            private Integer findMinAfterMinusOne(List<Integer> trainingIds) {
                return trainingIds.stream()
                        .filter(id -> id > -1)
                        .min(Integer::compareTo)
                        .orElse(null);
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        globalTrainingData.removeListener();
        binding = null;

    }
}