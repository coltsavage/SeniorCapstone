package pdx.com.nikeelevatorpitch.team;

import com.android.volley.toolbox.ImageLoader;

import pdx.com.nikeelevatorpitch.ProductListCallback;

/**
 * Provides a single point of communication to the Backend.
 */

public interface BackendControl {

    public void getCategories(CategoriesCallback callback);

    public void getProductList(String[] categories, Long afterTs, ProductListCallback callback);

    public void searchProducts(String search, ProductListCallback callback);

    public ImageLoader getImageLoader();
}