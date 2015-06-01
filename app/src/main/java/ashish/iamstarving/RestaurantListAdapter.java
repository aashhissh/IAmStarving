package ashish.iamstarving;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by ashish on 29-04-2015.
 */
public class RestaurantListAdapter extends ArrayAdapter<RestaurantData> {

    private final Context context;
    private final ArrayList<RestaurantData> restaurantDatas;
    ImageView ivBrandLogo;
    ImageButton ibtFavourite;
    private TextView tvBrandName, tvOffers, tvCategories, tvDistance, tvLocationName;

    public RestaurantListAdapter(Context context, ArrayList<RestaurantData> items) {
        super(context, R.layout.list_element_design, items);
        this.context = context;
        this.restaurantDatas = items;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = layoutInflater.inflate(R.layout.list_element_design, null);
        init(view);
        final RestaurantData restaurantData = restaurantDatas.get(position);
        tvBrandName.setText(restaurantData.getOutletName());
        tvOffers.setText(String.valueOf(restaurantData.getNumCoupons()));
        tvDistance.setText(fixDistance(restaurantData.getDistance()));
        tvLocationName.setText(restaurantData.getNeighbourhoodName());
        String cat = "";
        for (Category category : restaurantData.getCategories()) {
            cat += " \u2022 " + category.getName();
        }
        tvCategories.setText(cat);
        view.findViewById(R.id.ibtFavourite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (restaurantData.isFavourite()) {
                    restaurantData.setFavourite(false);
                    ((ImageButton)view.findViewById(R.id.ibtFavourite)).setImageResource(R.drawable.icon_unfavourite);
                } else {
                    restaurantData.setFavourite(true);
                    ((ImageButton)view.findViewById(R.id.ibtFavourite)).setImageResource(R.drawable.icon_favourite);
                }
            }
        });

        Picasso.with(context)
                .load(restaurantData.getLogoURL())
                .into(ivBrandLogo);

        return view;
    }

    public void init(View view) {
        tvBrandName = (TextView) view.findViewById(R.id.tvBrandName);
        tvOffers = (TextView) view.findViewById(R.id.tvOffers);
        tvCategories = (TextView) view.findViewById(R.id.tvCategories);
        tvDistance = (TextView) view.findViewById(R.id.tvDistance);
        tvLocationName = (TextView) view.findViewById(R.id.tvLocationName);
        ivBrandLogo = (ImageView) view.findViewById(R.id.ivBrandLogo);
        ibtFavourite = (ImageButton) view.findViewById(R.id.ibtFavourite);
    }

    private String fixDistance(float distance) {
        String Distance = "";
        if (distance > 1000) {
            DecimalFormat form = new DecimalFormat("0.0");
            Distance = " " + form.format(distance / 1000) + " Km ";
        } else {
            Distance = " " + distance + " m ";
        }
        return Distance;
    }
}
