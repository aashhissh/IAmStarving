package ashish.iamstarving;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashish on 29-04-2015.
 */
public class RestaurantData {
    @Expose
    private String OutletName;
    @Expose
    private int NumCoupons;
    @Expose
    private double Latitude;
    @Expose
    private double Longitude;
    @Expose
    private String LogoURL;
    @Expose
    private String NeighbourhoodName;
    @Expose
    private List<Category> Categories = new ArrayList<Category>();

    private Long distance = 0L;
    private boolean favourite = false;

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public List<Category> getCategories() {
        return Categories;
    }

    public void setCategories(List<Category> categories) {
        Categories = categories;
    }

    public String getOutletName() {
        return OutletName;
    }

    public void setOutletName(String outletName) {
        OutletName = outletName;
    }

    public int getNumCoupons() {
        return NumCoupons;
    }

    public void setNumCoupons(int numCoupons) {
        NumCoupons = numCoupons;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getLogoURL() {
        return LogoURL;
    }

    public void setLogoURL(String logoURL) {
        LogoURL = logoURL;
    }

    public String getNeighbourhoodName() {
        return NeighbourhoodName;
    }

    public void setNeighbourhoodName(String neighbourhoodName) {
        NeighbourhoodName = neighbourhoodName;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }
}
