package edu.bloomu.budgetapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    public HashSet<Budget> budgets = new HashSet<>();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_dashboard, container, false);
        String reference = getArguments().getString(ARG_PARAM1);
        userRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("users").child(reference);
        budgets = Budget.getBudgets(userRef);

        return view;
    }
}