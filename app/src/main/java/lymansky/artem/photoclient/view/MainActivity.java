package lymansky.artem.photoclient.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PageKeyedDataSource;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import lymansky.artem.photoclient.R;
import lymansky.artem.photoclient.presenter.DataSourceCallback;
import lymansky.artem.photoclient.presenter.PhotoAdapter;
import lymansky.artem.photoclient.model.Filter;
import lymansky.artem.photoclient.model.Photo;
import lymansky.artem.photoclient.model.PhotoViewModel;
import lymansky.artem.photoclient.presenter.PhotoDataSource;
import lymansky.artem.photoclient.presenter.PhotoDataSourceFiltered;

public class MainActivity extends AppCompatActivity implements DataSourceCallback {

    private static final int SPAN_COUNT_PORTRAY = 2;
    private static final int SPAN_COUNT_LANDSCAPE = 3;

    private GridLayoutManager layoutManager;
    private RecyclerView rv;
    private PhotoAdapter adapter;
    private Filter filter;
    private PhotoViewModel photoViewModel;
    private LinearLayout noInternetMessage;
    private LinearLayout emptyResultsMessage;
    private ImageView noInternetImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PhotoDataSource.setDataSourceCallback(this);
        PhotoDataSourceFiltered.setDataSourceCallback(this);

        noInternetMessage = findViewById(R.id.noInternetMessage);
        emptyResultsMessage = findViewById(R.id.emptyResultsMessage);
        noInternetImage = findViewById(R.id.no_internet_image);
        setMessagesToGone();

        adapter = new PhotoAdapter();
        rv = findViewById(R.id.recyclerView);

        if(PhotoViewModel.getFilter() == null) {
            filter = new Filter();
            PhotoViewModel.setFilter(filter);
        } else {
            filter = PhotoViewModel.getFilter();
        }

        //Configuration check
        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setupLayoutManager(SPAN_COUNT_PORTRAY);
        } else if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setupLayoutManager(SPAN_COUNT_LANDSCAPE);
        }


        photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
        photoViewModel.getPhotoPagedList().observe(this, new Observer<PagedList<Photo>>() {
            @Override
            public void onChanged(@Nullable PagedList<Photo> photos) {
                adapter.submitList(photos);
            }
        });

        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);

        noInternetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMessagesToGone();
                photoViewModel.updateData(MainActivity.this, filter);
                photoViewModel.getPhotoPagedList().observe(MainActivity.this, new Observer<PagedList<Photo>>() {
                    @Override
                    public void onChanged(@Nullable PagedList<Photo> photos) {
                        setMessagesToGone();
                        adapter.submitList(photos);
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setFocusable(false);
        searchView.setQueryHint(getString(R.string.search_here));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filter.setSearchQuery(s);
                setMessagesToGone();
                photoViewModel.updateData(MainActivity.this, filter);
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

    //DataSourceCallback methods
    @Override
    public void onConnectionFailure() {
        emptyResultsMessage.setVisibility(View.GONE);
        noInternetMessage.setVisibility(View.VISIBLE);

    }

    @Override
    public void onEmptyResponse() {
        noInternetMessage.setVisibility(View.GONE);
        emptyResultsMessage.setVisibility(View.VISIBLE);
    }

    //Custom methods
    private void setupLayoutManager(int span) {
        layoutManager = new GridLayoutManager(this, span, LinearLayoutManager.VERTICAL, false);
    }

    private void setMessagesToGone() {
        noInternetMessage.setVisibility(View.GONE);
        emptyResultsMessage.setVisibility(View.GONE);
    }
}
