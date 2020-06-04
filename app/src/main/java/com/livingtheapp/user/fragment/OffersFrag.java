package com.livingtheapp.user.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.livingtheapp.user.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OffersFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OffersFrag  extends Fragment {
    private static final String TAG = "SpeedDial";


    public OffersFrag() {
        // Required empty public constructor
    }
    /**
     * @return A new instance of fragment SpeedDialFragment.
     */
    public static OffersFrag newInstance() {
        return new OffersFrag();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_offers, container, false);

        return root;
    }
}
