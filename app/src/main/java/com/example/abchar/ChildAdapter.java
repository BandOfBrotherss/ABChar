package com.example.abchar;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import org.w3c.dom.Text;


public class ChildAdapter extends FirestoreRecyclerAdapter<FireModel, ChildAdapter.ChildHolder> {

    private OnItemClickListener listener;

    public ChildAdapter(@NonNull FirestoreRecyclerOptions<FireModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChildHolder holder, int position, @NonNull FireModel model) {
        holder.title.setText(model.getName());
        holder.failTrial.setText(String.valueOf(model.getFailTrials()));
        holder.trueTrial.setText(String.valueOf(model.getSuccesTrials()));
        holder.trainingCount.setText(String.valueOf(model.getTrainingCount()));
    }

    @NonNull
    @Override
    public ChildHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_item, viewGroup, false);
        return new ChildHolder(v);
    }


    class ChildHolder  extends RecyclerView.ViewHolder {

        TextView title;
        TextView failTrial;
        TextView trueTrial;
        TextView trainingCount;

        public ChildHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_view_title);
            failTrial = itemView.findViewById(R.id.fail_numb);
            trueTrial = itemView.findViewById(R.id.true_numb);
            trainingCount = itemView.findViewById(R.id.train_count);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if ( position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;


    }
}
