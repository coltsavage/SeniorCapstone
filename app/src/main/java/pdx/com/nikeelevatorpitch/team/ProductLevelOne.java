package pdx.com.nikeelevatorpitch.team;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import pdx.com.nikeelevatorpitch.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductFragmentListener} interface
 * to handle interaction events.
 * Use the {@link ProductLevelOne#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductLevelOne extends Fragment {
    private static final String TAG = ProductLevelOne.class.getSimpleName();
    public static final String ARG_PRODUCT = "mProduct";

    private Product mProduct;

    private ProductLevelSwitcher levelSwitcher;

    public ProductLevelOne() {
        levelSwitcher = new ProductLevelSwitcher();
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param product Parameter 1.
     * @return A new instance of fragment ProductLevelOne.
     */
    public static ProductLevelOne newInstance(Product product) {
        ProductLevelOne fragment = new ProductLevelOne();
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
        View layout = inflater.inflate( R.layout.fragment_product_level_one, container, false);
        TextView productTitle = layout.findViewById(R.id.product_title);
        ListView descriptionList = layout.findViewById(R.id.lvl_1_desc);

        levelSwitcher.initialize(getContext(), layout, ProductFragmentType.PRODUCT_LVL_1);

        productTitle.setText(mProduct.name);
        ProductDescriptionArrayAdapter adapter = new ProductDescriptionArrayAdapter(this.getContext(), R.layout.content_product_desc_item, mProduct.L1info);
        descriptionList.setAdapter(adapter);

        ViewPager viewPager = (ViewPager)layout.findViewById(R.id.lvl_1_image);
        ProductImagePagerAdapter imageAdapter = new ProductImagePagerAdapter(getContext(), mProduct.pictures);
        viewPager.setAdapter(imageAdapter);

        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProductFragmentListener) {
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


    private class ProductDescriptionArrayAdapter extends ArrayAdapter<String> {
        Context mContext;
        int mLayoutId;
        String[] mData;

        public ProductDescriptionArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull String[] objects) {
            super(context, resource, objects);
            mContext = context;
            mLayoutId = resource;
            mData = objects;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                view = inflater.inflate(mLayoutId, parent, false);
            }

            String dataItem = mData[position];
            TextView textViewItem = view.findViewById(R.id.desc);
            textViewItem.setText(dataItem);

            return view;
        }
    }
}
