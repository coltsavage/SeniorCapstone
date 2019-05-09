package pdx.com.nikeelevatorpitch;

import java.util.List;

import pdx.com.nikeelevatorpitch.team.Product;

/**
 * Created by Colt on 7/22/2017.
 */

public interface ProductListCallback {

    public void onResult(List<Product> products);
}
