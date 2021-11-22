package fitness_center_booking_app.entity;

import java.io.Serializable;
import java.util.List;

public class FitnessClass implements Serializable {
    private String key, name, description;

    public FitnessClass() {

    }

    public FitnessClass(String key, String name, String description) {
        this.key = key;
        this.name = name;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "FitnessClass{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static boolean contains(List<FitnessClass> fitnessClassList, String name) {
        for (FitnessClass fitnessClass : fitnessClassList) {
            if (fitnessClass.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}
