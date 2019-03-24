package com.example.abchar;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;



public class ChildAdapter extends FirestoreRecyclerAdapter<FireModel, ChildAdapter.ChildHolder> {

    public ChildAdapter(@NonNull FirestoreRecyclerOptions<FireModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChildHolder holder, int position, @NonNull FireModel model) {
        holder.title.setText(model.getName());
        holder.age.setText(String.valueOf(model.getAge()));
        holder.usagetime.setText(String.valueOf(model.getUsageTime()));
    }

    @NonNull
    @Override
    public ChildHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_item, viewGroup, false);
        return new ChildHolder(v);
    }


    class ChildHolder  extends RecyclerView.ViewHolder {

        TextView title;
        TextView age;
        TextView usagetime;

        public ChildHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_view_title);
            age = itemView.findViewById(R.id.age_numb);
            usagetime = itemView.findViewById(R.id.usage_time);
        }
    }
}
