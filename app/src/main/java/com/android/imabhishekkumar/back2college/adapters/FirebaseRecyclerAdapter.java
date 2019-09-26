package com.android.imabhishekkumar.back2college.adapters;

import android.content.Intent;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.android.imabhishekkumar.back2college.R;
import com.android.imabhishekkumar.back2college.model.ModelPost;
import com.android.imabhishekkumar.back2college.ui.PhotoView;
import com.android.imabhishekkumar.back2college.utils.MenuHandler;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.google.firebase.firestore.DocumentSnapshot;
import de.hdodenhof.circleimageview.CircleImageView;

public class FirebaseRecyclerAdapter extends FirestoreRecyclerAdapter<ModelPost, FirebaseRecyclerAdapter.PostHolder> {


    public FirebaseRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ModelPost> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull PostHolder holder, final int position, @NonNull final ModelPost model) {
        DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String parentUId = snapshot.getId();
        String username = model.getName();
        String avatarURL = model.getAvatar();
        String webURL = model.getMultimediaURL();
        String postContent = model.getDetails();
        Long timestamp = model.getTimestamp();
        if (postContent == null || postContent.isEmpty()) {
            holder.post.setVisibility(View.GONE);
        }

        if (webURL != null) {
            holder.webView.setVisibility(View.VISIBLE);
            Glide.with(holder.webView.getContext())
                    .load(webURL)
                    .placeholder(R.drawable.loading)
                    .into(holder.webView);

        }
        Glide.with(holder.avatar.getContext())
                .load(avatarURL)
                .into(holder.avatar);

        holder.name.setText(username);
        holder.post.setText(postContent);
        holder.time.setReferenceTime(timestamp);
        holder.webView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PhotoView.class);
                intent.putExtra("imageUrl", model.getMultimediaURL());
                view.getContext().startActivity(intent);
            }
        });
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view, position,parentUId,model);
            }
        });
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_row,
                        parent,
                        false);


        return new PostHolder(view);
    }

    class PostHolder extends RecyclerView.ViewHolder {
        TextView name;
        RelativeTimeTextView time;
        TextView post;
        CircleImageView avatar;
        ImageView webView;
        ImageView moreBtn;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameTV);
            time = itemView.findViewById(R.id.timestamp);
            post = itemView.findViewById(R.id.postContentTV);
            avatar = itemView.findViewById(R.id.circleImageView);
            webView = itemView.findViewById(R.id.postWebView);
            moreBtn = itemView.findViewById(R.id.moreBtn);


        }
    }

    private void showPopupMenu(View view, int position, String parentUId, ModelPost modelPost) {

        PopupMenu popup = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.more_options, popup.getMenu());
        popup.setOnMenuItemClickListener(new MenuHandler(position,parentUId, view.getContext(),modelPost));
        popup.show();

    }
}
