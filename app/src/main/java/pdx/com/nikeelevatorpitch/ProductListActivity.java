package pdx.com.nikeelevatorpitch;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import pdx.com.nikeelevatorpitch.team.Category;
import pdx.com.nikeelevatorpitch.team.ProductListFragment;

public class ProductListActivity extends ProductListLogisticsActivity {

    private boolean mIsActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setProductListLayoutType( ProductListFragment.ROW);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mIsActive = true;

        handleIntent( getIntent() );
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIsActive = false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // oddly called prior onStart() during activity switch; must check if can be handled
        if (mIsActive) {
            handleIntent(intent);
        } else {
            setIntent(intent);
        }
    }

    private void handleIntent(Intent intent) {

        switch ( intent.getAction() ) {
            case Intent.ACTION_SEARCH:
                String query = intent.getStringExtra(SearchManager.QUERY);
                (new ProductListRetriever()).requestProductsViaSearch(query);

                clearNavigationSelection();
                setTitle( query.toUpperCase() );
                break;

            case ACTION_GET_PINS:
                (new ProductListRetriever()).requestProductsViaCategory(null);

                syncNavigationSelection();
                setTitle( getString(R.string.pinned) );
                break;

            case ACTION_GET_SELECT_PRODUCTS:
                String[] selection = ( NavigationItemsManager.getInstance() ).getSelectionString();
                (new ProductListRetriever()).requestProductsViaCategory(selection);

                syncNavigationSelection();
                setTitle( ((Category) getIntent().getSerializableExtra(EXTRA_CATEGORY)).getName() );
                break;

            default:
                Intent newIntent = new Intent(getApplicationContext(), NewsFeedActivity.class);
                startActivity(newIntent);
        }
    }
}
