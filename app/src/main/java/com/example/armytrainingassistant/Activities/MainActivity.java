package com.example.armytrainingassistant.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.armytrainingassistant.Controller.CustomExpandableListAdapter;
import com.example.armytrainingassistant.Controller.SectionsPagerAdapter;
import com.example.armytrainingassistant.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private final String FRAGMENT_FILE_KEY_CHOOSER = "fragment_key_chooser";

    public static String PACKAGE_NAME;
    public static Context CONTEXT;
    public static FirebaseFirestore db;
    public static CustomExpandableListAdapter machineGunnerListAdapter;
    public static ViewPager viewPager;
    public static String senderEmail;
    public static String senderCred;
    public static FirebaseUser user;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CONTEXT = getApplicationContext();
        PACKAGE_NAME = CONTEXT.getPackageName();

        senderEmail = getString(R.string.admin_email);
        senderCred = getString(R.string.admin_email_cred);

        // Instantiate Database
        db = FirebaseFirestore.getInstance();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        //create and set adapter
        machineGunnerListAdapter = new CustomExpandableListAdapter(CONTEXT);
        Log.d("ADAPTER", "CREATED");

        //get fileId to create correct fragment
        try{
            int fileID = getIntent().getExtras().getInt(FRAGMENT_FILE_KEY_CHOOSER);
            sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), fileID);
        } catch(NullPointerException e){
            Log.d("MainActivity", "fileID is null");
        }

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(user == null)
            signInAnonymously();

    }

    private void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("mAuth", "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            MainActivity.user = user;
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("mAuth", "signInAnonymously:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
        // [END signin_anonymously]
    }

}