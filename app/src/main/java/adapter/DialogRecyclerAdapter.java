package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.se.blueboard.R;

import model.Message;
import model.User;
import utils.Utils;

public class DialogRecyclerAdapter extends FirestoreRecyclerAdapter<User, DialogRecyclerAdapter.DialogViewHolder> {
    private Context mContext;
    public DialogRecyclerAdapter(@NonNull FirestoreRecyclerOptions<User> options, Context context) {
        super(options);
        mContext = context;
    }
    public class DialogViewHolder extends RecyclerView.ViewHolder {
        TextView name, studentId;
        public DialogViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.Dialog_Name);
            studentId = v.findViewById(R.id.Dialog_studentId);

            v.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }
    @Override
    public void onBindViewHolder(@NonNull DialogViewHolder holder, int position, User user) {
        Log.d("onBindViewHolder: ", user.toString());
        holder.name.setText(user.getName());
        holder.studentId.setText(Utils.masking(String.valueOf(user.getStudentId())));
    }
    @Override
    public DialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);

        DialogViewHolder vh = new DialogViewHolder(v);
        return vh;
    }
    public interface OnItemClickListener {
        void onItemClick(User user);
    }
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    @Override
    public void onDataChanged() {
        // Called each time there is a new query snapshot. You may want to use this method
        // to hide a loading spinner or check for the "no documents" state and update your UI.
        // ...
        super.onDataChanged();

    }
    @Override
    public void onError(FirebaseFirestoreException e) {
        // Called when there is an error getting a query snapshot. You may want to update
        // your UI to display an error message to the user.
        // ...
        Log.d("onError: ", e.getMessage());
    }
}
