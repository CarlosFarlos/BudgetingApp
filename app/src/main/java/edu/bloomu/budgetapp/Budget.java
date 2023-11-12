package edu.bloomu.budgetapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Budget class to hold information on user budgets
 */
public class Budget
{
    private String name;
    private double maxAmount;
    private double currentSpend = 0.0;

    public Budget(String name, double maxAmount)
    {
        this.name = name;
        this.maxAmount = maxAmount;
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
     * Saves the set of budgets to SharedPreferences
     */
    public static void saveBudgets(HashSet<Budget> budgetSet, DatabaseReference userDbRef)
    {
        Gson gson = new Gson();
        String json = gson.toJson(budgetSet);
        userDbRef.child("budgets").setValue(json);
    }

    /**
     * Loads the set of budgets from SharedPreferences
     */
    public static HashSet<Budget> getBudgets(DatabaseReference userDbRef)
    {
        final String[] json = new String[1];
        Gson gson = new Gson();
        userDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    json[0] = snapshot.child("budgets").getValue(String.class);
                    Log.d("Userdata", "Budget JSON:" + json[0]);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Type setType = new TypeToken<HashSet<Budget>>(){}.getType();
        HashSet<Budget> budgets = gson.fromJson(json[0], setType);
        if (budgets == null) {
            budgets = new HashSet<Budget>();
        }
        return budgets;
    }

}
