package lymansky.artem.photoclient.presenter;

import android.arch.paging.PagedListAdapter;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import lymansky.artem.photoclient.R;
import lymansky.artem.photoclient.model.Photo;
import lymansky.artem.photoclient.model.KeyHolder;
import lymansky.artem.photoclient.view.PhotoViewActivity;

public class PhotoAdapter extends PagedListAdapter<Photo, PhotoAdapter.PhotoViewHolder> {

    private static final int TRANSITION_DURATION = 1000;

    public PhotoAdapter() {
        super(DIFF_CALLBACK);
    }


    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.photo_holder, viewGroup, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder photoViewHolder, int i) {
        Photo photo = getItem(i);
        if (photo != null) {
            photoViewHolder.bindTo(photo);
        }
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView idText;
        TextView username;
        Photo boundPhoto;

        private PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            idText = itemView.findViewById(R.id.byText);
            username = itemView.findViewById(R.id.idText);
            imageView.setOnClickListener(this);
        }

        private void bindTo(Photo photo) {
            boundPhoto = photo;
            Glide.with(imageView)
                    .load(photo.getUrls().getThumb())
                    .apply(RequestOptions.centerCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade(TRANSITION_DURATION))
                    .into(imageView);
            username.setText(photo.getUser().getUsername());
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), PhotoViewActivity.class);
            intent.putExtra(KeyHolder.KEY_PIC_VIEW_LINK, boundPhoto.getUrls().getRegular());
            intent.putExtra(KeyHolder.KEY_PIC_DOWNLOAD_LINK, boundPhoto.getLinks().getDownload());
            intent.putExtra(KeyHolder.KEY_PIC_ID, boundPhoto.getId());
            view.getContext().startActivity(intent);
        }
    }

    private static DiffUtil.ItemCallback<Photo> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Photo>() {
                @Override
                public boolean areItemsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
                    return oldItem == newItem;
                }
            };
}
