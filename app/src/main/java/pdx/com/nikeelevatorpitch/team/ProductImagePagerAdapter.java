package pdx.com.nikeelevatorpitch.team;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import pdx.com.nikeelevatorpitch.ProductActivity;
import pdx.com.nikeelevatorpitch.R;

/**
 * Created by Eli on 7/23/2017.
 */

public class ProductImagePagerAdapter extends PagerAdapter {

    Context context;
    String images[];
    LayoutInflater layoutInflater;

    public ProductImagePagerAdapter(Context context, String images[]) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View productImageItem = layoutInflater.inflate( R.layout.product_image_item, container, false);

        // ImageView productImage = (ImageView) productImageItem.findViewById(R.id.product_image);

        NetworkImageView productImage = (NetworkImageView) productImageItem.findViewById(R.id.product_image);

        ImageLoader imageLoader = ((ProductActivity)context).getImageLoader();
        imageLoader.get(images[position], imageLoader.getImageListener(productImage, 0, 0));
        productImage.setImageUrl(images[position], imageLoader);
        container.addView(productImageItem);

        return productImageItem;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
