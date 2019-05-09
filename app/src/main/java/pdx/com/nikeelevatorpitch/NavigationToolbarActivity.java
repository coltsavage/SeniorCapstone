package pdx.com.nikeelevatorpitch;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.toolbox.ImageLoader;

import pdx.com.nikeelevatorpitch.team.BackendControl;
import pdx.com.nikeelevatorpitch.team.Connector;

public abstract class NavigationToolbarActivity extends AppCompatActivity {

    protected final static String EXTRA_PRODUCT = "extra_product";
    protected final static String EXTRA_CATEGORY = "extra_category_selection";
    protected final static String ACTION_GET_PINS = "action_get_pins";
    protected final static String ACTION_GET_SELECT_PRODUCTS = "action_get_select_products";

    private int mContentPaneResId;
    private BackendControl mBackend;
    private NavigationDrawer mNavDrawer;
    private NavigationItemsManager mNavItemsManager;

    /* Activity is being created. Perform basic app startup logic
     * that occurs only once during the lifetime of the app. Can
     * recover state that was saved prior being destroyed.
     * Immediately followed by onStart().
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_toolbar);

        mBackend = Connector.getInstance(this);
        mNavItemsManager = NavigationItemsManager.getInstance();
        mContentPaneResId = R.id.content_pane;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavDrawer = new NavigationDrawer(this);
        mNavDrawer.addItemSelectedListener( new NavigationDrawerItemSelectedListener() );

        if ( mNavItemsManager.isEmpty() ) {
            mNavItemsManager.setNonCategoryItems(
                    new String[]{
                        getString(R.string.news_feed)/*,
                        getString(R.string.pinned)*/
                    } );
            mNavItemsManager.setNavigationItemsAvailableListener(mNavDrawer);
            mNavItemsManager.requestCategories(mBackend);
        } else {
            mNavDrawer.buildNavigationMenu( mNavItemsManager.getNavigationItems() );
        }
    }

    private class NavigationDrawerItemSelectedListener
                implements NavigationDrawerList.ItemSelectedListener {

        public void onItemSelected(String navItemName) {
            Intent intent;

            if ( navItemName.equals(getString(R.string.news_feed) )) {
                intent = new Intent(getApplicationContext(), NewsFeedActivity.class);
            }
            else if ( navItemName.equals( getString(R.string.pinned) ) ) {
                intent = new Intent(getApplicationContext(), ProductListActivity.class);
                intent.setAction(ACTION_GET_PINS);
            }
            else if (mNavItemsManager.isSelectionCategory()) {
                intent = new Intent(getApplicationContext(), SubcategorySelectionActivity.class);
                intent.putExtra(EXTRA_CATEGORY, mNavItemsManager.getSelectedCategory());
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            }
            else {
                onItemSelected( getString(R.string.news_feed) );
                return;
            }
            startActivity(intent);
        }
    }

    /* Activity will soon be entering the foreground and becoming
     * visible to the user. Called immediately after onCreate() or
     * onRestart(), and immediately followed by onResume().
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    /* Opportunity to restore activity state after it had been stopped or
     * killed. Called after onStart(), and only if saved data exists. Is
     * the same data available to onCreate().
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /* Activity has entered the foreground and user will begin interacting.
     * Remains in this state until losing focus (ie. notification pop-up) or
     * user navigating elsewhere. For the former, the system invokes
     * onPause(), and either onResume() if the activity regains
     * focus, otherwise onStop(). onResume() should build whatever onPause()
     * tears down.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /* Activity has lost focus (note this does not mean it has been replaced
     * by another activity). Should release any resources that may
     * drain the battery. Upon regaining focus, system invokes onResume(),
     * otherwise onStop().
     * onPause() needs to be brief; no saving user data or heavy lifting. Do
     * this during onStop().
     */

    @Override
    protected void onPause() {
        super.onPause();
    }

    /* Opportunity to save activity state that can be restored when reactivated.
     * Is called during the transition from onPause() to onStop(). Should be
     * relatively light-weight; not to be used for all clean-up.
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    /* Activity is no longer visible to the user. Should release most all of
     * the resources unneeded by the user while not in use. System may kill
     * the process if grown stale, which may not always invoke onDestroy(),
     * so good practice is to prevent memory leaks in this state. Note that the
     * system retains View state regardless if destroyed. Upon user returning
     * to the activity, onRestart() is called.
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    /* Activity is being revisited by the user after viewing a different
     * activity. Previous state was onStop() and onStart() is immediately
     * following. Build any resources that were torn down by onStop().
     */
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /* Activity has either concluded, or the process is being reclaimed by
     * the system due to memory constraints. Cannot rely on it being called
     * by the system when the process is being killed, so best practice may
     * be to release resources during onStop().
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if ( !mNavDrawer.closeDrawer() ) {
            super.onBackPressed();
        }
        mNavItemsManager.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName cName = new ComponentName(getApplicationContext(), ProductListActivity.class);
        SearchableInfo sInfo = searchManager.getSearchableInfo(cName);

        final MenuItem menuItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setSearchableInfo(sInfo);
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }

                    public boolean onQueryTextSubmit(String query) {
                        menuItem.collapseActionView();
                        return false;
                    }
                } );

        return true;
    }

    protected void clearNavigationSelection() {
        mNavDrawer.clearSelection();
    }

    int getContentPaneResourceId() {
        return mContentPaneResId;
    }

    protected BackendControl getController() {
        return mBackend;
    }

    protected void setNavigationSelection(String navItemName) {
        int position = mNavItemsManager.getNavigationItemPosition(navItemName);
        mNavDrawer.setItemAsSelected(position);
    }

    protected void syncNavigationSelection() {
        mNavDrawer.sync();
    }

    public ImageLoader getImageLoader() {
        return mBackend.getImageLoader();
    }
}
