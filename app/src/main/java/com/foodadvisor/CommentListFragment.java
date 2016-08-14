package com.foodadvisor;

import android.app.ListFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.foodadvisor.models.Comment;
import com.foodadvisor.models.Model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentListFragment extends ListFragment {

    List<Comment> comments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_comment_list, container, false);

        final ListView listview = (ListView) v.findViewById(android.R.id.list);

        // Inflate the layout for this fragment
        String restaurantId = this.getArguments().getString("restaurantId");
        Model.instance().getComments(Integer.parseInt(restaurantId), new Model.GetCommentsListener() {
            @Override
            public void done(final List<Comment> commentsRes) {
                Model.instance().loadImages(commentsRes, new Model.LoadImageListener(){

                    @Override
                    public void onResult(List<Comment> commentsWithImages) {
                        comments = commentsWithImages;
                        System.out.println("all set");
                        v.findViewById(R.id.loadingPanel).setVisibility(View.GONE);

                        final ListAdapter adapter = new ListAdapter(MyApplication.getContext(),
                                R.layout.comment_list_item, comments);
                        listview.setAdapter(adapter);
                    }
                });
            }
        });

        return v;
    }

    public class ListAdapter extends ArrayAdapter<Comment> {

        public ListAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public ListAdapter(Context context, int resource, List<Comment> items) {
            super(context, resource, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.comment_list_item, null);
            }

            Comment comment = getItem(position);

            if (comment != null) {
                TextView name = (TextView) v.findViewById(R.id.comment_name);
                TextView date = (TextView) v.findViewById(R.id.comment_date);
                TextView content = (TextView) v.findViewById(R.id.comment_text);
                ImageView image = (ImageView) v.findViewById(R.id.comment_image);
                TextView rate = (TextView) v.findViewById(R.id.comment_rate_number);

                if (name != null) {
                    name.setText(comment.getName());
                }

                if (date != null) {
                    date.setText(comment.getDate());
                }

                if (content != null) {
                    content.setText(comment.getContent());
                }

                if (image != null){
                    Picasso.with(MyApplication.getContext())
                        .load(comment.getImageUri())
                        .into(image);
                }

                if (rate != null){
                    rate.setText(comment.getRate().toString());
                }
            }

            return v;
        }
    }
}
