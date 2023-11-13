package edu.bloomu.budgetapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private String mParam1;
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pie_chart, container, false);
        /**
        String reference = getArguments().getString(ARG_PARAM1);
        userRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("users").child(reference);
        budgets = Budget.getBudgets(userRef);
         **/

        PieChart pieChart = view.findViewById(R.id.pie_chart);
        PieData data = new PieData();
        ArrayList<PieEntry> dataVals = new ArrayList<>();
        dataVals.add(new PieEntry(50, "poop"));
        dataVals.add(new PieEntry(100, "pee"));
        PieDataSet pieDataSet = new PieDataSet(dataVals, "");
        int[] colorClassArray = new int[]{Color.GRAY, Color.BLUE};
        pieDataSet.setColors(colorClassArray);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setDescription(new Description());


        return view;
    }
}