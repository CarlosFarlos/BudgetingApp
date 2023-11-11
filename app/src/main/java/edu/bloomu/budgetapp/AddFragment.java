package edu.bloomu.budgetapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.widget.Button;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.Set;


public class AddFragment extends Fragment {


    public Set<Budget> budgets;
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
        String reference = getArguments().getString(mParam1);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(reference);

        // UI control for the add budget form
        EditText editBudgetName = view.findViewById(R.id.new_budget_name);
        EditText editMaxAmount = view.findViewById(R.id.new_budget_amount);
        ImageButton buttonSubmit = view.findViewById(R.id.add_budget_btn);
        return view;
    }

    /**
     * Adds a new budget category to the set of budget categories.
     */
    private void addNewBudget(EditText budgetName, EditText maxAmount)
    {
        String name = budgetName.getText().toString();
        String max = maxAmount.getText().toString();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(max))
        {
            Toast.makeText(getActivity(), "Please fill in both fields",
                    Toast.LENGTH_SHORT).show();
            return;
        } else if(isBudgetName(name))
        {
            Toast.makeText(getActivity(), "This budget already exists!",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            // Convert Max Budget allowance to a double
            double maxDouble = Double.parseDouble(max);
            // Create a new budget object and store it in the set
            Budget newBudget = new Budget(name, maxDouble);
            budgets.add(newBudget);

            budgetName.setText("");
            maxAmount.setText("");
            Toast.makeText(getActivity(), "Budget added successfully",
                    Toast.LENGTH_SHORT).show();
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






}