package lymansky.artem.photoclient.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import lymansky.artem.photoclient.R;
import lymansky.artem.photoclient.presenter.FileDownloadClient;
import lymansky.artem.photoclient.presenter.KeyHolder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PhotoViewActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private PhotoView imageView;
    private String imageUrl;
    private String downloadUrl;
    private String id;
    private ImageButton popUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_photo_view);

        Intent intent = getIntent();
        imageView = findViewById(R.id.fullscreenPicture);
        popUp = findViewById(R.id.popUp);
        imageUrl = intent.getStringExtra(KeyHolder.KEY_PIC_VIEW_LINK);
        downloadUrl = intent.getStringExtra(KeyHolder.KEY_PIC_DOWNLOAD_LINK);
        Log.e("PhotoViewActivity", "download link --------------------------> " + downloadUrl);
        id = intent.getStringExtra(KeyHolder.KEY_PIC_ID);

        new GetImageFromUrl(imageView).execute(imageUrl);

        popUp.setOnClickListener(new View.OnClickListener() {
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
                Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show();
                downloadFile(downloadUrl);
                return true;
            case R.id.set_wallpaper:
                Toast.makeText(this, "Set as wallpaper...", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

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

    private void downloadFile(String link) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://unsplash.com")
                .build();
        FileDownloadClient client = retrofit.create(FileDownloadClient.class);
        Call<ResponseBody> call = client.downloadFile(link);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                writeToDisk(response.body());
                Toast.makeText(PhotoViewActivity.this, "File downloaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PhotoViewActivity.this, "Download error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void writeToDisk(ResponseBody body) {
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    + File.separator
                    + id
                    + ".jpg"
            );
            InputStream in = null;
            OutputStream out = null;
            try {
                byte[] fileReader = new byte[4096];
                in = body.byteStream();
                out = new FileOutputStream(file);

                while (true) {
                    int read = in.read(fileReader);
                    if (read == -1) {
                        break;
                    }

                    out.write(fileReader, 0, read);

                }
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {

        PhotoView imageView;

        public GetImageFromUrl(PhotoView image) {
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
