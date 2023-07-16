package com.example.workoutplanner.trainingData;

import android.util.Log;


import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GlobalTrainingData {
    private static GlobalTrainingData instance = null;
    private MutableLiveData<List<Training>> trainingsLiveData;
    private List<Training> trainings;
    private ListenerRegistration registration;

    private GlobalTrainingData() {
        trainingsLiveData = new MutableLiveData<>();
        trainings = new ArrayList<>();
        fetchTrainings();
    }

    public static synchronized GlobalTrainingData getInstance() {
        if (instance == null) {
            instance = new GlobalTrainingData();
        }
        return instance;
    }

    public MutableLiveData<List<Training>> getTrainingsLiveData() {
        return trainingsLiveData;
    }

    private void fetchTrainings() {
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
                    // Notify observers about the update
                    trainingsLiveData.postValue(trainings);
                });
    }
    public void removeListener() {
        if (registration != null) {
            registration.remove();
        }
    }
}
