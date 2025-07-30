package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Car;
import static utils.Constants.DATA_FILE;

import java.io.*;
import java.util.List;

public class CarDataService {

    public static ObservableList<Car> loadCars() {
        ObservableList<Car> list = FXCollections.observableArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                String[] info = line.split(",");
                Car car = new Car(
                        Integer.parseInt(info[0]),
                        info[1],
                        info[2],
                        Integer.parseInt(info[3])
                );
                list.add(car);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void saveCars(List<Car> cars) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            writer.write("id,name,make,year\n");
            for (Car car : cars) {
                writer.write(String.format("%d,%s,%s,%d\n", car.getId(), car.getName(), car.getMake(), car.getYear()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
