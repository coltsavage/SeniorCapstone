package pdx.com.nikeelevatorpitch;

import android.os.Bundle;

import pdx.com.nikeelevatorpitch.team.ProductListFragment;

public class NewsFeedActivity extends ProductListLogisticsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setProductListLayoutType( ProductListFragment.GRID);
        ( new ProductListRetriever() ).requestProductsViaTime( withinLastNumberOfDays(30) );
    }

    @Override
    protected void onStart() {
        super.onStart();

        setNavigationSelection( getString(R.string.news_feed) );
    }

    private Long withinLastNumberOfDays(int days) {
        return System.currentTimeMillis() - (86400000 * days); // ms in a day
    }
}