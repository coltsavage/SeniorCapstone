package pdx.com.nikeelevatorpitch;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Colt on 7/29/2017.
 */

class NavigationItemsAdapter extends ArrayAdapter<String> {

    private Activity mHostActivity;
    private NavigationItemsManager mNavItemsManager;
    private List<String> mNavItems;

    NavigationItemsAdapter(Activity context, List<String> navItems) {
        super(context, R.layout.navigation_item_layout, navItems);
        mHostActivity = context;
        mNavItemsManager = NavigationItemsManager.getInstance();
        mNavItems = new ArrayList<>(navItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View navItemLayout = convertView;
        if (navItemLayout == null) {
            LayoutInflater inflater = mHostActivity.getLayoutInflater();
            navItemLayout = inflater.inflate(R.layout.navigation_item_layout, null);
        }

        TextView categoryGroupTitle = (TextView) navItemLayout.findViewById(R.id.navItemCategoryTitle);
        TextView navItem = (TextView) navItemLayout.findViewById(R.id.navItemName);

        if ( mNavItemsManager.isNavItemSeparator(position) ) {
            categoryGroupTitle.setVisibility(View.VISIBLE);
            navItem.setVisibility(View.GONE);
        } else {
            navItem.setVisibility(View.VISIBLE);
            navItem.setText( mNavItems.get(position) );
            categoryGroupTitle.setVisibility(View.GONE);
        }

        return navItemLayout;
    }

    @Override
    public boolean isEnabled(int position) {
        return !mNavItemsManager.isNavItemSeparator(position);
    }
}
