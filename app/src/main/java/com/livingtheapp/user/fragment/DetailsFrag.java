package com.livingtheapp.user.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.livingtheapp.user.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFrag extends Fragment {
    private static final String TAG = "SpeedDial";


    public DetailsFrag() {
        // Required empty public constructor
    }
    /**
     * @return A new instance of fragment SpeedDialFragment.
     */
    public static DetailsFrag newInstance() {
        return new DetailsFrag();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_details, container, false);

        return root;
    }
}
