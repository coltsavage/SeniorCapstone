package pdx.com.nikeelevatorpitch.team;

import android.graphics.Bitmap;

import java.io.Serializable;
// Product class
// Written by Timothy

public class Product implements Serializable {
    // BEGIN header
    int id_num;
    int release;
    int price;
    String iconURL;
    Bitmap icon;
    String name;
    String[] categories;
    private static final long serialversionUID= 157L;
    // END header

    // BEGIN content
    String[] L1info;
    String L2info;
    String L3info;
    String[] pictures;
    Bitmap[] pics;
    // END content

    public Product(){
        this.id_num = 0;
        this.release =0;
        this.price = 0;
        this.name = null;
        this.categories = null;
        this.L1info = null;
        this.L2info = null;
        this.L3info = null;
        this.pictures = null;
        this.icon = null;
        this.pics = null;
        this.iconURL = null;
    }

    public Product(int id, int release, int price, String name, String[] categories, String[] L1,
                   String L2, String L3, String[] pics, String iconURL){
        this.id_num = id;
        this.release =release;
        this.price = price;
        this.name = name;
        this.categories = new String[categories.length];
        for(int i=0; i<categories.length; i++)
            this.categories[i] = categories[i];
        this.L1info = new String[L1.length];
        for(int i = 0; i<L1.length; i++)
            this.L1info[i] = L1[i];
        this.L2info = L2;
        this.L3info = L3;
        this.pictures = new String[pics.length];
        for(int i=0; i<pics.length; i++)
            this.pictures[i] = pics[i];
        this.iconURL = iconURL;
    }


    public void display() {}

}
