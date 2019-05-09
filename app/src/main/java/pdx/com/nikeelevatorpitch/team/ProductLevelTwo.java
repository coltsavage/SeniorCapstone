package pdx.com.nikeelevatorpitch.team;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pdx.com.nikeelevatorpitch.R;

/**
 * Created by Eli on 7/29/2017.
 */

public class ProductLevelTwo extends Fragment{
    private static final String TAG = ProductLevelTwo.class.getSimpleName();
    public static final String ARG_PRODUCT = "mProduct";

    private Product mProduct;

    private ProductLevelSwitcher levelSwitcher;

    public ProductLevelTwo() {
        levelSwitcher = new ProductLevelSwitcher();
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param product Parameter 1.
     * @return A new instance of fragment ProductLevelTwo.
     */
    public static ProductLevelTwo newInstance(Product product) {
        ProductLevelTwo fragment = new ProductLevelTwo();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mProduct = (Product) getArguments().getSerializable(ARG_PRODUCT);
        } else {
            Log.e(TAG, "Missing Arguments");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate( R.layout.fragment_product_level_two, container, false);
        TextView productTitle = layout.findViewById(R.id.product_title);
        TextView productDesc = layout.findViewById(R.id.lvl_2_desc);

        levelSwitcher.initialize(getContext(), layout, ProductFragmentType.PRODUCT_LVL_2);

        productTitle.setText(mProduct.name);
        productDesc.setText(mProduct.L2info);
        productDesc.setMovementMethod(new ScrollingMovementMethod());

        ViewPager viewPager = (ViewPager)layout.findViewById(R.id.lvl_2_image);
        ProductImagePagerAdapter imageAdapter = new ProductImagePagerAdapter(getContext(), mProduct.pictures);
        viewPager.setAdapter(imageAdapter);

        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProductFragmentListener ) {
            levelSwitcher.setListener((ProductFragmentListener) context);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ProductFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        levelSwitcher.setListener(null);
    }
}
