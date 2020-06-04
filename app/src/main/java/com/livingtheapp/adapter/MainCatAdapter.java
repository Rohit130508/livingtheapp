package com.livingtheapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.livingtheapp.modal.MainCatModal;
import com.livingtheapp.user.MainActivity;
import com.livingtheapp.user.R;
import com.livingtheapp.user.SubCategory;
import com.livingtheapp.user.utils.Utils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainCatAdapter extends RecyclerView.Adapter<MainCatAdapter.ViewHolder>
{
    private ArrayList<MainCatModal> arrayList;
    private ItemListener mListener;
    private Context context;
    public MainCatAdapter(MainActivity mainActivity, ArrayList<MainCatModal> arrayList, ItemListener itemListener) {
        this.arrayList = arrayList;
        mListener=itemListener;
        context = mainActivity;
    }

    @NonNull
    @Override
    public MainCatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_cat , parent, false);
        return new MainCatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainCatAdapter.ViewHolder holder, int position) {

       holder.setData(arrayList.get(position));


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgBeau;
        MainCatModal item;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBeau = itemView.findViewById(R.id.imgBeau);
            imgBeau.setOnClickListener(v ->   context.startActivity(new Intent(context, SubCategory.class)
                    .putExtra("id",item.getId())
                    .putExtra("name",item.getName())));

        }

        void setData(MainCatModal item) {
            this.item = item;

            Utils.Picasso(item.getImagefile(), imgBeau);

        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }

    public interface ItemListener {
        void onItemClick(MainCatModal item);
    }
}
