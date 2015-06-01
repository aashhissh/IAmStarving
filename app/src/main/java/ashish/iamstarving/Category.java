package ashish.iamstarving;

import com.google.gson.annotations.Expose;

/**
 * Created by ashish on 29-04-2015.
 */
public class Category {
    @Expose
    private String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
