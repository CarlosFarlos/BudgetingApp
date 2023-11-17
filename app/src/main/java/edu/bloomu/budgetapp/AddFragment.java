package edu.bloomu.budgetapp;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;



public class AddFragment extends Fragment {


    public ArrayList<Budget> budgets = MainActivity.budgets;
    private final DatabaseReference userRef = MainActivity.userRef;
    private static final String ARG_PARAM1 = "param1";


    public AddFragment() {
        // Required empty public constructor
    }

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
        if(budgets == null)
        {
            assert getArguments() != null;
            String reference = getArguments().getString(ARG_PARAM1);
            assert reference != null;
            budgets = Budget.getBudgets(userRef);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        // UI control for the add budget form
        EditText editBudgetName = view.findViewById(R.id.new_budget_name);
        EditText editMaxAmount = view.findViewById(R.id.new_budget_amount);
        ImageButton buttonSubmit = view.findViewById(R.id.add_budget_btn);
        buttonSubmit.setOnClickListener(View -> addNewBudget(editBudgetName, editMaxAmount));


        // UI control for the add expenses form
        EditText currentBudgetName = view.findViewById(R.id.expense_category_name);
        EditText currentSpent = view.findViewById(R.id.amount_spent);
        ImageButton submitExpense = view.findViewById(R.id.add_expense_btn);
        submitExpense.setOnClickListener(View -> addNewExpense(currentBudgetName, currentSpent));

        return view;
    }

    /**
     * Adds a new expense to a specified budget
     */
    private void addNewExpense(EditText budgetName, EditText spentAmount)
    {
        String name = budgetName.getText().toString();
        String temp = spentAmount.getText().toString();
        double spent = Double.parseDouble(temp);

        if(!isBudgetName(name))
        {
            Toast.makeText(getActivity(), "That budget does not exist!",
                    Toast.LENGTH_SHORT).show();
        } else {
            Budget existingBudget = findBudget(name);
            assert existingBudget != null;
            double currentSpend = existingBudget.getCurrentSpend();
            double maxAmount = existingBudget.getMaxAmount();

            if(currentSpend + spent > maxAmount || spent > maxAmount)
            {
                Toast.makeText(getActivity(),
                        "This expense would go over your budget, please update your budget maximum",
                        Toast.LENGTH_SHORT).show();
            } else {
                existingBudget.setCurrentSpend(spent + currentSpend);
            }
            Budget.saveBudgets(existingBudget, userRef);
        }
        hideKeyboard();
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

            assert existingBudget != null;
            Budget.saveBudgets(existingBudget, userRef);
        } else {
            Budget newBudget = new Budget();
            newBudget.setName(name);
            newBudget.setMaxAmount(Double.parseDouble(max));
            budgets.add(newBudget);
            Toast.makeText(getActivity(), "New Budget added.",
                    Toast.LENGTH_SHORT).show();
        }
        hideKeyboard();
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

    /**
     * Finds the specified budget in the list
     */
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

    /**
     * Minimizes the keyboard for the device.
     */
    private void hideKeyboard()
    {
        View view = getActivity().getCurrentFocus();
        if(view != null)
        {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

}