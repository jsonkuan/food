package iths.com.food.model;

/**
 * Created by Hristijan on 2016-11-14.
 */

public interface IMeal {

    long getId();
    void setId(long id);

    String getName();
    void setName(String name);

    String getCategory();
    void setCategory(String category);

    String getDateTime();
    void setDateTime(String dateTime);

    String getDescription();
    void setDescription(String description);

    int getHealthyScore();
    void setHealthyScore(int healthyScore);

    int getTasteScore();
    void setTasteScore(int tasteScore);

    double getLongitude();
    void setLongitude(double longitude);

    double getLatitude();
    void setLatitude(double latitude);

    String getImagePath();
    void setImagePath(String imagePath);

    double getTotalScore();
}
