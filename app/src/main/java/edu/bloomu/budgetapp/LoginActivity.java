package edu.bloomu.budgetapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginActivity extends AppCompatActivity
{
    EditText unameField = findViewById(R.id.username_field);
    EditText pwordField = findViewById(R.id.password_field);
    Button createAcctBtn = findViewById(R.id.account_create_btn);
    Button loginBtn = findViewById(R.id.login_btn);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Animates the logo
        ImageView logo = findViewById(R.id.login_logo);
        ObjectAnimator logoAnimator =
                ObjectAnimator.ofFloat(logo, "translationY", 250, 0);
        logoAnimator.setDuration(1000);
        logoAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fadeInAnim();
            }
        });
        logoAnimator.start();

        Button loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(view -> loginButtonHandler());
    }

    /**
     * Animations for contents other than the logo.
     */
    private void fadeInAnim()
    {


        ObjectAnimator fadeInAnimator = ObjectAnimator.ofFloat(unameField,
                "alpha", 0, 1);
        fadeInAnimator.setDuration(500);
        fadeInAnimator.start();

        fadeInAnimator = ObjectAnimator.ofFloat(pwordField,
                "alpha", 0, 1);
        fadeInAnimator.setDuration(500);
        fadeInAnimator.start();

        fadeInAnimator = ObjectAnimator.ofFloat(createAcctBtn,
                "alpha", 0, 1);
        fadeInAnimator.setDuration(500);
        fadeInAnimator.start();

        fadeInAnimator = ObjectAnimator.ofFloat(loginBtn,
                "alpha", 0, 1);
        fadeInAnimator.setDuration(500);
        fadeInAnimator.start();
    }

    /**
     * Handles user login attempts. If the user presses the login button with
     * valid credentials starts activity with user creds as extras. Otherwise,
     * displays an error dialog box.
     */
    private void loginButtonHandler()
    {
        String username = unameField.toString();
        String password = pwordField.toString();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(LoginActivity.this, task ->
                {
                    if(task.isSuccessful())
                    {
                        //start new activity with user creds as extras
                    } else {
                        //throw up an error dialog about having invalid creds
                    }
                });
    }
}
