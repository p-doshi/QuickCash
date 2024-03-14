package dal.cs.quickcash3.jobs;

import android.app.appsearch.SearchResult;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import dal.cs.quickcash3.R;
public class SearchJobActivity extends AppCompatActivity {

    private JobSearchHandler searchHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobs_search_page);
        searchHandler = new JobSearchHandler();
        this.setUpSearchBar();

    }


    public void setUpSearchBar(){
        SearchView searchView = findViewById(R.id.searchBar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                handleSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
            });
    }

    private void handleSearch(String query) {

        LinearLayout searchResultsContainer = findViewById(R.id.searchResultsContainer);
        searchResultsContainer.removeAllViews();

        SearchResult[] searchResults = searchHandler.search(query);


        for (SearchResult result : searchResults) {
            View searchResultsView = LayoutInflater.from(this).inflate(R.layout.jobs_search_page, null);
            searchResultsContainer.addView(searchResultsView);
        }
    }
}
