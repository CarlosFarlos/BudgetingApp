package edu.bloomu.budgetapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class AddFragment extends Fragment {


    public HashSet<Budget> budgets = new HashSet<>();
    private DatabaseReference userRef;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new instance of this fragment
     * using the provided parameters.
     *
     * @param dbRef a DatabaseReference to the specific UUID
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(DatabaseReference dbRef) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, dbRef.getKey());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        String reference = getArguments().getString(ARG_PARAM1);
        userRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(reference);
        budgets = Budget.getBudgets(userRef);

        // UI control for the add budget form
        EditText editBudgetName = view.findViewById(R.id.new_budget_name);
        EditText editMaxAmount = view.findViewById(R.id.new_budget_amount);
        ImageButton buttonSubmit = view.findViewById(R.id.add_budget_btn);
        buttonSubmit.setOnClickListener(View -> addNewBudget(editBudgetName, editMaxAmount));
        return view;
    }

    /**
     * Adds a new budget category to the set of budget categories.
     */
    private void addNewBudget(EditText budgetName, EditText maxAmount)
    {
        String name = budgetName.getText().toString();
        String max = maxAmount.getText().toString();

        if(isBudgetName(name))
        {
            Budget existingBudget = findBudget(name);
            if(existingBudget != null)
            {
                existingBudget.setMaxAmount(Double.parseDouble(max));
            }
            Toast.makeText(getActivity(), "Existing Budget Overwritten.",
                    Toast.LENGTH_SHORT).show();

            Budget.saveBudgets(budgets, userRef);
        } else {
            Budget newBudget = new Budget(name, Double.parseDouble(max));
            budgets.add(newBudget);
            Budget.saveBudgets(budgets, userRef);
        }
    }

    /**
     * Checks if the given Budget name already exists in the list of budgets
     */
    private boolean isBudgetName(String budgetName)
    {
        for (Budget budget : budgets)
        {

            if(budget.getName().equalsIgnoreCase(budgetName))
            {
                return true;
            }
        }
        return false;
    }

    private Budget findBudget(String name)
    {
        for (Budget budget : budgets)
        {
            if(budget.getName().equalsIgnoreCase(name))
            {
                return budget;
            }
        }

        return null;
    }

}
