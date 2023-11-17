package edu.bloomu.budgetapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    public ArrayList<Budget> budgets = MainActivity.budgets;
    private RecyclerView recyclerView;
    private DatabaseReference userRef;
    private static final String ARG_PARAM1 = "param1";

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance(DatabaseReference dbRef) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, dbRef.getKey());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ALERT", "onCreate called!");
        Log.d("OnCreate", "Budgets:" + budgets.isEmpty());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_dashboard, container, false);
        assert getArguments() != null;
        String reference = getArguments().getString(ARG_PARAM1);
        assert reference != null;
        userRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("users").child(reference);
        recyclerView = view.findViewById(R.id.budget_recycler_view);
        setAdapter();
        Log.d("ALERT", "onCreateView called!");
        Log.d("OnCreateView", "Budgets:" + budgets.isEmpty());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Budget.getBudgets(userRef);
    }



    private void setAdapter()
    {
        BudgetAdapter budgetAdapter = new BudgetAdapter(budgets);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(budgetAdapter);
    }
}