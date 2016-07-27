package me.dannysuen.fivehundredpx.model;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danny on 16-7-26.
 */

@Parcel
public class Category extends Model {

    @ParcelConstructor
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Category name: like Animals, Nature...
     */
    public String name;

    // Selected cover image url
    public String coverImageUrl;

    public static List<Category> getDefaultCategories() {
        List<Category> result = new ArrayList<>();
        Category category;

        category = new Category(0, "Uncategorized");
        category.coverImageUrl = "https://drscdn.500px.org/photo/164917263/q%3D50_w%3D140_h%3D140/b952b24e7cf52284d3ee672928355d8a?v=2";
        result.add(category);

        category = new Category(10, "Abstract");
        category.coverImageUrl = "https://drscdn.500px.org/photo/164894239/q%3D50_w%3D140_h%3D140/e7e4c4a40ce16de8adbf95be1ce251b4?v=3";
        result.add(category);

        category = new Category(11, "Animals");
//        category.coverImageUrl = ""
        result.add(category);
        result.add(new Category(5, "Black and White"));
        result.add(new Category(1, "Celebrities"));
        result.add(new Category(9, "City and Architecture"));
        result.add(new Category(15, "Commercial"));
        result.add(new Category(16, "Concert"));
        result.add(new Category(20, "Family"));
        result.add(new Category(14, "Fashion"));
        result.add(new Category(2, "Film"));
        result.add(new Category(24, "Fine Art"));
        result.add(new Category(23, "Food"));
        result.add(new Category(3, "Journalism"));
        result.add(new Category(8, "Landscapes"));
        result.add(new Category(12, "Macro"));
        return result;
    }

}
