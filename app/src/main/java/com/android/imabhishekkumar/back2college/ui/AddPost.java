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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddPost extends AppCompatActivity {
    @BindView(R.id.edittextBG)
    ConstraintLayout background;
    @BindView(R.id.addPostEditText)
    EditText editText;
    @BindView(R.id.back_Button)
    ImageButton goBackBtn;
    @BindView(R.id.addMedia)
    ImageButton addMediaBtn;
    @BindView(R.id.filters)
    ImageButton filterButton;
    @BindView(R.id.addPost)
    FloatingActionButton addButton;
    @BindView(R.id.addPostCoordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.addPostWebView)
    ImageView wv;
    @BindView(R.id.linearBottomSheet)
    LinearLayout layoutBottomSheet;
    @BindView(R.id.checkBoxes)
    LinearLayout checkBoxes;
    BottomSheetBehavior sheetBehavior;
    @BindView(R.id.bs_submit_btn)
    Button bsSubmit;
    @BindView(R.id.allCB)
    CheckBox all;
    @BindView(R.id.aeroCB)
    CheckBox aero;
    @BindView(R.id.autoCB)
    CheckBox auto;
    @BindView(R.id.bioInfoCB)
    CheckBox bioInfo;
    @BindView(R.id.bioMedCB)
    CheckBox bioMed;
    @BindView(R.id.civilCB)
    CheckBox civil;
    @BindView(R.id.cseCB)
    CheckBox cse;
    @BindView(R.id.eceCB)
    CheckBox ece;
    @BindView(R.id.eeeCB)
    CheckBox eee;
    @BindView(R.id.eieCB)
    CheckBox eie;
    @BindView(R.id.etceCB)
    CheckBox etce;
    @BindView(R.id.itCB)
    CheckBox it;
    @BindView(R.id.mechCB)
    CheckBox mech;
    @BindView(R.id.mnpCB)
    CheckBox mnp;
    FirebaseAuth auth;
    Uri mImageUri;
    ProgressDialog mProgress;
    FirebaseFirestore firebaseFirestore;
    Map<String, Object> posts = new HashMap<>();
    String postText, postMultimediaURL;
    private StorageReference mStorageRef, postImages;
    private final static int gallerycode = 1;
    ArrayList<String> selectedDepartment = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        checkBoxes.setOnClickListener(view -> all.setChecked(false));
        all.setOnClickListener(view -> unCheckAll());
        Log.d("Details", auth.getCurrentUser().getDisplayName());
        Log.d("Details", auth.getCurrentUser().getPhotoUrl().toString());
        background.setOnClickListener(view -> showKeyboard());
        filterButton.setOnClickListener(view -> toggleBottomSheet());
        addMediaBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/* video/* ");
            startActivityForResult(intent, gallerycode);
        });
        goBackBtn.setOnClickListener(view -> {
            startActivity(new Intent(AddPost.this, Home.class));
            finish();
        });
        bsSubmit.setOnClickListener(view -> {
            checkDepartments();
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        });
        addButton.setOnClickListener(view -> {
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
                addImageToDB(mImageUri, posts);
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
                pushToDB(posts);
            }


        });
        if (!isConnectionAvailable()) {
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

    private void pushToDB(Map<String, Object> posts) {
        firebaseFirestore.collection("posts").add(posts)
                .addOnSuccessListener(documentReference -> {
                    String chatId = documentReference.getId();
                    firebaseFirestore.collection("users").document(auth.getUid()).collection("Timeline").document(chatId).set(posts);
                    mProgress.dismiss();
                    startActivity(new Intent(AddPost.this, Home.class));
                    finish();
                });

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


    private void addImageToDB(Uri mImageUri, Map<String, Object> posts) {
        postImages = mStorageRef.child("media/").child(mImageUri.getLastPathSegment());
        postImages.putFile(mImageUri).continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return postImages.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                posts.put("multimediaURL", downloadUri.toString());
                pushToDB(posts);

            } else {
                Log.d("upload failed: ", task.getException().getMessage());
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
