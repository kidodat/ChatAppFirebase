package com.google.firebase.udacity.friendlychat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.MediaController;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.transcode.BitmapBytesTranscoder;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import static android.support.v7.widget.RecyclerView.*;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.google.firebase.udacity.friendlychat.R.id.videoView;

public class MessageAdapter extends ArrayAdapter<FriendlyMessage> {

    private Context mContext = null;
    ViewHolder holder;

    public MessageAdapter(Context context, int resource, List<FriendlyMessage> objects) {
        super(context, resource, objects);
        this.mContext = context;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //holder = new ViewHolder();
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }

        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.photoImageView);
        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        final VideoView videoView = (VideoView) convertView.findViewById(R.id.videoView);

        FriendlyMessage message = getItem(position);

        boolean isPhoto = message.getPhotoUrl() != null;
        if (isPhoto) {
            if(message.getType() != null) {
                if (message.getType().contains("video")) {
                    //videoを画面い表示
                    messageTextView.setVisibility(View.GONE);
                    photoImageView.setVisibility(View.GONE);
                    videoView.setVisibility(View.VISIBLE);
                    videoView.setVideoURI(Uri.parse(message.getPhotoUrl()));
                    videoView.seekTo(0);

                    //tesing about MediaController ** add str
                    //videoView.setMediaController(new MediaController(getApplicationContext()));
                    Context context = parent.getContext();
                    videoView.setMediaController(new MediaController(mContext));
                    //tesing about MediaController ** add end
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            videoView.seekTo(100);
                        }
                    });

//                    videoView.setOnTouchListener(new OnTouchListener() {
//                        @Override
//                        public boolean onTouch(View view, MotionEvent motionEvent) {
//                            if(videoView.getLayoutParams().height==150){
//                                videoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//                            }else{
//
//                            }
//                            return false;
//                        }
//                    });
                } else{
                    //イメージ表示
                    messageTextView.setVisibility(View.GONE);
                    videoView.setVisibility(View.GONE);
                    photoImageView.setVisibility(View.VISIBLE);
                    Glide.with(photoImageView.getContext())
                            .load(message.getPhotoUrl())
                            .thumbnail(0.1f)
                            .into(photoImageView);
                }
            }
        } else {
            videoView.setVisibility(View.GONE);
            messageTextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            messageTextView.setText(message.getText());
        }
        authorTextView.setText(message.getName());

        return convertView;
    }


}
