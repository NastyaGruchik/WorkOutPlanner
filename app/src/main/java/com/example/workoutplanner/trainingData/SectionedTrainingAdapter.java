package com.example.workoutplanner.trainingData;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutplanner.R;
import com.example.workoutplanner.ReadWriteUserDetails;
import com.example.workoutplanner.ReminderBroadcast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

public class SectionedTrainingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int SECTION_TYPE = 0;
    private static final int ITEM_TYPE = 1;
    private List<Training> trainings;

    private List<Object> sectionedData = new ArrayList<>();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

    private final String userId = firebaseUser != null ? firebaseUser.getUid() : null;
    DatabaseReference userTrainingRef = referenceProfile.child(userId).child("trainingIds");
    private final String yellow="#FFFF00";
    private final String green="#00C853";
    private final String red="#DD2C00";
    public SectionedTrainingAdapter(List<Training> trainings) {
        this.trainings = trainings;
        setData(trainings);
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Training> trainingData) {
        sectionedData.clear();
        trainingData.sort(Comparator.comparing(Training::getDateTime));

        LocalDate currentDate = null;
        for (Training training : trainingData) {
            LocalDate trainingDate = training.getDateTime().toLocalDate();
            if (!trainingDate.equals(currentDate)) {
                currentDate = trainingDate;
                sectionedData.add(currentDate);
            }
            sectionedData.add(training);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (sectionedData.get(position) instanceof LocalDate) {
            return SECTION_TYPE;
        } else {
            return ITEM_TYPE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SECTION_TYPE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.section_header, parent, false);
            return new SectionViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_activity, parent, false);
            return new TrainingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof SectionViewHolder) {
            ((SectionViewHolder) holder).bind((LocalDate) sectionedData.get(position));
        } else {
            Training training = (Training) sectionedData.get(position);
            TrainingViewHolder trainingHolder = (TrainingViewHolder) holder;
            trainingHolder.trainingName.setText(training.getName());
            trainingHolder.trainingDescription.setText(training.getDescription());
            trainingHolder.trainingRegisteredNum.setText("Registered:" + training.getRegisteredNum()+ " out of "+ training.getMaxParticipants());
            trainingHolder.trainingTime.setText(training.getHour()+":"+training.getMinutes());

            if(training.getRegisteredNum() >= training.getMaxParticipants()){
                trainingHolder.trainingButtonRegistration.setText("Join Waiting List");
                trainingHolder.trainingButtonRegistration.setBackgroundColor(Color.parseColor(yellow));
            }else{
                trainingHolder.trainingButtonRegistration.setText("Register");
                trainingHolder.trainingButtonRegistration.setBackgroundColor(Color.parseColor(green));
            }
            //user regestred already
            for(int i=0; i<training.getRegisteredUsers().size();i++){
                if(training.getRegisteredUsers().get(i).equals(userId)){
                    trainingHolder.trainingButtonRegistration.setText("Cancel");
                    trainingHolder.trainingButtonRegistration.setBackgroundColor(Color.parseColor(red));
                }
            }

            trainingHolder.trainingButtonRegistration.setOnClickListener(v -> {
                if(trainingHolder.trainingButtonRegistration.getText().equals("Register")){
                    registerForTraining(training.getId(), trainingHolder);
                } else if (trainingHolder.trainingButtonRegistration.getText().equals("Join Waiting List")) {
                    addWaitingList(training.getId(), trainingHolder);
                }else {//cancel
                    unRegisterForTraining(training.getId(),trainingHolder);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return sectionedData.size();
    }

    public static class TrainingViewHolder extends RecyclerView.ViewHolder {
        public TextView trainingName,trainingDescription,trainingTime,trainingRegisteredNum;
        public Button trainingButtonRegistration;

        public TrainingViewHolder(View view) {
            super(view);
            trainingName = view.findViewById(R.id.textViewProgramName);
            trainingDescription= view.findViewById(R.id.textViewProgramDescription);
            trainingTime=view.findViewById(R.id.textViewProgramTime);
            trainingRegisteredNum=view.findViewById(R.id.textViewProgramRegisteredNum);
            trainingButtonRegistration =view.findViewById(R.id.buttonProgramRegister);

        }
    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        public TextView dateHeader;

        public SectionViewHolder(View view) {
            super(view);
            dateHeader = view.findViewById(R.id.textViewDateHeader);
        }

        public void bind(LocalDate date) {
            dateHeader.setText(date.getDayOfWeek().toString()+ " "+date.toString()); // Modify this to display the date as you prefer
        }
    }
    private void unRegisterForTraining(int trainingId, TrainingViewHolder trainingHolder){
        referenceProfile.child(userId).get().addOnSuccessListener(snapshot -> {
            ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);
            if (userDetails != null) {
                Training training = null;
                for (int i=0;i<trainings.size();i++){
                    if (trainings.get(i).getId()==trainingId){
                        training= trainings.get(i);
                        break;
                    }
                }
                if(training!=null){
                    training.setRegisteredNum(training.getRegisteredNum()-1);
                    training.getRegisteredUsers().remove(userId);
                    DocumentReference trainingRef = db.collection("trainings").document(training.getUid());
                    trainingRef
                            .update("registeredNum", training.getRegisteredNum(),"registeredUsers",training.getRegisteredUsers())
                            .addOnSuccessListener(aVoid -> {
                                Log.d("TAG", "DocumentSnapshot successfully updated!");
                                Toast.makeText(trainingHolder.itemView.getContext(),"You cancel successfully", Toast.LENGTH_SHORT).show();
                                trainingHolder.trainingButtonRegistration.setBackgroundColor(Color.parseColor(green));
                                trainingHolder.trainingButtonRegistration.setText("Register");
                                unReminder(trainingHolder.itemView.getContext(),trainingId);
                                notifyDataSetChanged();
                            })
                            .addOnFailureListener(e -> {
                                Log.w("TAG", "Error updating document", e);
                                Toast.makeText(trainingHolder.itemView.getContext(),e.toString(), Toast.LENGTH_SHORT).show();

                            });
                    userTrainingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Get the current trainingIds
                                ArrayList<Object> trainingIdsObjects = (ArrayList<Object>) dataSnapshot.getValue();
                                ArrayList<Integer> trainingIds = new ArrayList<>();

                                // Convert the objects in the ArrayList to Integers
                                for (Object o : trainingIdsObjects) {
                                    Long l = (Long) o;
                                    trainingIds.add(l.intValue());
                                }

                                // delete trainingId
                                trainingIds.remove(Integer.valueOf(trainingId));

                                // Write the new list back to the database
                                userTrainingRef.setValue(trainingIds);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle potential errors.
                            Log.w("TAG", "loadTrainingIds:onCancelled", databaseError.toException());
                        }
                    });


                }else{

                }

                // check if the training is not full
            }
        }).addOnFailureListener(e -> {

            // TODO: Handle error and show message to the user
        });
    }

    private void registerForTraining(int trainingId, TrainingViewHolder trainingHolder) {
       // Toast.makeText(trainingHolder.itemView.getContext(),""+userId, Toast.LENGTH_SHORT).show();
        referenceProfile.child(userId).get().addOnSuccessListener(snapshot -> {
            ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);
            if (userDetails != null) {
                Training training = null;
                for (int i=0;i<trainings.size();i++){
                    if (trainings.get(i).getId()==trainingId){
                        training= trainings.get(i);
                        break;
                    }
                }
                if(training!=null&&training.getRegisteredNum()<training.getMaxParticipants()){
                    training.setRegisteredNum(training.getRegisteredNum()+1);
                    training.getRegisteredUsers().add(userId);
                    DocumentReference trainingRef = db.collection("trainings").document(training.getUid());
                    Training finalTraining = training;
                    trainingRef
                            .update("registeredNum", training.getRegisteredNum(),"registeredUsers",training.getRegisteredUsers())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "DocumentSnapshot successfully updated!");
                                    Toast.makeText(trainingHolder.itemView.getContext(),"You add successfully", Toast.LENGTH_SHORT).show();
                                    trainingHolder.trainingButtonRegistration.setBackgroundColor(Color.parseColor(red));
                                    trainingHolder.trainingButtonRegistration.setText("Cancel");
                                    notifyDataSetChanged();

                                    addTrainingToCalendar(trainingHolder.itemView.getContext(), finalTraining);
                                    addReminder(trainingHolder.itemView.getContext(),finalTraining);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error updating document", e);
                                    Toast.makeText(trainingHolder.itemView.getContext(),e.toString(), Toast.LENGTH_SHORT).show();

                                }
                            });
                    userTrainingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<Integer> trainingIds;
                            if (dataSnapshot.exists()) {
                                // Get the current trainingIds
                                trainingIds = (ArrayList<Integer>) dataSnapshot.getValue();

                                // Add new trainingId
                                trainingIds.add(trainingId);

                                // Write the new list back to the database
                                userTrainingRef.setValue(trainingIds);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle potential errors.
                            Log.w("TAG", "loadTrainingIds:onCancelled", databaseError.toException());
                        }
                    });

                }else{

                }

                // check if the training is not full
            }
        }).addOnFailureListener(e -> {

            // TODO: Handle error and show message to the user
        });
    }

    private void addReminder(Context context, Training training) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, training.getId(), intent, PendingIntent.FLAG_IMMUTABLE);

        // get time for the alarm (one day before the training)
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(training.getDateTime().toEpochSecond(ZoneOffset.UTC));
        calendar.add(Calendar.DAY_OF_YEAR, -1); // This will set the calendar to one day before the training
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
    private void unReminder(Context context, int trainingId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, trainingId, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.cancel(pendingIntent);
    }



    private void addWaitingList(int id, TrainingViewHolder trainingHolder) {

    }
    private void addTrainingToCalendar(Context context, Training training) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_CALENDAR}, 0);
        } else {
            long startMillis = 0;
            long endMillis = 0;
            Calendar beginTime = Calendar.getInstance();
            beginTime.set(training.getDateTime().getYear(), training.getDateTime().getMonthValue()-1, training.getDateTime().getDayOfMonth(), training.getDateTime().getHour(), training.getDateTime().getMinute());
            startMillis = beginTime.getTimeInMillis();
            Calendar endTime = Calendar.getInstance();
            endTime.set(training.getDateTime().getYear(), training.getDateTime().getMonthValue()-1, training.getDateTime().getDayOfMonth(), training.getDateTime().getHour()+1, training.getDateTime().getMinute());
            endMillis = endTime.getTimeInMillis();

            ContentResolver cr = context.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, startMillis);
            values.put(CalendarContract.Events.DTEND, endMillis);
            values.put(CalendarContract.Events.TITLE, training.getName());
            values.put(CalendarContract.Events.DESCRIPTION, training.getDescription());
            values.put(CalendarContract.Events.CALENDAR_ID, 1);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

            // Save the eventId into the database for future references
            //long eventId = Long.parseLong(uri.getLastPathSegment());
            Toast.makeText(context,"Event added to your calendar", Toast.LENGTH_SHORT).show();
        }
    }


}
