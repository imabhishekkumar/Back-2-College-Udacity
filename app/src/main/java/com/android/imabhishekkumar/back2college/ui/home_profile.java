package com.android.imabhishekkumar.back2college.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.imabhishekkumar.back2college.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link home_profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link home_profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class home_profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    @BindView(R.id.profileName)
    TextView nameTV;
    @BindView(R.id.profileEmail)
    TextView emailTV;
    @BindView(R.id.profileDept)
    TextView deptTV;
    @BindView(R.id.profileRoll)
    TextView rollTV;
    @BindView(R.id.profileDOB)
    TextView dobTV;
    @BindView(R.id.profileMobile)
    TextView mobileTV;
    @BindView(R.id.profileReg)
    TextView regTV;
    @BindView(R.id.profileAvatar)
    CircleImageView avatarIV;
    // @BindView(R.id.pass)
    //ImageView QR;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    private OnFragmentInteractionListener mListener;

    public home_profile() {
        // Required empty public constructor
    }

    public static home_profile newInstance(String param1, String param2) {
        home_profile fragment = new home_profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home_profile, container, false);
        ButterKnife.bind(this, view);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore.collection("users").document(firebaseAuth.getUid()).addSnapshotListener((documentSnapshot, e) -> {
            String name = documentSnapshot.get("displayName").toString();
            String department = "Department: " + documentSnapshot.get("department").toString();
            String register = documentSnapshot.get("registerNumber").toString();
            String roll = "Roll number: " + documentSnapshot.get("rollNumber").toString();
            String email = "Email: " + documentSnapshot.get("email").toString();
            String mobileNumber = "Phone: " + documentSnapshot.get("mobileNumber").toString();
            String dateOfBirth = "Date of Birth: " + documentSnapshot.get("dateOfBirth").toString();
            String avatar = documentSnapshot.get("displayImage").toString();
            nameTV.setText(name);
            deptTV.setText(department);
            regTV.setText(register);
            rollTV.setText(roll);
            emailTV.setText(email);
            mobileTV.setText(mobileNumber);
            dobTV.setText(dateOfBirth);


            Glide.with(view)
                    .load(avatar)
                    .into(avatarIV)
            ;
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
