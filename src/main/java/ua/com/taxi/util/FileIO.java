package ua.com.taxi.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileIO {

    public Properties readProperties(String fileName) {
        Properties prop = new Properties();
        try (InputStream input = FileIO.class.getClassLoader().getResourceAsStream(fileName)) {

            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop;
    }
}
