package com.example.workoutplanner.trainingData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutplanner.R;

import java.util.List;

public class RegisteredTrainingAdapter extends RecyclerView.Adapter<RegisteredTrainingAdapter.TrainingViewHolder>  {
    private final List<Training> registeredTrainings;
    public RegisteredTrainingAdapter(List<Training> registeredTrainings){
        this.registeredTrainings = registeredTrainings;
    }

    @Override
    public TrainingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_activity, parent, false);
        return new TrainingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrainingViewHolder holder, int position) {
        Training training = registeredTrainings.get(position);
        holder.trainingName.setText(training.getName());
        holder.trainingDescription.setText(training.getDescription());
        holder.trainingRegisteredNum.setText("Registered:" + training.getRegisteredNum()+ " out of "+ training.getMaxParticipants());
        holder.trainingTime.setText(training.getDateTime().toString());
        holder.trainingButtonRegistration.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return registeredTrainings.size();
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
}
