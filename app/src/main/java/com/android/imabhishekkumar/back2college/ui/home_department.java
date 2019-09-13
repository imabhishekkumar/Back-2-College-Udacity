package com.android.imabhishekkumar.back2college.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.android.imabhishekkumar.back2college.R;
import com.android.imabhishekkumar.back2college.model.ModelPost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link home_department.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link home_department#newInstance} factory method to
 * create an instance of this fragment.
 */
public class home_department extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private RecyclerView mRecyclerView;
    PostRecyclerView postRecyclerView;
    FirebaseFirestore firebaseFirestore;
    ImageButton infoBtn;
    private ProgressDialog mProgress;
    DocumentReference documentReference;
    final String TAG= "MainActivity";
    String department;
    public home_department() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home_department.
     */
    // TODO: Rename and change types and number of parameters
    public static home_department newInstance(String param1, String param2) {
        home_department fragment = new home_department();
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
        View view= inflater.inflate(R.layout.fragment_home_department, container, false);
        mAuth = FirebaseAuth.getInstance();
        mRecyclerView = view.findViewById(R.id.recyclerViewDept);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUser = mAuth.getCurrentUser();
        final List<ModelPost> modelList = new ArrayList<>();
        mProgress = new ProgressDialog(getContext());
        mProgress.setMessage("Please wait");
        mProgress.show();
        firebaseFirestore = FirebaseFirestore.getInstance();
        documentReference = firebaseFirestore.collection("users").document(mUser.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                department=task.getResult().get("department").toString();
                getPost(modelList);
        }});

        return view ;
    }
    private void getPost(final List<ModelPost> modelList) {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference collRef = rootRef.collection("posts");
        Query q= collRef.whereArrayContains("postTo",department);
       // Query query = collRef.whereEqualTo("postTo", department);
        q.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        final ModelPost modelPost = new ModelPost();
                        modelPost.setPost(document.getString("details"));
                        Log.d("Time",document.getString("time"));
                        modelPost.setTimestamp(document.getLong("timestamp"));
                        if (document.getString("multimediaURL") != null) {
                            modelPost.setWebLink(document.getString("multimediaURL"));
                            Log.d("web", document.getString("multimediaURL"));

                        }
                        modelPost.setAvatarURL(document.getString("avatar"));
                        modelPost.setUserName(document.getString("name"));

                        modelList.add(modelPost);

                        //Collections.reverse(modelList);
                        postRecyclerView = new PostRecyclerView(getContext(), modelList);
                        mRecyclerView.setAdapter(postRecyclerView);
                        postRecyclerView.notifyDataSetChanged();
                        mProgress.dismiss();
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });




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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
