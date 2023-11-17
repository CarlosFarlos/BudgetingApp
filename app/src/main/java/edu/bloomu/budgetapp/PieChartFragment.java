package edu.bloomu.budgetapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class PieChartFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private final ArrayList<Budget> budgets = MainActivity.budgets;


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
        PieChart chart = view.findViewById(R.id.pie_chart_1);
        showChart(chart);


        return view;
    }

    private void showChart(PieChart chart)
    {
        // Used to generate darker variations of the app theme color
        List<Integer> colorsList = new ArrayList<>();
        float darknessRatio = 0.1f;
        int baseColor = Color.parseColor("#FFBD58");
        int index = 0;

        // Prepare the entries for the chart
        List<PieEntry> dataValues = new ArrayList<>();
        for(Budget budget : budgets)
        {
            dataValues.add(new PieEntry((float) budget.getMaxAmount(), budget.getName()));
            int temp =
                    ColorUtils.blendARGB(baseColor, Color.BLACK,
                            darknessRatio * index);
            colorsList.add(temp);
            index++;
        }
        PieDataSet dataSet = new PieDataSet(dataValues, "Categories");
        dataSet.setColors(colorsList);

        // Change the actual data labels on the chart
        PieData data = new PieData(dataSet);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(15f);
        data.setValueFormatter(new PercentFormatter(chart));

        // Change the legend properties
        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        // Remove the description label for the chart
        Description descriptionLabel = chart.getDescription();
        descriptionLabel.setText("");

        chart.setHoleColor(Color.TRANSPARENT);
        chart.setHoleRadius(40f);
        chart.setUsePercentValues(true);
        chart.setData(data);
    }


}