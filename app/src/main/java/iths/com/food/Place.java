package iths.com.food;

/**
 * Created by jas0n on 2016-11-29.
 */
public enum Place {
    GOTHENBURG (57.7, 11.97),
    MALMO (55.60, 13),
    STOCKHOLM (59.32, 18.07),
    SKOOL (60, 20);

    private final double lat;
    private final double lng;

    Place(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

}
