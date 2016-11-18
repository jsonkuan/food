package iths.com.food.Model;

/**
 * Created by jas0n on 2016-11-14.
 */

//TODO: Model class for a meal

public class Meal {
    private long id;
    private String name;
    private String category;
    private String dateTime;
    private String description;
    private int healthyScore;
    private int tasteScore;
    private double longitude;
    private double latitude;
    private String imagePath;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHealthyScore() {
        return healthyScore;
    }

    public void setHealthyScore(int healthyScore) {
        this.healthyScore = healthyScore;
    }

    public int getTasteScore() {
        return tasteScore;
    }

    public void setTasteScore(int tasteScore) {
        this.tasteScore = tasteScore;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getTotalScore() {
        return (tasteScore + healthyScore) / 2.0;
    }
}
