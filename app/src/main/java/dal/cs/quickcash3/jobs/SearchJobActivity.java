package dal.cs.quickcash3.jobs;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import dal.cs.quickcash3.R;
public class SearchJobActivity extends AppCompatActivity {

//    private JobSearchHandler searchHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobs_search_page);
//        searchHandler = new JobSearchHandler();
        this.setUpSearchBar();
        this.setUpFilterIcon();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ItemFragment())
                .commit();

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

    public void setUpFilterIcon(){
        ImageView filterIcon = findViewById(R.id.filterIcon);
        filterIcon.setOnClickListener(v -> {

        });

    }
    private void handleSearch(String query) {

//        LinearLayout searchResultsContainer = findViewById(R.id.searchResultsContainer);
//        searchResultsContainer.removeAllViews();
//
//        SearchResult[] searchResults = searchHandler.search(query);
//
//
//        for (SearchResult result : searchResults) {
//            View searchResultsView = LayoutInflater.from(this).inflate(R.layout.jobs_search_page, null);
//            searchResultsContainer.addView(searchResultsView);
//        }
    }
}
