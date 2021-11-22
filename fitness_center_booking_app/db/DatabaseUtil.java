package fitness_center_booking_app.db;

import java.util.List;

import fitness_center_booking_app.entity.FitnessClass;

public class DatabaseUtil {
    public static boolean classExist(List<FitnessClass> fitnessClassList, String name) {
        for (FitnessClass fitnessClass : fitnessClassList) {
            if (fitnessClass.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}
