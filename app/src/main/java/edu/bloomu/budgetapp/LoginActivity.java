package edu.bloomu.budgetapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Displays Username and Password fields, and two buttons. One button attempts
 * to create a new user account with the information provided by the user in
 * the fields. The other button attempts to log users in.
 *
 * @author Carlos Ivan Oquendo-Pagan
 */

public class LoginActivity extends AppCompatActivity
{

    FirebaseDatabase db;
    FirebaseAuth auth;
    EditText unameField, pwordField;
    Button createAcctBtn, loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

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

        // Login button functions
        Button loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(view -> loginButtonHandler());

        // Create account button functions
        Button createAccountBtn = findViewById(R.id.account_create_btn);
        createAccountBtn.setOnClickListener(view -> createButtonHandler());
    }

    /**
     * Animations for contents other than the logo.
     */
    private void fadeInAnim()
    {
        unameField = findViewById(R.id.username_field);
        pwordField = findViewById(R.id.password_field);
        createAcctBtn = findViewById(R.id.account_create_btn);
        loginBtn = findViewById(R.id.login_btn);

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
        String username = unameField.getText().toString();
        String password = pwordField.getText().toString();
        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference("users");
        Query usernameQuery = userRef.orderByChild("username").equalTo(username);

        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot userSnapshot : snapshot.getChildren())
                    {
                        String dbUsername = userSnapshot
                                .child("username")
                                .getValue(String.class);
                        String hashedPass = userSnapshot
                                .child("password")
                                .getValue(String.class);

                        assert hashedPass != null;
                        if(BCrypt.verifyer()
                                .verify(password.toCharArray(), hashedPass).verified)
                        {
                            //Start new activity with the username as an extra
                            startMainActivity(dbUsername);
                        } else {
                            //Error toast
                            Toast.makeText(getApplicationContext(),
                                    "Invalid credentials. Please try again.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Error", "Error: " + error.getMessage());
            }
        });
    }

    /**
     * Handles user account creation. Reads the user credentials and checks
     * them against existing usernames in the DB, if the username is unique
     * adds it to the DB along with the hashed password.
     */
    private void createButtonHandler()
    {
        String enteredUser = unameField.getText().toString();
        String enteredPass = pwordField.getText().toString();
        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference("users");

        Query usernameQuery = userRef
                .orderByChild("username")
                .equalTo(enteredUser);
        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Toast.makeText(getApplicationContext(),
                            "Username is already in use.", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    String hashedPass = BCrypt.withDefaults()
                            .hashToString(12, enteredPass.toCharArray());

                    DatabaseReference newUserRef = userRef.push();
                    newUserRef.child("username").setValue(enteredUser);
                    newUserRef.child("password").setValue(hashedPass);

                    Toast.makeText(getApplicationContext(), "Account created.",
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error Creating Account",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Starts the next activity once a user has valid login credentials
     */
    private void startMainActivity(String username)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}