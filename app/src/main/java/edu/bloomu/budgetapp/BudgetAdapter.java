package edu.bloomu.budgetapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.MyViewHolder>
{
    private final ArrayList<Budget> budgetList;

    public BudgetAdapter(ArrayList<Budget> budgetList)
    {
        this.budgetList = budgetList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView nameText, amountText, maxText, percent;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            nameText = itemView.findViewById(R.id.textViewBudgetName);
            amountText = itemView.findViewById(R.id.textViewAmountSpent);
            maxText = itemView.findViewById(R.id.textViewMaxAmount);
            percent = itemView.findViewById(R.id.percent_check);
        }
    }
    @NonNull
    @Override
    public BudgetAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                         int viewType)
    {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.budget_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetAdapter.MyViewHolder holder, int position)
    {
        String name = budgetList.get(position).getName();
        String spent = "Amount spent: $" + budgetList.get(position).getCurrentSpend();
        String max = "Maximum budget allowance: $"
                + budgetList.get(position).getMaxAmount();

        double cSpend = budgetList.get(position).getCurrentSpend();
        double cMax = budgetList.get(position).getMaxAmount();

        String current = calculateProgress(cSpend, cMax) + "%";
        int percentage = calculateProgress(cSpend, cMax);
        int textColor = getColorBasedOnPercentage(percentage);

        holder.nameText.setText(name);
        holder.amountText.setText(spent);
        holder.maxText.setText(max);
        holder.percent.setText(current);
        holder.percent.setTextColor(textColor);
    }


    // Function to calculate progress as an percent
    private int calculateProgress(double spent, double max)
    {
        return (int) ((spent / max) * 100);
    }

    // Function to get color based on percentage
    private int getColorBasedOnPercentage(int percentage) {
        float[] green = {120f, 1f, 0.5f};   // Green
        float[] yellow = {60f, 1f, 0.5f};   // Yellow
        float[] red = {0f, 1f, 0.5f};       // Red

        // Interpolate between colors based on the percentage
        float[] interpolatedColor =
                interpolateColors(green, yellow, red, percentage / 100f);

        return ColorUtils.HSLToColor(interpolatedColor);
    }

    // Function to interpolate between colors
    private float[] interpolateColors(float[] startColor, float[] middleColor,
                                      float[] endColor, float percentage)
    {
        float[] resultColor = new float[3];
        for (int i = 0; i < 3; i++) {
            resultColor[i] = (1 - percentage) * (1 - percentage) * startColor[i]
                    + 2 * (1 - percentage) * percentage * middleColor[i]
                    + percentage * percentage * endColor[i];
        }
        return resultColor;
    }

    @Override
    public int getItemCount() {
        return budgetList.size();
    }
}
