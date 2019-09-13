package com.android.imabhishekkumar.back2college.ui;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.imabhishekkumar.back2college.R;
import com.android.imabhishekkumar.back2college.model.ModelPost;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostRecyclerView extends RecyclerView.Adapter<PostRecyclerView.ViewHolder> {

    private Context context;
    private List<ModelPost> modelPostArray;
    private LayoutInflater mLayoutInflator;
    String username, postContent, avatarURL, webURL;
    Long timestamp;


    public PostRecyclerView(Context context, List<ModelPost> data) {
        mLayoutInflator = LayoutInflater.from(context);
        modelPostArray = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.post_row,
                        viewGroup,
                        false);


        return new ViewHolder(view, context);


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        ModelPost currentPost = modelPostArray.get(i);
        username = currentPost.getUserName();
        avatarURL = currentPost.getAvatarURL();
        webURL = currentPost.getWebLink();
        postContent = currentPost.getPost();
        timestamp = currentPost.getTimestamp();
        if(postContent==null||postContent.isEmpty()){
            viewHolder.post.setVisibility(View.GONE);
        }

        if(webURL!=null){
            viewHolder.webView.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(webURL)
                    .placeholder(R.drawable.loading)
                    .into(viewHolder.webView);
        }
        Picasso.get()
                .load(avatarURL)
                .into(viewHolder.avatar);
        viewHolder.name.setText(username);
        viewHolder.post.setText(postContent);
        viewHolder.time.setReferenceTime(timestamp);

     /*   if (postContent != null) {

            //viewHolder.webView.setVisibility(View.VISIBLE);

        }*/

    }

    @Override
    public int getItemCount() {
        return modelPostArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        RelativeTimeTextView time;
        TextView post;
        CircleImageView avatar;
        ImageView webView;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            name = itemView.findViewById(R.id.nameTV);
            time = itemView.findViewById(R.id.timestamp);
            post = itemView.findViewById(R.id.postContentTV);
            avatar = itemView.findViewById(R.id.circleImageView);
            webView = itemView.findViewById(R.id.postWebView);

            webView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Intent intent = new Intent(view.getContext(), PhotoView.class);
                    intent.putExtra("imageUrl",modelPostArray.get(pos).getWebLink());
                    view.getContext().startActivity(intent);
                }
            });

        }
    }
}
