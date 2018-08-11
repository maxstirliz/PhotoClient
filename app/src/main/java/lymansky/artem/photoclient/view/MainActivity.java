package lymansky.artem.photoclient.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import lymansky.artem.photoclient.R;
import lymansky.artem.photoclient.adapters.PhotoAdapter;
import lymansky.artem.photoclient.model.Photo;
import lymansky.artem.photoclient.model.PhotoViewModel;

public class MainActivity extends AppCompatActivity {

    private static final int SPAN_COUNT_PORTRAY = 2;
    private static final int SPAN_COUNT_LANDSCAPE = 3;

    private GridLayoutManager layoutManager;

    private RecyclerView rv;
    private PhotoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.recyclerView);

        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setLayoutManager(SPAN_COUNT_PORTRAY);
        } else if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setLayoutManager(SPAN_COUNT_LANDSCAPE);
        }


        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);

        PhotoViewModel photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);

        final PhotoAdapter adapter = new PhotoAdapter();

        photoViewModel.photoPagedList.observe(this, new Observer<PagedList<Photo>>() {
            @Override
            public void onChanged(@Nullable PagedList<Photo> photos) {
                adapter.submitList(photos);
            }
        });

        rv.setAdapter(adapter);
    }

    private void setLayoutManager(int span) {
        layoutManager = new GridLayoutManager(this, span, LinearLayoutManager.VERTICAL, false);
    }
}
