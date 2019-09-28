package com.android.imabhishekkumar.back2college.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.imabhishekkumar.back2college.R;
import com.android.imabhishekkumar.back2college.adapters.FirebaseRecyclerAdapter;
import com.android.imabhishekkumar.back2college.model.ModelPost;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.aviran.cookiebar2.CookieBar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link home_all.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link home_all#newInstance} factory method to
 * create an instance of this fragment.
 */
public class home_all extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private RecyclerView mRecyclerView;
    private ConstraintLayout nothingToShow;
    private RelativeLayout relativeLayout;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference postReference;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private Query query;
    private FirestoreRecyclerOptions<ModelPost> options;


    public home_all() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home_all.
     */
    // TODO: Rename and change types and number of parameters
    public static home_all newInstance(String param1, String param2) {
        home_all fragment = new home_all();
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
        View view = inflater.inflate(R.layout.fragment_fragment_all, container, false);
        mAuth = FirebaseAuth.getInstance();
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUser = mAuth.getCurrentUser();
        nothingToShow = view.findViewById(R.id.nothingToShow);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        final List<ModelPost> modelList = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();

        postReference = firebaseFirestore.collection("posts");
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();

        query = postReference.whereArrayContains("postTo", "all").orderBy("timestamp", Query.Direction.DESCENDING);
        options = new FirestoreRecyclerOptions.Builder<ModelPost>()
                .setQuery(query, ModelPost.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter(options);
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseFirestore.setFirestoreSettings(settings);


        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
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
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuth == null){
            startActivity(new Intent(getContext(), RegisterActivity.class));}
        else{

            firebaseRecyclerAdapter.startListening();
        }

    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
