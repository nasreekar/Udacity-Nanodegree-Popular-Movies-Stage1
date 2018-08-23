package com.example.abhijithsreekar.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.abhijithsreekar.popularmovies.Adapters.CustomMoviesAdapter;
import com.example.abhijithsreekar.popularmovies.Interface.MovieInterface;
import com.example.abhijithsreekar.popularmovies.Models.Movie;
import com.example.abhijithsreekar.popularmovies.Models.MovieResponse;
import com.example.abhijithsreekar.popularmovies.Network.APIClient;
import com.example.abhijithsreekar.popularmovies.Utils.MovieUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.rv_main)
    public RecyclerView rvMain;

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    private static Retrofit retrofit;
    private static String API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.popular_movies);
        API_KEY = getResources().getString(R.string.API_KEY);

        ButterKnife.bind(this);
        progressBar.setVisibility(View.VISIBLE);
        getPopularMovies();
    }

    // TODO - Refactor popular movies and top rated movies code into one function - Part 2
    private void getPopularMovies() {
        if (new MovieUtils().isNetworkAvailable(this)) {
            if (retrofit == null) {
                retrofit = APIClient.getRetrofitInstance();
            }
            MovieInterface movieService = retrofit.create(MovieInterface.class);
            Call<MovieResponse> call = movieService.getPopularMovies(API_KEY);
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    progressBar.setVisibility(View.INVISIBLE);
                    List<Movie> movies = response.body().getResults();
                    generateMovieList(movies);
                    Log.d(TAG, "Number of movies received: " + movies.size());
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.i("Network Connection Status", "Not available");
        }
    }

    private void getTopRatedMovies() {
        if (new MovieUtils().isNetworkAvailable(this)) {
            if (retrofit == null) {
                retrofit = APIClient.getRetrofitInstance();
            }
            MovieInterface movieService = retrofit.create(MovieInterface.class);
            Call<MovieResponse> call = movieService.getTopRatedMovies(API_KEY);
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    progressBar.setVisibility(View.INVISIBLE);
                    List<Movie> movies = response.body().getResults();
                    generateMovieList(movies);
                    Log.d(TAG, "Number of movies received: " + movies.size());
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.i("Network Connection Status", "Not available");
        }
    }

    private void generateMovieList(final List<Movie> results) {
        CustomMoviesAdapter adapter = new CustomMoviesAdapter(this, results, new CustomMoviesAdapter.MovieItemClickListener() {
            @Override
            public void onMovieItemClick(int clickedItemIndex) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("Movie", results.get(clickedItemIndex));
                startActivity(intent);
            }
        });

        initializeGridView(adapter);
    }

    private void initializeGridView(CustomMoviesAdapter adapter) {
        rvMain.setHasFixedSize(true);
        rvMain.setLayoutManager(new GridLayoutManager(this, 2));
        rvMain.setAdapter(adapter);

        // TODO - Implement Pagination (onScrollListener) - Part 2
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.topRated:
                getTopRatedMovies();
                setTitle(R.string.toprated_movies);
                break;
            case R.id.popular:
                getPopularMovies();
                setTitle(R.string.popular_movies);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
