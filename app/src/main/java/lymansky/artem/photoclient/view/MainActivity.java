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
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import lymansky.artem.photoclient.R;
import lymansky.artem.photoclient.adapters.PhotoAdapter;
import lymansky.artem.photoclient.model.Photo;
import lymansky.artem.photoclient.model.PhotoViewModel;
import lymansky.artem.photoclient.presenter.PhotoDataSource;

public class MainActivity extends AppCompatActivity implements PhotoDataSource.SearchQueryListener {

    private static final int SPAN_COUNT_PORTRAY = 2;
    private static final int SPAN_COUNT_LANDSCAPE = 3;

    private GridLayoutManager layoutManager;
    private RecyclerView rv;
    private PhotoAdapter adapter;
    private String query;
    private PhotoViewModel photoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PhotoDataSource.setSearchQueryListener(this);

        adapter = new PhotoAdapter();
        rv = findViewById(R.id.recyclerView);

        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setLayoutManager(SPAN_COUNT_PORTRAY);
        } else if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setLayoutManager(SPAN_COUNT_LANDSCAPE);
        }

        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);

        photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);


        photoViewModel.getPhotoPagedList().observe(this, new Observer<PagedList<Photo>>() {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setFocusable(false);
        searchView.setQueryHint("Search here!");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                query = s;
                photoViewModel.updateData(MainActivity.this);
                photoViewModel.getPhotoPagedList().observe(MainActivity.this, new Observer<PagedList<Photo>>() {
                    @Override
                    public void onChanged(@Nullable PagedList<Photo> photos) {
                        adapter.submitList(photos);
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }

    @Override
    public String getSearchQuery() {
        return query;
    }
}
