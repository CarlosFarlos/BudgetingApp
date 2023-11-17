package edu.bloomu.budgetapp;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Budget class to hold information on user budgets
 */
public class Budget
{
    private String name;
    private double maxAmount;
    private double currentSpend = 0.0;

    public Budget()
    {
    }


    public void setName(String name)
    {
        this.name = name;
    }

    public double getMaxAmount()
    {
        return maxAmount;
    }

    public double getCurrentSpend()
    {
        return currentSpend;
    }

    public void setCurrentSpend(double currentSpend)
    {
        this.currentSpend = currentSpend;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getName()
    {
        return name;
    }

    /**
     * Saves the set of budgets to the database
     */
    public static void saveBudgets(Budget budget, DatabaseReference userDbRef)
    {
        userDbRef.child("budgets").child(budget.getName()).setValue(budget);
    }

    /**
     * Loads the set of budgets from the database
     *
     * @return
     */
    public static ArrayList<Budget> getBudgets(DatabaseReference userDbRef) {
        ArrayList<Budget> budgets = new ArrayList<>();
        userDbRef.child("budgets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot budgetSnapshot : snapshot.getChildren()) {
                    Budget budget = budgetSnapshot.getValue(Budget.class);
                    if (budget != null) {
                        budgets.add(budget);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });
        return budgets;
    }



}
