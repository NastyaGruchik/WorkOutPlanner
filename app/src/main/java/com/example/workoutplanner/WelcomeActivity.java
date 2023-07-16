package com.example.workoutplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.workoutplanner.trainingData.Training;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;



import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WelcomeActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth authProfiles;
    private static final String TAG = "WelcomeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        authProfiles = FirebaseAuth.getInstance();
      // addDataClasses();
        //open login activity
        Button buttonLogin = findViewById(R.id.button1_login);
        buttonLogin.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
            startActivity(intent);
        });

        //open register activity
        Button buttonRegister = findViewById(R.id.button1_register);
        buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this,RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void addDataClasses() {
        String yoga = "A relaxing class focusing on flexibility and strength.";
        String crossfit = "A high-intensity workout that combines elements of cardio, weight lifting, and core training.";
        String hiit = "High-Intensity Interval Training.";
        String pilates = "A class to improve flexibility, strength, and body awareness.";
        String zumba = "A high-intensity workout that combines elements of cardio, weight lifting, and core training.";
        String kickBoxing = "High-Intensity Interval Training, short and intense bursts of exercise followed by short recovery periods.";

        List<Training> trainings = Arrays.asList(
                new Training("Yoga", yoga, LocalDateTime.of(2023,7,16,9,0)),
                new Training("Crossfit",crossfit,LocalDateTime.of(2023,7,16,18,0)),
                new Training("HIIT", hiit, LocalDateTime.of(2023,7,16,19,0)),
                new Training("Pilates",pilates, LocalDateTime.of(2023,7,16,20,0)),
                new Training("Yoga", yoga, LocalDateTime.of(2023,7,16,21,0)),

                new Training("Yoga", yoga, LocalDateTime.of(2023,7,17,9,0)),
                new Training("Zumba", zumba,LocalDateTime.of(2023,7,17,18,0)),
                new Training("KickBoxing", kickBoxing, LocalDateTime.of(2023,7,17,19,0)),
                new Training("Crossfit",crossfit, LocalDateTime.of(2023,7,17,20,0)),
                new Training("Yoga", yoga, LocalDateTime.of(2023,7,17,21,0)),

                new Training("Yoga", yoga, LocalDateTime.of(2023,7,18,9,0)),
                new Training("Zumba", zumba,LocalDateTime.of(2023,7,18,18,0)),
                new Training("HIIT", hiit, LocalDateTime.of(2023,7,18,19,0)),
                new Training("KickBoxing",kickBoxing, LocalDateTime.of(2023,7,18,20,0)),
                new Training("Yoga", yoga, LocalDateTime.of(2023,7,18,21,0)),

                new Training("Yoga", yoga, LocalDateTime.of(2023,7,19,9,0)),
                new Training("Crossfit", crossfit,LocalDateTime.of(2023,7,19,18,0)),
                new Training("KickBoxing", kickBoxing, LocalDateTime.of(2023,7,19,19,0)),
                new Training("Pilates",pilates, LocalDateTime.of(2023,7,19,20,0)),
                new Training("Yoga", yoga, LocalDateTime.of(2023,7,19,21,0)),

                new Training("Yoga", yoga, LocalDateTime.of(2023,7,20,9,0)),
                new Training("HIIT", hiit,LocalDateTime.of(2023,7,20,18,0)),
                new Training("Zumba", zumba, LocalDateTime.of(2023,7,20,19,0)),
                new Training("Pilates",pilates, LocalDateTime.of(2023,7,20,20,0)),
                new Training("Yoga", yoga, LocalDateTime.of(2023,7,20,21,0)),

                new Training("Yoga", yoga, LocalDateTime.of(2023,7,21,7,0)),
                new Training("Pilates", pilates,LocalDateTime.of(2023,7,21,8,0)),
                new Training("KickBoxing", kickBoxing, LocalDateTime.of(2023,7,21,9,0)),
                new Training("Crossfit",crossfit, LocalDateTime.of(2023,7,21,10,0)),

                new Training("Yoga", yoga, LocalDateTime.of(2023,7,23,9,0)),
                new Training("Crossfit",crossfit,LocalDateTime.of(2023,7,23,18,0)),
                new Training("HIIT", hiit, LocalDateTime.of(2023,7,23,19,0)),
                new Training("Pilates",pilates, LocalDateTime.of(2023,7,23,20,0)),
                new Training("Yoga", yoga, LocalDateTime.of(2023,7,23,21,0)),

                new Training("Yoga", yoga, LocalDateTime.of(2023,7,24,9,0)),
                new Training("Zumba", zumba,LocalDateTime.of(2023,7,24,18,0)),
                new Training("KickBoxing", kickBoxing, LocalDateTime.of(2023,7,24,19,0)),
                new Training("Crossfit",crossfit, LocalDateTime.of(2023,7,24,20,0)),
                new Training("Yoga", yoga, LocalDateTime.of(2023,7,24,21,0)),

                new Training("Yoga", yoga, LocalDateTime.of(2023,7,25,9,0)),
                new Training("Zumba", zumba,LocalDateTime.of(2023,7,25,18,0)),
                new Training("HIIT", hiit, LocalDateTime.of(2023,7,25,19,0)),
                new Training("KickBoxing",kickBoxing, LocalDateTime.of(2023,7,25,20,0)),
                new Training("Yoga", yoga, LocalDateTime.of(2023,7,25,21,0)),

                new Training("Yoga", yoga, LocalDateTime.of(2023,7,26,9,0)),
                new Training("Crossfit", crossfit,LocalDateTime.of(2023,7,26,18,0)),
                new Training("KickBoxing", kickBoxing, LocalDateTime.of(2023,7,26,19,0)),
                new Training("Pilates",pilates, LocalDateTime.of(2023,7,26,20,0)),
                new Training("Yoga", yoga, LocalDateTime.of(2023,7,26,21,0)),

                new Training("Yoga", yoga, LocalDateTime.of(2023,7,27,9,0)),
                new Training("HIIT", hiit,LocalDateTime.of(2023,7,27,18,0)),
                new Training("Zumba", zumba, LocalDateTime.of(2023,7,27,19,0)),
                new Training("Pilates",pilates, LocalDateTime.of(2023,7,27,20,0)),
                new Training("Yoga", yoga, LocalDateTime.of(2023,7,27,21,0)),

                new Training("Yoga", yoga, LocalDateTime.of(2023,7,28,7,0)),
                new Training("Pilates", pilates,LocalDateTime.of(2023,7,28,8,0)),
                new Training("KickBoxing", kickBoxing, LocalDateTime.of(2023,7,28,9,0)),
                new Training("Crossfit",crossfit, LocalDateTime.of(2023,7,28,10,0)),

                new Training("Yoga", yoga, LocalDateTime.of(2023,7,30,9,0)),
                new Training("Crossfit",crossfit,LocalDateTime.of(2023,7,30,18,0)),
                new Training("HIIT", hiit, LocalDateTime.of(2023,7,30,19,0)),
                new Training("Pilates",pilates, LocalDateTime.of(2023,7,30,20,0)),
                new Training("Yoga", yoga, LocalDateTime.of(2023,7,30,21,0)),

                new Training("Yoga", yoga, LocalDateTime.of(2023,8,31,9,0)),
                new Training("Zumba", zumba,LocalDateTime.of(2023,8,31,18,0)),
                new Training("KickBoxing", kickBoxing, LocalDateTime.of(2023,8,31,19,0)),
                new Training("Crossfit",crossfit, LocalDateTime.of(2023,8,31,20,0)),
                new Training("Yoga", yoga, LocalDateTime.of(2023,8,31,21,0)),

                new Training("Yoga", yoga, LocalDateTime.of(2023,8,1,9,0)),
                new Training("Zumba", zumba,LocalDateTime.of(2023,8,1,18,0)),
                new Training("HIIT", hiit, LocalDateTime.of(2023,8,1,19,0)),
                new Training("KickBoxing",kickBoxing, LocalDateTime.of(2023,8,1,20,0)),
                new Training("Yoga", yoga, LocalDateTime.of(2023,8,1,21,0)),

                new Training("Yoga", yoga, LocalDateTime.of(2023,8,2,9,0)),
                new Training("Crossfit", crossfit,LocalDateTime.of(2023,8,2,18,0)),
                new Training("KickBoxing", kickBoxing, LocalDateTime.of(2023,8,2,19,0)),
                new Training("Pilates",pilates, LocalDateTime.of(2023,8,2,20,0)),
                new Training("Yoga", yoga, LocalDateTime.of(2023,8,2,21,0)),

                new Training("Yoga", yoga, LocalDateTime.of(2023,8,3,9,0)),
                new Training("HIIT", hiit,LocalDateTime.of(2023,8,3,18,0)),
                new Training("Zumba", zumba, LocalDateTime.of(2023,8,3,19,0)),
                new Training("Pilates",pilates, LocalDateTime.of(2023,8,3,20,0)),
                new Training("Yoga", yoga, LocalDateTime.of(2023,8,3,21,0)),

                new Training("Yoga", yoga, LocalDateTime.of(2023,8,4,7,0)),
                new Training("Pilates", pilates,LocalDateTime.of(2023,8,4,8,0)),
                new Training("KickBoxing", kickBoxing, LocalDateTime.of(2023,8,4,9,0)),
                new Training("Crossfit",crossfit, LocalDateTime.of(2023,8,4,10,0))
        );

        //save to firebase


        // Convert each training to a Map so it can be saved in Firestore.
        List<Map<String, Object>> trainingMaps = new ArrayList<>();
        for (Training training : trainings) {
            Map<String, Object> trainingMap = new HashMap<>();
            trainingMap.put("id", training.getId());
            trainingMap.put("name", training.getName());
            trainingMap.put("description", training.getDescription());  // Assuming `getTrainer()` returns a String.
            trainingMap.put("registeredNum",training.getRegisteredNum());
            trainingMap.put("registeredUsers",training.getRegisteredUsers());
            trainingMap.put("waitingListUsers",training.getWaitingListUsers());
            // Convert LocalDateTime to java.util.Date then to Timestamp.
            Instant instant = training.getDateTime().atZone(ZoneId.systemDefault()).toInstant();
            java.util.Date date = Date.from(instant);
            trainingMap.put("date", new java.sql.Timestamp(date.getTime()));
            trainingMaps.add(trainingMap);
        }
        for (Map<String, Object> trainingMap : trainingMaps) {
            // Notice that we removed the `document()` call with the ID argument.
            db.collection("trainings")
                    .add(trainingMap)  // Add the new document with an auto-generated ID.
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());

                            // If you want to use the generated ID for something, you can get it from the `documentReference` object:
                            String generatedId = documentReference.getId();
                            // You can now use `generatedId` for your purposes. For example, if you want to add it back to the document:
                            documentReference.update("uId", generatedId);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(authProfiles.getCurrentUser()!=null){
            Toast.makeText(WelcomeActivity.this,"Welcome back!",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(WelcomeActivity.this,UserProfileActivity.class));
            finish();
        }
        if (ContextCompat.checkSelfPermission(WelcomeActivity.this, android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // If not, ask for it
            ActivityCompat.requestPermissions( WelcomeActivity.this, new String[]{Manifest.permission.WRITE_CALENDAR}, 0);
    }
}

}