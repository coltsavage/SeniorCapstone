package pdx.com.nikeelevatorpitch;

import android.app.Activity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import java.util.List;


/**
 * Created by Colt on 7/28/2017.
 */

class NavigationDrawer implements
        NavigationDrawerList.ItemSelectedListener,
        NavigationItemsManager.NavigationItemsAvailableListener {

    private Activity mHostActivity;
    private NavigationDrawerHeader mNavDrawerHeader;
    private NavigationDrawerList mNavDrawerList;

    NavigationDrawer(Activity activity) {
        mHostActivity = activity;
        mNavDrawerHeader = new NavigationDrawerHeader(mHostActivity);

        mNavDrawerList = new NavigationDrawerList(mHostActivity);
        mNavDrawerList.addItemSelectedListener(this);
    }

    void addItemSelectedListener(NavigationDrawerList.ItemSelectedListener listener) {
        mNavDrawerList.addItemSelectedListener(listener);
    }

    void buildNavigationMenu(List<String> navItems) {
        mNavDrawerList.build(navItems);
    }

    void clearSelection() {
        mNavDrawerList.clearSelection();
    }

    Boolean closeDrawer() {
        DrawerLayout drawer = (DrawerLayout) mHostActivity.findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    public void onItemSelected(String navItemName) {
        closeDrawer();
    }

    public void onNavigationItemsAvailable(List<String> navItems) {
        buildNavigationMenu(navItems);
    }

    void setItemAsSelected(int position) {
        mNavDrawerList.setItemAsSelected(position);
    }

    void sync() {
        mNavDrawerList.sync();
    }
}
