package lymansky.artem.photoclient.adapters;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import lymansky.artem.photoclient.R;
import lymansky.artem.photoclient.model.Photo;

public class PhotoAdapter extends PagedListAdapter<Photo, PhotoAdapter.PhotoViewHolder> {

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
        if(photo != null) {
            photoViewHolder.bindTo(photo);
        }
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView idText;

        private PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            idText = itemView.findViewById(R.id.idText);
        }

        private void bindTo(Photo photo) {

            //TODO: Glide setting for image to fit the CardView
            Glide.with(imageView)
                    .load(photo.getUrls().getThumb())
                    .into(imageView);
            idText.setText(photo.getId());
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
