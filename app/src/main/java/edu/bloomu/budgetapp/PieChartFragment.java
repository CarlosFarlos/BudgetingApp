package edu.bloomu.budgetapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PieChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PieChartFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private DatabaseReference userRef;
    private ArrayList<Budget> budgets = new ArrayList<>();


    public PieChartFragment() {
        // Required empty public constructor
    }


    public static PieChartFragment newInstance(DatabaseReference dbRef) {
        PieChartFragment fragment = new PieChartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, dbRef.getKey());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(budgets == null)
        {
            assert getArguments() != null;
            String reference = getArguments().getString(ARG_PARAM1);
            assert reference != null;
            userRef = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users").child(reference);
            ArrayList<Budget> newBudgets = new ArrayList<>();
            budgets = Budget.getBudgets(userRef, newBudgets);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pie_chart, container, false);

        return view;
    }

}