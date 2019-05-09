package pdx.com.nikeelevatorpitch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import pdx.com.nikeelevatorpitch.team.Category;
import pdx.com.nikeelevatorpitch.team.SubcategorySelectionFragment;

public class SubcategorySelectionActivity extends NavigationToolbarActivity
        implements SubcategorySelectionFragment.SubcategorySelectionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Category category = (Category) getIntent().getSerializableExtra(EXTRA_CATEGORY);
        setTitle( category.getName() );
        SubcategorySelectionFragment frag = SubcategorySelectionFragment.newInstance(category);

        FragmentTransaction fragTrans = ( getSupportFragmentManager() ).beginTransaction();
        fragTrans.replace(getContentPaneResourceId(), frag);
        fragTrans.commit();
    }

    public void onSubcategorySelection(Category subcategory) {
        ( NavigationItemsManager.getInstance() ).setSelectedSubcategory(subcategory);

        Intent intent = new Intent( getApplicationContext(), ProductListActivity.class );
        intent.setAction(NavigationToolbarActivity.ACTION_GET_SELECT_PRODUCTS);
        intent.putExtra(NavigationToolbarActivity.EXTRA_CATEGORY, subcategory);
        startActivity(intent);
    }
}