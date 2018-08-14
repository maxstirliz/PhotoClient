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

import java.util.List;

import lymansky.artem.photoclient.R;
import lymansky.artem.photoclient.adapters.PhotoAdapter;
import lymansky.artem.photoclient.model.Photo;
import lymansky.artem.photoclient.model.PhotoViewModel;
import lymansky.artem.photoclient.presenter.Client;
import lymansky.artem.photoclient.presenter.KeyHolder;
import lymansky.artem.photoclient.presenter.PhotoDataSource;
import lymansky.artem.photoclient.presenter.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements PhotoDataSource.SearchQueryListener {

    private static final int SPAN_COUNT_PORTRAY = 2;
    private static final int SPAN_COUNT_LANDSCAPE = 3;

    private GridLayoutManager layoutManager;

    private RecyclerView rv;
    private PhotoAdapter adapter;
    private List<Photo> photos;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PhotoDataSource.setSearchQueryListener(this);

        if(savedInstanceState != null) {
            query = savedInstanceState.getString(KeyHolder.KEY_QUERY);
        }

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

    @Override
    public String getSearchQuery() {
        return query;
    }
}
