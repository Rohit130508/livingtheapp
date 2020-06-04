package com.livingtheapp.user.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


    RecyclerView rvList;
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

        rvList = root.findViewById(R.id.rvList);
        rvList.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        OfferAdapter adapter = new OfferAdapter();
        rvList.setAdapter(adapter);

        return root;
    }

    class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder>
    {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offer,
                    parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 3;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}
