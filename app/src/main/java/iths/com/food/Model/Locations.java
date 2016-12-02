package iths.com.food.Model;

import iths.com.food.Place;

/**
 * Created by jas0n on 2016-11-29.
 */
public class Locations {

    private Place place;

    public Locations(Place place) {
        this.place = place;
    }

    public double getLat() {
        return place.getLat();
    }

    public double getLng() {
        return place.getLng();
    }

    public String getTitle() {
        return place.name();
    }
}
