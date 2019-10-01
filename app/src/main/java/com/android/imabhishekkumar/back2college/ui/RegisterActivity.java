package com.android.imabhishekkumar.back2college.ui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.imabhishekkumar.back2college.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import org.aviran.cookiebar2.CookieBar;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    ProgressDialog progressdialog;
    FirebaseFirestore db;
    DatePickerDialog.OnDateSetListener datePickerDialogListener;
    GoogleSignInClient mGoogleSignInClient;
    public static int RC_SIGN_IN = 1;
    public static String TAG = "Register Activity";

    @BindView(R.id.frameLayout)
    LinearLayout cardView;
    @BindView(R.id.button)
    Button createAccountBtn;
    @BindView(R.id.registerNumber_text)
    EditText registerNumberET;
    @BindView(R.id.rollrNumber_text)
    EditText rollNumberET;
    @BindView(R.id.mobile_text)
    EditText mobileNumberET;
    @BindView(R.id.department_text)
    Spinner departmentET;
    @BindView(R.id.imageButton)
    CircleImageView avatarImage;
    @BindView(R.id.register_layout)
    ConstraintLayout registerLayout;
    @BindView(R.id.registerDetails_layout)
    ConstraintLayout registerDetailsLayout;
    @BindView(R.id.userEmail_TV)
    TextView userEmailTV;
    @BindView(R.id.username_TV)
    TextView greetingsTV;
    @BindView(R.id.date_text)
    TextView dateOfBirthET;
    @BindView(R.id.date_container)
    TextView dateContainer;
    @BindView(R.id.close_btn_registerDetails)
    ImageButton closeButtonDetails;
    String userEmail;
    String displayName, registerNumber, rollNumber, mobileNumber, department, date;
    String googleToken = "";
    Uri userImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        registerLayout.setVisibility(View.VISIBLE);
        registerDetailsLayout.setVisibility(View.GONE);
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        registerLayout.setVisibility(View.VISIBLE);
        registerDetailsLayout.setVisibility(View.GONE);

        String[] items = new String[]{"Select your department", "Aero", "Auto", "BioInfo", "BioMed", "Civil", "CSE", "ECE", "EEE", "EIE", "ETCE", "IT", "MECH", "MnP"};
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items);
        departmentET.setAdapter(adapter);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        progressdialog = new ProgressDialog(RegisterActivity.this);


        signInButton.setOnClickListener(view -> {

            progressdialog.setMessage("Signing you in ...");
            progressdialog.setIndeterminate(true);
            progressdialog.show();
            signIn();
        });


        closeButtonDetails.setOnClickListener(view -> {
            registerLayout.setVisibility(View.VISIBLE);
            registerDetailsLayout.setVisibility(View.GONE);
        });

        cardView.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this,
                    datePickerDialogListener,
                    year, month, day);
            dialog.getWindow();
            dialog.show();
            dateContainer.setVisibility(View.VISIBLE);
        });
        datePickerDialogListener = (datePicker, year, month, day) -> {
            month = month + 1;
            date = day + "/" + month + "/" + year;
            dateOfBirthET.setText(date);
        };
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        createAccountBtn.setOnClickListener(view -> {
            mUser = mAuth.getCurrentUser();
            registerNumber = registerNumberET.getText().toString();
            rollNumber = rollNumberET.getText().toString();
            mobileNumber = mobileNumberET.getText().toString();
            department = departmentET.getSelectedItem().toString();
            registerNumber = registerNumberET.getText().toString();
            userEmail = userEmailTV.getText().toString();
            progressdialog.setMessage("Creating account, please wait..");
            progressdialog.setIndeterminate(true);
            progressdialog.show();
            addDataToDB();


        });
    }

    private void subscribeToTopic() {

        FirebaseMessaging.getInstance().subscribeToTopic("B2CNotification")
                .addOnCompleteListener(task -> {
                    FirebaseMessaging.getInstance().subscribeToTopic(department);
                    String msg = "Subscribed";
                    if (!task.isSuccessful()) {
                        msg = "Failed to subscribe";
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, msg);
                        startActivity(new Intent(RegisterActivity.this, Home.class));
                        progressdialog.dismiss();
                        finish();
                    }

                });

    }


    private void addDataToDB() {
        Map<String, Object> user = new HashMap<>();
        user.put("registerNumber", registerNumber);
        user.put("rollNumber", rollNumber);
        user.put("mobileNumber", mobileNumber);
        user.put("department", department);
        user.put("dateOfBirth", date);
        user.put("email", userEmail);
        user.put("verified", "true");
        user.put("userId", mUser.getUid());
        user.put("displayName", displayName);
        user.put("displayImage", userImage.toString());

        db.collection("users").document(mUser.getUid())
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                    subscribeToTopic();
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }


    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1058860673477-puka05q76t96drdk573urhdu5j3c32ii.apps.googleusercontent.com")
            .requestEmail()
            .build();


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                mAuth = FirebaseAuth.getInstance();
                firebaseAuthWithGoogle(account);
                userEmail = account.getEmail();
                displayName = account.getDisplayName();
                userImage = account.getPhotoUrl();


            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                progressdialog.dismiss();
                CookieBar.build(RegisterActivity.this)
                        .setTitle("Google sign in failed")
                        .setMessage("Please check your internet connection and try again.")
                        .setBackgroundColor(R.color.colorPrimary)
                        .setCookiePosition(CookieBar.TOP)
                        .show();
                Log.w(TAG, "Google sign in failed", e);
            }
        }

    }

    private void setUser(String displayName, Uri userImage) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .setPhotoUri(userImage)
                .build();

        user.updateProfile(profileUpdates);
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        googleToken = acct.getIdToken();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                        mUser = mAuth.getCurrentUser();
                        if (isNew) {
                            registerLayout.setVisibility(View.GONE);
                            registerDetailsLayout.setVisibility(View.VISIBLE);
                            userEmailTV.setText(userEmail);
                            greetingsTV.setText("Welcome, " + displayName);
                            Glide.with(getApplicationContext())
                                    .load(userImage)
                                    .into(avatarImage);
                            setUser(displayName, userImage);
                        } else {
                            startActivity(new Intent(RegisterActivity.this, Home.class));
                            finish();
                        }

                        progressdialog.dismiss();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(RegisterActivity.this, Home.class));
            finish();
        }

    }

}
