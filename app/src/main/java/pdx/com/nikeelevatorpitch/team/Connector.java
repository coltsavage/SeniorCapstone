package pdx.com.nikeelevatorpitch.team;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
import java.lang.StringBuilder;


import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import pdx.com.nikeelevatorpitch.ProductListCallback;


/**
 * Created by Jacob on 7/23/17.
 */



public class Connector implements BackendControl {
    private static Connector mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;


    private Connector(Context context) {
        mCtx = context;

        mRequestQueue = getRequestQueue();
        //mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static Connector newInstance(Context context){
        mInstance = new Connector(context);
        return mInstance;
    }
    public static synchronized Connector getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Connector(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            Cache cache = new DiskBasedCache(mCtx.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            // Don't forget to start the volley request queue
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }



    public void getCategories(CategoriesCallback cb){
        String url = "https://paigesystems.com/categories/";

        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new CatRequestListener(cb),

                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error.Response", error.toString());

                            }
                        });

        // Add to queue
        this.addToRequestQueue(jsArrayRequest);
    }

    //REquest product list for standardized API
    //TODO: turn on proper select
    public void getProductList(String[] cats, Long afterTs, ProductListCallback cb) {
       // String url = "https://paigesystems.com/products/";
        String url;

       if(cats != null && (afterTs == null || afterTs == 0)){
            Log.d("cats", "about to set url");
            String base = "https://paigesystems.com/categories";
            StringBuilder finalUrl = new StringBuilder(base);
            for(int i=0; i<cats.length; i++){
                 if(!"all".equals(cats[i])){
                     finalUrl.append("/");
                     finalUrl.append(cats[i]);

                 }
            }

            url = finalUrl.toString();
            Log.d("URL", url);
        }
        else if(cats == null && afterTs != null && afterTs > 0){
            Log.d("newsfeed", "about to set url");
            url = "https://paigesystems.com/products?afterTs="+afterTs;
        }
        else{
            Log.d("badArgs", "about to set url");
            url = "https://paigesystems.com/products/";
        }


        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new RequestListener(cb),

                 new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());

                    }
                });

        // Add to queue
        this.addToRequestQueue(jsArrayRequest);
    }

    //todo: turn on proper select
    public void searchProducts(String search, ProductListCallback cb) {
        String base = "https://paigesystems.com/products?keyword=";

        String[] terms = search.split(" ");
        StringBuilder finalUrl = new StringBuilder(base);
        finalUrl.append(terms[0]);
        for(int i=1; i<terms.length; i++){
            finalUrl.append("%20");
            finalUrl.append(terms[i]);
        }
        String url = finalUrl.toString();

        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new RequestListener(cb),

                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error.Response", error.toString());

                            }
                        });

        // Add to queue
        this.addToRequestQueue(jsArrayRequest);
    }

    private class CatRequestListener implements Response.Listener<JSONArray> {
        private CategoriesCallback cb;

        public CatRequestListener(CategoriesCallback cb) {
            this.cb = cb;
        }

        //todo: turn on proper select
        @Override
        public void onResponse(JSONArray response1) {
            try {
                Log.d("category", String.format("about to parse, size: %d", response1.length()));
                ArrayList<Category> catList = new ArrayList<>();
                //parse jason
                for (int i = 0; i < response1.length(); i++) {
                    JSONObject response = response1.getJSONObject(i);
                    String name = response.getString("category");
                    //String name = response1.getString(i); ///remove
                    //String[] subCategories = {"all","test1","test2","test3"}; //remove
                    JSONArray subCats = response.getJSONArray("subcategories");
                    String[] subCategories = new String[subCats.length() +1];
                    subCategories[0] = "all";
                    for (int j = 0; j < subCats.length(); j++)
                        subCategories[j+1] = subCats.getString(j);
                    Category toAdd = new Category(name, subCategories);
                    catList.add(toAdd);
                }
                this.cb.onResult(catList);
            } catch (Exception e) {
                Log.d("error", e.toString());
            }
        }
    }

    private class RequestListener implements Response.Listener<JSONArray> {
        private ProductListCallback cb;

        public RequestListener(ProductListCallback cb) {
            this.cb = cb;
        }

        @Override
        public void onResponse(JSONArray response1) {
            try {
                Log.d("prods", String.format("about to parse, size: %d", response1.length()));
                ArrayList<Product> prodList = new ArrayList<>();
                //parse jason
                for (int i = 0; i < response1.length(); i++) {
                    JSONObject response = response1.getJSONObject(i);
                    String name = response.getString("prodName");
                    Log.d("Connector", String.format("PRODUCT: %s - %s", name, response.toString()));
                    int date = response.getInt("releaseDate");
                    int price = response.getInt("price");
                    JSONArray URLS = response.getJSONArray("imageURLs");
                    JSONArray L1 = response.getJSONArray("L1info");
                    String L2info = response.getString("L2info");
                    String L3info = response.getString("L3info");
                    //JSONArray Cats = response.getJSONArray("Categories");
                    String[] urls = new String[URLS.length()];
                    for (int j = 0; j < URLS.length(); j++)
                        urls[j] = URLS.getString(j);
                    String[] L1info = new String[L1.length()];
                    for (int j = 0; j < L1.length(); j++)
                        L1info[j] = L1.getString(j);
                    String[] categories = {"test", "test2"};
                    //String[] categories = new String[Cats.length()];
                    //for (int j = 0; j < Cats.length(); j++)
                        //categories[j] = Cats.getString(j);
                    String icon = response.getString("iconURL");
                    //String icon = "http://http://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/sign-check-icon.png";

                    Product toAdd = new Product(i, date, price, name, categories, L1info,
                            L2info, L3info, urls, icon);
                    prodList.add(toAdd);
                }
                this.cb.onResult(prodList);
            } catch (Exception e) {
                Log.d("error", e.toString());
            }
        }
    }
}


