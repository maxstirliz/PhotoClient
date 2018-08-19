package lymansky.artem.photoclient.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.InputStream;
import java.net.URL;

import lymansky.artem.photoclient.R;
import lymansky.artem.photoclient.presenter.KeyHolder;

public class PhotoViewActivity extends AppCompatActivity {

    private PhotoView imageView;
    private String imageUrl;
    private String downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_photo_view);

        Intent intent = getIntent();
        imageView = findViewById(R.id.fullscreenPicture);
        imageUrl = intent.getStringExtra(KeyHolder.KEY_PIC_VIEW_LINK);
        downloadUrl = intent.getStringExtra(KeyHolder.KEY_PIC_DOWNLOAD_LINK);

        new DownloadImageTask(imageView).execute(imageUrl);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        PhotoView imageView;

        public DownloadImageTask(PhotoView image) {
            this.imageView = image;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
