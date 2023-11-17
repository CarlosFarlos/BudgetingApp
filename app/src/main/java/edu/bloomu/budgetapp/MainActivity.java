package edu.bloomu.budgetapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * Displays fragment container and three buttons. The fragment container will
 * manage changes between fragments displayed. Each button will display a new
 * fragment. The left-most button displays the dashboard fragment, the middle
 * button displays the add fragment, and the right-most button displays the
 * pie chart fragment.
 *
 * @author Carlos Ivan Oquendo-Pagan
 */
public class MainActivity extends AppCompatActivity {

    public static ArrayList<Budget> budgets = new ArrayList<>();

    public static DatabaseReference userRef;
    ImageButton dashButton, addButton, chartButton;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();

        username = intent.getStringExtra("username");
        String dataKey = intent.getStringExtra("dataKey");
        assert dataKey != null;
        userRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(dataKey);
        budgets = Budget.getBudgets(userRef);

        // Button to navigate to the dashboard fragment
        dashButton = findViewById(R.id.budget_view_btn);
        dashButton.setOnClickListener(this::onDashButtonClick);

        // Button to navigate to the add budgets/expenses fragment
        addButton = findViewById(R.id.add_view_btn);
        addButton.setOnClickListener(this::onAddButtonClick);

        // Button to navigate to the pie chart fragment
        chartButton = findViewById(R.id.chart_view_btn);
        chartButton.setOnClickListener(this::onChartButtonClick);

        addButton.callOnClick();
    }

    /**
     * Resets all button colors and inverts the color of the button pressed.
     */
    void alternateButtonColor(ImageButton button)
    {
        resetButtons();
        if(button.equals(dashButton)) invertDashButton();
        if(button.equals(addButton)) invertAddButton();
        if(button.equals(chartButton)) invertChartButton();
    }

    /**
     * Resets the button themes to their defaults
     */
    void resetButtons()
    {
        resetDashButton();
        resetAddButton();
        resetChartButton();
    }

    // Resets the colors of the dashboard button
    void resetDashButton()
    {
        dashButton.setImageResource(R.drawable.tiny_pig_invert);
        dashButton.setBackgroundResource(R.drawable.rounded_main_btn);
    }

    // Inverts the colors of the dashboard button
    void invertDashButton()
    {
        dashButton.setImageResource(R.drawable.tiny_pig);
        dashButton.setBackgroundResource(R.drawable.rounded_main_btn_invert);
    }

    // Resets the colors of the add button
    void resetAddButton()
    {
        addButton.setImageResource(R.drawable.add_invert);
        addButton.setBackgroundResource(R.drawable.rounded_main_btn);
    }

    // Inverts the colors of the add button
    void invertAddButton()
    {
        addButton.setImageResource(R.drawable.add);
        addButton.setBackgroundResource(R.drawable.rounded_main_btn_invert);
    }

    // Resets the colors of the chart button
    void resetChartButton()
    {
        chartButton.setImageResource(R.drawable.tiny_pie_invert);
        chartButton.setBackgroundResource(R.drawable.rounded_main_btn);
    }

    // Inverts the colors of the chart button
    void invertChartButton()
    {
        chartButton.setImageResource(R.drawable.tiny_pie);
        chartButton.setBackgroundResource(R.drawable.rounded_main_btn_invert);
    }

    /**
     * Onclick method for the dashboard button
     */
    private void onDashButtonClick(View view)
    {
        alternateButtonColor(dashButton);
        DashboardFragment dashboardFragment = DashboardFragment.newInstance(userRef);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_down_to_up,
                R.anim.exit_up_to_down, R.anim.enter_up_to_down,
                R.anim.exit_down_to_up);
        transaction.replace(R.id.fragment_container, dashboardFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Onclick method for the Add button
     */
    private void onAddButtonClick(View view)
    {
        alternateButtonColor(addButton);
        AddFragment addFragment = AddFragment.newInstance(userRef);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_down_to_up,
                R.anim.exit_up_to_down, R.anim.enter_down_to_up,
                R.anim.exit_up_to_down);
        transaction.replace(R.id.fragment_container, addFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Onclick method for the pie chart button
     */
    private void onChartButtonClick(View view)
    {
        alternateButtonColor(chartButton);
        PieChartFragment pieChartFragment = PieChartFragment.newInstance(userRef);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_down_to_up,
                R.anim.exit_up_to_down, R.anim.enter_down_to_up,
                R.anim.exit_up_to_down);
        transaction.replace(R.id.fragment_container, pieChartFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Returns username for use in fragments
     */
    public String getUsername()
    {
        return username;
    }
}