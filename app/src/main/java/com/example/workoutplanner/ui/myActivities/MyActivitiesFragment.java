package com.example.workoutplanner.ui.myActivities;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.workoutplanner.ReadWriteUserDetails;
import com.example.workoutplanner.databinding.FragmentMyActivitiesBinding;
import com.example.workoutplanner.trainingData.GlobalTrainingData;
import com.example.workoutplanner.trainingData.RegisteredTrainingAdapter;
import com.example.workoutplanner.trainingData.Training;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MyActivitiesFragment extends Fragment {

    private FragmentMyActivitiesBinding binding;
    private RegisteredTrainingAdapter adapter;
    private FirebaseUser firebaseUser;
    private String userId;
    private List<Training> trainings = new ArrayList<>();;
    private ListenerRegistration registration;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Registered Users");
    List<Training> registeredTrainings = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMyActivitiesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser != null ? firebaseUser.getUid() : null;

        // Fetch trainings from Firebase
        fetchRegisteredTrainings();

        return root;
    }

    private void fetchRegisteredTrainings() {

        DatabaseReference userTrainingRef = databaseReference.child(userId).child("trainingIds");

        databaseReference.child(userId).get().addOnSuccessListener(snapshot -> {
            ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);
            if (userDetails != null) {
                List<Integer> trainingIds = userDetails.getTrainingIds();
                fetchTrainings(trainingIds);
            }
        }).addOnFailureListener(e -> {
            // TODO: Handle error and show message to the user
        });
    }

    private void fetchTrainings(List<Integer> trainingIds) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        registration = db.collection("trainings")
                .orderBy("id")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w("GlobalTrainingData", "Listen failed.", error);
                        return;
                    }

                    // Clear the old list
                    trainings.clear();

                    for (QueryDocumentSnapshot doc : value) {
                        Training training = doc.toObject(Training.class);
                        training.returnToLocalDateTime(doc.getTimestamp("date"));

                        training.setUid(doc.getId());
                        if (training.getDateTime().isAfter(LocalDateTime.now()) && training.getDateTime().isBefore(LocalDateTime.now().plusWeeks(2)))
                            trainings.add(training);
                    }

                    for (Integer trainingId : trainingIds) {
                        for (int i=0;i<trainings.size();i++){
                            if ( trainingId == trainings.get(i).getId()){
                                registeredTrainings.add(trainings.get(i));
                            }
                        }
                    }
                    // Update the RecyclerView after all data is loaded and processed
                    adapter = new RegisteredTrainingAdapter(registeredTrainings);
                    binding.recyclerViewMyActivities.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerViewMyActivities.setAdapter(adapter);
                });
    }

}
