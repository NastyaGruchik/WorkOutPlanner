package com.example.workoutplanner.ui.addActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutplanner.R;
import com.example.workoutplanner.databinding.FragmentAddActivityBinding;
import com.example.workoutplanner.trainingData.SectionedTrainingAdapter;
import com.example.workoutplanner.trainingData.GlobalTrainingData;


public class AddActivityFragment extends Fragment {

    private FragmentAddActivityBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAddActivityBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Get the instance of the singleton
        GlobalTrainingData globalTrainingData = GlobalTrainingData.getInstance();

        // Observe the LiveData
        globalTrainingData.getTrainingsLiveData().observe(getViewLifecycleOwner(), trainings -> {
            // Set the data to the RecyclerView
            RecyclerView recyclerView = root.findViewById(R.id.recyclerViewPrograms);
            SectionedTrainingAdapter adapter = new SectionedTrainingAdapter(trainings);
            recyclerView.setAdapter(adapter);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Remove listener when the view is destroyed
        GlobalTrainingData.getInstance().removeListener();
        binding = null;
    }
}
