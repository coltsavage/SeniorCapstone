package pdx.com.nikeelevatorpitch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pdx.com.nikeelevatorpitch.team.BackendControl;
import pdx.com.nikeelevatorpitch.team.CategoriesCallback;
import pdx.com.nikeelevatorpitch.team.Category;

/**
 * Created by Colt on 7/28/2017.
 */

class NavigationItemsManager implements CategoriesCallback {

    interface NavigationItemsAvailableListener {
        void onNavigationItemsAvailable(List<String> navItems);
    }

    private static NavigationItemsManager mInstance;
    private NavigationItemsAvailableListener mListener;
    private List<String> mNavItems;
    private List<Category> mCategories;
    private int mIndexOfFirstCategory;
    private int mNavItemSelected;
    private Category mSelectedSubcategory;
    private int mPrevNavItem;

    static NavigationItemsManager getInstance() {
        if (mInstance == null) {
            mInstance = new NavigationItemsManager();
        }
        return mInstance;
    }

    private NavigationItemsManager() {
        mNavItems = new ArrayList<>();
        mNavItemSelected = -1;
        mPrevNavItem = -1;
    }

    void clearSelection() {
        mNavItemSelected = -1;
    }

    String getNavigationItemName(int index) {
        return mNavItems.get(index);
    }

    int getNavigationItemPosition(String name) {
        return mNavItems.indexOf(name);
    }

    List<String> getNavigationItems() {
        return mNavItems;
    }

    Category getSelectedCategory() {
        if ( isCategory(mNavItemSelected) ) {
            return mCategories.get(mNavItemSelected - mIndexOfFirstCategory);
        }
        else {
            return null;
        }
    }

    String[] getSelectionString() {
        if ( isCategory(mNavItemSelected) ) {
            ArrayList<String> selectionStr = new ArrayList<String>();
            selectionStr.add( getNavigationItemName(mNavItemSelected) );

            if (mSelectedSubcategory != null) {
                selectionStr.add( mSelectedSubcategory.getName() );
            }

            return selectionStr.toArray(new String[0]);
        } else {
            return null;
        }
    }

    int getSelectedItemPosition() {
        return mNavItemSelected;
    }

    boolean isEmpty() {
        return mCategories == null;
    }

    boolean isNavItemSeparator(int index) {
        return index == mIndexOfFirstCategory - 1;
    }

    boolean isSelectionCategory() {
        return isCategory(mNavItemSelected);
    }

    void onBackPressed() {
        restoreSelectionOnFailedSubcategorySelection();
    }

    public void onResult(List<Category> categories) {
        mCategories = categories;

        mNavItems.add("");   // category title placeholder/separator
        mIndexOfFirstCategory = mNavItems.size();
        for (Category c: mCategories) {
            mNavItems.add( c.getName() );
        }

        mListener.onNavigationItemsAvailable( getNavigationItems() );
    }

    void requestCategories(BackendControl source) {
        source.getCategories(this);
    }

    void setItemAsSelected(int index) {
        if ( isCategory(index) ) {
            preserveSelectionDuringSubcategorySelection();
        }
        mNavItemSelected = index;
    }

    void setNavigationItemsAvailableListener(NavigationItemsAvailableListener listener) {
        mListener = listener;
    }

    void setNonCategoryItems(String[] navItems) {
        mNavItems.addAll( Arrays.asList(navItems) );
    }

    void setSelectedSubcategory(Category subcategory) {
        clearRestoreOnSuccessfulSubcategorySelection();
        mSelectedSubcategory = subcategory;
    }

    private void clearRestoreOnSuccessfulSubcategorySelection() {
        mPrevNavItem = -1;
    }

    private boolean isCategory(int index) {
        return index >= mIndexOfFirstCategory;
    }

    private void preserveSelectionDuringSubcategorySelection() {
        mPrevNavItem = mNavItemSelected;
    }

    private void restoreSelectionOnFailedSubcategorySelection() {
        if (mPrevNavItem != -1) {
            mNavItemSelected = mPrevNavItem;
            mPrevNavItem = -1;
        }
    }
}
