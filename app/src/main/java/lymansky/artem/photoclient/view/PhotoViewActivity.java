package lymansky.artem.photoclient.view;

import android.Manifest;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import lymansky.artem.photoclient.R;
import lymansky.artem.photoclient.model.KeyHolder;

public class PhotoViewActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private static final String JPG_EXTENSION = ".jpg";
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 123;

    private PhotoView mImageView;
    private String mImageUrl;
    private String mDownloadUrl;
    private String mId;
    private ImageButton mPopup;
    private Bitmap mBitmapCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_photo_view);

        //Getting Photo data
        Intent intent = getIntent();
        mImageView = findViewById(R.id.fullscreenPicture);
        mPopup = findViewById(R.id.popUp);
        mImageUrl = intent.getStringExtra(KeyHolder.KEY_PIC_VIEW_LINK);
        mDownloadUrl = intent.getStringExtra(KeyHolder.KEY_PIC_DOWNLOAD_LINK);
        mId = intent.getStringExtra(KeyHolder.KEY_PIC_ID);

        new GetImageFromUrlTask(mImageView).execute(mImageUrl);

        mPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.download:
                if (isExternalStorageWritable()) {
                    checkPermission();
                } else {
                    Toast.makeText(this, R.string.storage_problems, Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.set_wallpaper:
                WallpaperManager wpManager = WallpaperManager.getInstance(getApplicationContext());
                try {
                    mBitmapCache = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
                    wpManager.setBitmap(mBitmapCache);
                    Toast.makeText(this, R.string.wallpaper_set, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE: {
                if(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadFile(mImageUrl);
                } else {
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //Custom methods
    private void showMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            popupMenu.setGravity(Gravity.END);
        }
        popupMenu.setOnMenuItemClickListener(this);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.photo_view_activity_menu, popupMenu.getMenu());
        popupMenu.show();
    }

    //Check permission to write on SD card
    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, R.string.need_write_permission, Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
            }
        } else {
            downloadFile(mImageUrl);
        }
    }

    private void downloadFile(String uriString) {
        Uri uri = Uri.parse(uriString);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(mId + JPG_EXTENSION);
        request.setDescription(getString(R.string.downloading));
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, mId + JPG_EXTENSION);
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
    }

    //Check external storage on WRITE permission
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private class GetImageFromUrlTask extends AsyncTask<String, Void, Bitmap> {

        PhotoView imageView;

        GetImageFromUrlTask(PhotoView image) {
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
