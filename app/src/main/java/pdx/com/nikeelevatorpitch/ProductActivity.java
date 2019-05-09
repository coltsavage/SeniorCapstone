package pdx.com.nikeelevatorpitch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import pdx.com.nikeelevatorpitch.team.Product;
import pdx.com.nikeelevatorpitch.team.ProductFragmentListener;
import pdx.com.nikeelevatorpitch.team.ProductFragmentType;
import pdx.com.nikeelevatorpitch.team.ProductLevelOne;
import pdx.com.nikeelevatorpitch.team.ProductLevelThree;
import pdx.com.nikeelevatorpitch.team.ProductLevelTwo;

public class ProductActivity extends NavigationToolbarActivity
            implements ProductFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createProductFragment( ProductFragmentType.PRODUCT_LVL_1);
    }

    @Override
    protected void onStart() {
        super.onStart();

        syncNavigationSelection();
    }

    public void switchProductLevel(ProductFragmentType productLvl) {
        createProductFragment(productLvl);
    }

    private void createProductFragment(ProductFragmentType productLvl) {
        Product product = (Product) ((getIntent()).getExtras()).get(EXTRA_PRODUCT);

        Fragment frag;
        switch (productLvl) {
            case PRODUCT_LVL_1:
                frag = ProductLevelOne.newInstance(product);
                break;
            case PRODUCT_LVL_2:
                frag = ProductLevelTwo.newInstance(product);
                break;
            case PRODUCT_LVL_3:
                frag = ProductLevelThree.newInstance(product);
                break;
            default:
                frag = ProductLevelOne.newInstance(product);
        }

        FragmentTransaction fragTrans = (getSupportFragmentManager()).beginTransaction();
        fragTrans.replace(getContentPaneResourceId(), frag);
        fragTrans.commit();
    }
}
