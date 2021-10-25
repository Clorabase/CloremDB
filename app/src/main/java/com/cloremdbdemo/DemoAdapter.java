package com.cloremdbdemo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.DemoHolder> {
    private ArrayList<Demo> demoArrayList;
    private Context context;
    private static final String TAG = DemoAdapter.class.getSimpleName();

    public DemoAdapter(Context context, ArrayList<Demo> demoArrayList) {
        this.context = context;
        this.demoArrayList = demoArrayList;
    }

    @NonNull
    @Override
    public DemoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.demo_list_item, parent, false);
        return new DemoHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull DemoHolder holder, int position) {
        Demo demo = demoArrayList.get(position);
        holder.demoTv.setText(demo.getDemoDescription());
        holder.itemView.setOnLongClickListener(l -> {
            AppCompatActivity activity = (AppCompatActivity) context;
            FragmentManager fm = activity.getSupportFragmentManager();

            Bundle b = new Bundle(); // This bundle will be used to pass required fields in delete dialog
            b.putString("timestamp", demo.getId());
            b.putString("demoText", demo.getDemoDescription());
            DeleteDialog deleteDialog = new DeleteDialog();
            deleteDialog.setArguments(b);
            deleteDialog.setListener((DeleteDialog.DeleteListener) context);
            deleteDialog.show(fm, "Tag");
            return true;
        });
    }


    @Override
    public int getItemCount() {
        if (demoArrayList == null) {
            return 0;
        }
        return demoArrayList.size();
    }

    static class DemoHolder extends RecyclerView.ViewHolder {
        TextView demoTv;

        public DemoHolder(@NonNull View itemView) {
            super(itemView);
            demoTv = itemView.findViewById(R.id.demo_tv);
        }
    }

    public void addObject(Demo demo) {
        demoArrayList.add(demo);
        notifyDataSetChanged();
    }

    public void deleteObject(String timestamp) {
        Demo demo1 = null;
        for (Demo demo : demoArrayList) {
            if (demo.getId().equals(timestamp)) {
                demo1 = demo;
            }
        }
        if (demo1 != null) {
            demoArrayList.remove(demo1);
            notifyDataSetChanged();
        }
    }

    public void updateObject(String timestamp, String demoText) {
        int id = 0;
        for (Demo demo : demoArrayList) {
            if (demo.getId().equals(timestamp)) {
                id = demoArrayList.indexOf(demo);
            }
        }
        Demo demo = new Demo(timestamp, demoText);
        demoArrayList.set(id, demo);
        notifyItemChanged(id);
    }
}
