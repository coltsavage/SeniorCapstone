package pdx.com.nikeelevatorpitch;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Colt on 7/29/2017.
 */

class NavigationDrawerList {

    interface ItemSelectedListener {
        void onItemSelected(String navItemName);
    }

    private Activity mHostActivity;
    private ArrayList<ItemSelectedListener> mItemSelectedListeners;
    private NavigationItemsManager mNavItemsManager;
    private ListView mNavDrawerListView;

    NavigationDrawerList(Activity activity) {
        mHostActivity = activity;
        mItemSelectedListeners = new ArrayList<>();
        mNavItemsManager = NavigationItemsManager.getInstance();

        mNavDrawerListView = mHostActivity.findViewById(R.id.nav_drawer_list);
        mNavDrawerListView.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        onNavDrawerItemClicked(position);
                    }
                } );
    }

    void addItemSelectedListener(ItemSelectedListener listener) {
        mItemSelectedListeners.add(listener);
    }

    void build(List<String> navItems) {
        mNavDrawerListView.setAdapter( new NavigationItemsAdapter(mHostActivity, navItems) );
        sync();
    }

    void clearSelection() {
        mNavItemsManager.clearSelection();
        mNavDrawerListView.clearChoices();
    }

    void setItemAsSelected(int position) {
        mNavItemsManager.setItemAsSelected(position);
        mNavDrawerListView.setItemChecked(position, true);
    }

    void sync() {
        mNavDrawerListView.setItemChecked(mNavItemsManager.getSelectedItemPosition(), true);
    }

    private void onNavDrawerItemClicked(int position) {
        mNavItemsManager.setItemAsSelected(position);
        String navItemName = mNavItemsManager.getNavigationItemName(position);
        for (ItemSelectedListener l: mItemSelectedListeners) {
            l.onItemSelected(navItemName);
        }
    }
}
