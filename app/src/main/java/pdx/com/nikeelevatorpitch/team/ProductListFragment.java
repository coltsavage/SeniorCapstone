package pdx.com.nikeelevatorpitch.team;



import android.app.Activity;
import android.content.Context;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pdx.com.nikeelevatorpitch.ProductListLogisticsActivity;
import pdx.com.nikeelevatorpitch.R;


//Created by David: 22 Jul 2017


public class ProductListFragment extends Fragment {

    protected final static String EXTRA_PRODUCT_ID = "extra_product_id";
    protected final static String EXTRA_PRODUCT = "extra_product";
    protected final static String EXTRA_VIEW_MODE = "view_mode";
    public final static int ROW = 1;
    public final static int GRID = 2;
    final String TAG = "pListFragment";
    ProductSelectionListener mListener;
    int mViewMode;
    List<Product> mProducts;
    ListView mListView;
    GridView mGridView;
    ProductListArrayAdapter mAdapter;


    public ProductListFragment() {
        // Required empty public constructor
    }

    public static ProductListFragment newInstance(List<Product> productList, int viewmode) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_PRODUCT, (Serializable)productList);
        args.putInt(EXTRA_VIEW_MODE, viewmode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mViewMode = getArguments().getInt(EXTRA_VIEW_MODE);
            mProducts = (List<Product>)(getArguments().getSerializable(EXTRA_PRODUCT));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        if (mViewMode == ROW) {
            view= inflater.inflate( R.layout.fragment_product_listing_row, container, false);
            mListView = view.findViewById(R.id.list_view);
        }
        else {
            view= inflater.inflate(R.layout.fragment_product_listing_grid, container, false);
            mGridView = view.findViewById(R.id.grid_view);
        }
        updateProductList(mProducts);
        return view;
    }

    public void onButtonPressed(Product product) {
        if (mListener != null) {
            mListener.onProductSelection(product);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProductSelectionListener) {
            mListener = (ProductSelectionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ProductSelectionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceStates) {
        super.onActivityCreated(savedInstanceStates);


    }

    /* Replaces the current display of products with those specified in
     * the passed list.
     */


    public void updateProductList(List<Product> productList) {
        ProductListArrayAdapter adapter;
        mProducts = productList;
        if (mViewMode == ROW) {
            adapter = new ProductListArrayAdapter(this.getContext(), R.layout.content_product_entry_row, mProducts);
            adapter.addProducts(productList);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(mOnClickListener);
        }else {
            adapter = new ProductListArrayAdapter(this.getContext(), R.layout.content_product_entry_grid, mProducts);
            adapter.addProducts(productList);
            mGridView.setAdapter(adapter);
            mGridView.setOnItemClickListener(mOnClickListener);
        }


    }

/*    public void setProductSelectionListener(ProductSelectionListener listener) {
        this.mListener = listener;
    }*/


/*
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Product selected = mProducts.get(position);
        mListener.onProductSelection(selected);
    }*/
// Create a message handling object as an anonymous class.

    private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
    public void onItemClick(AdapterView parent, View v, int position, long id) {
        mListener.onProductSelection(mProducts.get(position));
    }
    };


    public interface ProductSelectionListener {
        public void onProductSelection(Product product);
    }

    public class ProductListArrayAdapter extends ArrayAdapter<Product> {
        Context mContext;
        int mLayoutId;
        List<Product> mProducts;

        public ProductListArrayAdapter(Context context, int resource, List<Product> products) {
            super(context, resource, products);
            mContext = context;
            mLayoutId = resource;
            mProducts = products;
        }

        @Override
        public int getCount() {
            if (mProducts == null) {
                Log.d(TAG, "count is null");
                return 0;
            } else
                return mProducts.size();
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                view = inflater.inflate(mLayoutId, parent, false);
            }

            Product product = mProducts.get(position);
            NetworkImageView productImage = (NetworkImageView) view.findViewById(R.id.icon);

            ImageLoader imageLoader = ((ProductListLogisticsActivity)getActivity()).getImageLoader();
            imageLoader.get(product.iconURL, imageLoader.getImageListener(productImage, 0, 0));
            productImage.setImageUrl(product.iconURL, imageLoader);


            TextView nameView = view.findViewById(R.id.name_view);
            nameView.setText(product.name);

            //No date view in newsfeed/grid mode
            if(mViewMode == ROW) {
                TextView dateView = view.findViewById(R.id.date_view);
                Date date = new Date(((long)product.release)*1000);
                SimpleDateFormat format = new SimpleDateFormat("MMM YYYY");
                String formatted = format.format(date);
                dateView.setText(formatted);
            }

            TextView priceView = view.findViewById(R.id.price_view);
            priceView.setText("$"+product.price);
            return view;
        }

        public void addProducts(List<Product> products) {
            mProducts = products;
            notifyDataSetChanged();
        }
    }
}
