package repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileRepository<T extends Serializable> {
    private String filename;

    public FileRepository(String filename) {
        this.filename = filename;
    }

    public void save(List<T> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
            System.out.println("Data saved to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> load() {
        File file = new File(filename);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
