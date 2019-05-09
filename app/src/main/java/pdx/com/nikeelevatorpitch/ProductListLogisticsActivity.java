package pdx.com.nikeelevatorpitch;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

import pdx.com.nikeelevatorpitch.team.Product;
import pdx.com.nikeelevatorpitch.team.ProductListFragment;

public class ProductListLogisticsActivity extends NavigationToolbarActivity
        implements ProductListFragment.ProductSelectionListener {

    private ProductListFragment mProductListFragment;
    private int mProductListLayout;

    public void onProductSelection(Product product) {
        Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
        intent.putExtra(EXTRA_PRODUCT, product);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    protected void setProductListLayoutType(int layout) {
        mProductListLayout = layout;
    }

    protected class ProductListRetriever implements ProductListCallback {

        public void onResult(List<Product> products) {
            if (mProductListFragment == null) {
                mProductListFragment = ProductListFragment.newInstance(products, mProductListLayout);

                FragmentTransaction fragTrans = (getSupportFragmentManager()).beginTransaction();
                fragTrans.replace(getContentPaneResourceId(), mProductListFragment);
                fragTrans.commitNow();
            } else {
                mProductListFragment.updateProductList(products);
            }
        }

        protected void requestProductsViaTime(Long time) {
            ( getController() ).getProductList(null, time, this);
        }

        protected void requestProductsViaCategory(String[] selectionStr) {
            ( getController() ).getProductList(selectionStr, null, this);
        }

        protected void requestProductsViaSearch(String query) {
            ( getController() ).searchProducts(query, this);
        }
    }
}