package com.android.imabhishekkumar.back2college.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.imabhishekkumar.back2college.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.aviran.cookiebar2.CookieBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddPost extends AppCompatActivity {
    ConstraintLayout background;
    EditText editText;
    ImageButton goBackBtn, addMediaBtn;
    ImageButton  filterButton;
    FloatingActionButton addButton;
    CoordinatorLayout coordinatorLayout;
    ImageView wv;
    FirebaseAuth auth;
    Uri mImageUri;
    LinearLayout layoutBottomSheet, checkBoxes;
    BottomSheetBehavior sheetBehavior;
    Button bsSubmit;
    ProgressDialog mProgress;
    CheckBox all, aero, auto, bioInfo, bioMed, civil, cse, ece, eee, eie, etce, it, mech, mnp;
    FirebaseFirestore firebaseFirestore;
    Map<String, Object> posts = new HashMap<>();
    Map<String, Object> userposts = new HashMap<>();
    String postText, postMultimediaURL, avatarURL;
    private StorageReference mStorageRef, postImages;
    private final static int gallerycode = 1;
    ArrayList<String> selectedDepartment = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        background = findViewById(R.id.edittextBG);
        editText = findViewById(R.id.addPostEditText);
        auth = FirebaseAuth.getInstance();
        goBackBtn = findViewById(R.id.back_Button);
        addMediaBtn = findViewById(R.id.addMedia);
        mProgress = new ProgressDialog(this);
        wv = findViewById(R.id.addPostWebView);
        addButton = findViewById(R.id.addPost);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        filterButton = findViewById(R.id.filters);
        layoutBottomSheet = findViewById(R.id.linearBottomSheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        bsSubmit = findViewById(R.id.bs_submit_btn);
        coordinatorLayout = findViewById(R.id.addPostCoordinatorLayout);
        all = findViewById(R.id.allCB);
        aero = findViewById(R.id.aeroCB);
        auto = findViewById(R.id.autoCB);
        bioInfo = findViewById(R.id.bioInfoCB);
        bioMed = findViewById(R.id.bioMedCB);
        civil = findViewById(R.id.civilCB);
        cse = findViewById(R.id.cseCB);
        ece = findViewById(R.id.eceCB);
        eee = findViewById(R.id.eeeCB);
        eie = findViewById(R.id.eieCB);
        etce = findViewById(R.id.etceCB);
        it = findViewById(R.id.itCB);
        mech = findViewById(R.id.mechCB);
        mnp = findViewById(R.id.mnpCB);
        checkBoxes = findViewById(R.id.checkBoxes);

        checkBoxes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                all.setChecked(false);
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unCheckAll();
            }
        });

        Log.d("Details", auth.getCurrentUser().getDisplayName());
        Log.d("Details", auth.getCurrentUser().getPhotoUrl().toString());
        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showKeyboard();
            }
        });
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBottomSheet();
            }
        });
        addMediaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/* video/* ");
                startActivityForResult(intent, gallerycode);
            }
        });
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddPost.this, Home.class));
                finish();
            }
        });
        bsSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDepartments();
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress.setMessage("Adding Post");
                mProgress.show();
                postText = editText.getText().toString();
                long yourmilliseconds = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                Date resultdate = new Date(yourmilliseconds);
                String formatteddate = sdf.format(resultdate);
                if (mImageUri != null) {
                    postMultimediaURL = mImageUri.toString();
                    posts.put("details", postText);
                    posts.put("name", auth.getCurrentUser().getDisplayName());
                    posts.put("avatar", auth.getCurrentUser().getPhotoUrl().toString());
                    posts.put("time", formatteddate);
                    posts.put("uid", auth.getCurrentUser().getUid());
                    if (selectedDepartment == null || selectedDepartment.size() == 0) {
                        posts.put("postTo", Arrays.asList("all"));
                    } else {
                        posts.put("postTo", selectedDepartment);
                    }
                    posts.put("timestamp", new Date().getTime());
                    addImageToDB();
                } else {
                    posts.put("details", postText);
                    posts.put("name", auth.getCurrentUser().getDisplayName());
                    posts.put("avatar", auth.getCurrentUser().getPhotoUrl().toString());
                    posts.put("time", formatteddate);
                    posts.put("uid", auth.getCurrentUser().getUid());
                    if (selectedDepartment == null || selectedDepartment.size() == 0) {
                        posts.put("postTo", Arrays.asList("all"));
                    } else {
                        posts.put("postTo", selectedDepartment);
                    }
                    posts.put("timestamp", new Date().getTime());
                    pushToDB();
                }


            }
        });
        if(!isConnectionAvailable()){
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection available.", Snackbar.LENGTH_LONG);
            snackbar.show();

        }
    }
    public boolean isConnectionAvailable() {

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();

    }
    private void unCheckAll() {
        selectedDepartment.clear();
        aero.setChecked(false);
        auto.setChecked(false);
        bioInfo.setChecked(false);
        bioMed.setChecked(false);
        civil.setChecked(false);
        cse.setChecked(false);
        ece.setChecked(false);
        eie.setChecked(false);
        eee.setChecked(false);
        etce.setChecked(false);
        it.setChecked(false);
        mech.setChecked(false);
        mnp.setChecked(false);

    }

    public void checkDepartments() {
        selectedDepartment.clear();
        if (aero.isChecked()) {
            selectedDepartment.add(getString(R.string.aero));
            all.setChecked(false);
        }
        if (auto.isChecked()) {
            selectedDepartment.add(getString(R.string.auto));
            all.setChecked(false);
        }
        if (bioInfo.isChecked()) {
            selectedDepartment.add(getString(R.string.bioinfo));
            all.setChecked(false);
        }
        if (bioMed.isChecked()) {
            selectedDepartment.add(getString(R.string.biomed));
            all.setChecked(false);
        }
        if (civil.isChecked()) {
            selectedDepartment.add(getString(R.string.civil));
            all.setChecked(false);
        }
        if (cse.isChecked()) {
            selectedDepartment.add(getString(R.string.cse));
            all.setChecked(false);
        }
        if (ece.isChecked()) {
            selectedDepartment.add(getString(R.string.ece));
            all.setChecked(false);
        }
        if (eee.isChecked()) {
            selectedDepartment.add(getString(R.string.eee));
        }
        if (eie.isChecked()) {
            selectedDepartment.add(getString(R.string.eie));
            all.setChecked(false);
        }
        if (etce.isChecked()) {
            selectedDepartment.add(getString(R.string.etce));
            all.setChecked(false);
        }
        if (it.isChecked()) {
            selectedDepartment.add(getString(R.string.it));
            all.setChecked(false);
        }
        if (mech.isChecked()) {
            selectedDepartment.add(getString(R.string.mech));
            all.setChecked(false);
        }
        if (mnp.isChecked()) {
            selectedDepartment.add(getString(R.string.mnp));
            all.setChecked(false);
        }
    }

    private void pushToDB() {
        firebaseFirestore.collection("posts").add(posts)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String chatId = documentReference.getId();
                        firebaseFirestore.collection("users").document(auth.getUid()).collection("Timeline").document(chatId).set(posts);
                        mProgress.dismiss();
                        sendNotification();
                        startActivity(new Intent(AddPost.this, Home.class));
                        finish();
                    }
                });

    }

    private void sendNotification() {
        FirebaseFunctions.getInstance()
                .getHttpsCallable("createTodo");

    }

    public void toggleBottomSheet() {
        all.setChecked(true);
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setPeekHeight(800);
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        }
    }


    private void addImageToDB() {
        postImages = mStorageRef.child("media/").child(mImageUri.getLastPathSegment());
        postImages.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return postImages.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    posts.put("multimediaURL", downloadUri.toString());
                    pushToDB();

                } else {
                    Log.d("upload failed: ", task.getException().getMessage());
                }
            }
        });
    }


    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText,
                InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == gallerycode) {
            mImageUri = data.getData();
            wv.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext())
                    .load(mImageUri.toString())
                    .into(wv);


        }
    }
}
