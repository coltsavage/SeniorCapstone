package pdx.com.nikeelevatorpitch.team;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacob on 7/26/17.
 */

public class Category implements Serializable{
    private String name;
    private List<Category> subCategories;
    private static final long serialversionUID= 2257L;

    public Category(){}
    public Category(String name, String[] subcats){
        this.name = name;
        if(subcats != null) {
            subCategories = new ArrayList<>();
            for (int i = 0; i < subcats.length; i++)
                subCategories.add(new Category(subcats[i], null));
        }

    }
    public String getName(){return name;}
    public List<Category> getSubCategories(){return subCategories;}

    public Boolean hasSubCategories() {
        return (subCategories != null) && ( !subCategories.isEmpty() );
    }
}
