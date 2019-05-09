package pdx.com.nikeelevatorpitch.team;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import pdx.com.nikeelevatorpitch.R;

/**
 * Created by Eli on 7/23/2017.
 */

public class ProductLevelSwitcher {
    private static final String TAG = ProductLevelSwitcher.class.getSimpleName();
    private ProductFragmentListener mListener;
    private ProductFragmentType prevLevel = null;
    private ProductFragmentType nextLevel = null;

    public ProductLevelSwitcher() {
    }

    public void initialize(Context context, View layout, ProductFragmentType currentLevel) {
        if(context instanceof ProductFragmentListener) {
            mListener = (ProductFragmentListener) context;
        } else {
            Log.e(TAG, "Parent activity does not implement ProductFragmentListener");
        }

        switch (currentLevel) {
            case PRODUCT_LVL_1:
                {
                    ImageView dot = layout.findViewById( R.id.dot_lvl_1);
                    dot.setImageDrawable(context.getDrawable(R.drawable.selected_dot));
                    nextLevel = ProductFragmentType.PRODUCT_LVL_2;
                }
                break;
            case PRODUCT_LVL_2:
                {
                    ImageView dot = layout.findViewById(R.id.dot_lvl_2);
                    dot.setImageDrawable(context.getDrawable(R.drawable.selected_dot));

                    prevLevel = ProductFragmentType.PRODUCT_LVL_1;
                    nextLevel = ProductFragmentType.PRODUCT_LVL_3;
                }
                break;
            case PRODUCT_LVL_3:
                {
                    ImageView dot = layout.findViewById(R.id.dot_lvl_3);
                    dot.setImageDrawable(context.getDrawable(R.drawable.selected_dot));

                    prevLevel = ProductFragmentType.PRODUCT_LVL_2;
                }
                break;
        }

        if(nextLevel != null) {
            FloatingActionButton minus = layout.findViewById(R.id.lvl_minus);
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "minus button pressed");
                    if (mListener != null) {
                        mListener.switchProductLevel(nextLevel);
                    } else {
                        Log.e(TAG, "No listener");
                    }
                }
            });
        }

        if(prevLevel != null) {
            FloatingActionButton add = layout.findViewById(R.id.lvl_add);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "add button pressed");
                    if (mListener != null) {
                        mListener.switchProductLevel(prevLevel);
                    } else {
                        Log.e(TAG, "No listener");
                    }
                }
            });
        }

    }

    public void setListener(ProductFragmentListener mListener) {
        this.mListener = mListener;
    }
}
