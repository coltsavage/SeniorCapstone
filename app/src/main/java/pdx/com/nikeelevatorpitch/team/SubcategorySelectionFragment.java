package pdx.com.nikeelevatorpitch.team;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import pdx.com.nikeelevatorpitch.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubcategorySelectionFragment.SubcategorySelectionListener} interface
 * to handle interaction events.
 * Use the {@link SubcategorySelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubcategorySelectionFragment extends ListFragment {

    protected final static String ARG_CATEGORY = "args_category";
    private SubcategorySelectionListener mListener;
    final String TAG = "SubCatSelectFrag";
    private List<Category> mSubcats;


    public SubcategorySelectionFragment() {
        // Required empty public constructor
    }

    public static SubcategorySelectionFragment newInstance(Category category) {
        SubcategorySelectionFragment fragment = new SubcategorySelectionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSubcats = ((Category) getArguments().getSerializable(ARG_CATEGORY)).getSubCategories();
        }
        else {
            // If needed, later deal with null arguments
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mListener.onSubcategorySelection(mSubcats.get(position));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate( R.layout.fragment_subcategory_selection, container, false);
        return rootView;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        SubcategoryListArrayAdapter adapter = new SubcategoryListArrayAdapter(this.getContext(), R.layout.content_subcategory, mSubcats);
        getListView().setAdapter(adapter);
    }

    /*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    } */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SubcategorySelectionListener) {
            mListener = (SubcategorySelectionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SubcategorySelectionListener");
        }
    }


/*    public void setSubcategorySelectionListener(SubcategorySelectionListener listener) {
        mListener = listener;
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface SubcategorySelectionListener {
        void onSubcategorySelection(Category subcategory);
    }

    public class SubcategoryListArrayAdapter extends ArrayAdapter<Category> {
        Context mContext;
        int mLayoutId;
        List<Category> mSubcats;

        public SubcategoryListArrayAdapter(Context context, int resource,  List<Category> subcats) {
            super(context, resource, subcats);
            mContext = context;
            mLayoutId = resource;
            mSubcats = subcats;
        }

        @Override
        public int getCount() {
            if (mSubcats == null) {
                Log.d(TAG, "count is null");
                return 0;
            } else
                return mSubcats.size();
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                view = inflater.inflate(mLayoutId, parent, false);
            }

            TextView tv = view.findViewById(R.id.subcat_text);
            tv.setText( mSubcats.get(position).getName());

            return view;
        }

    }


}