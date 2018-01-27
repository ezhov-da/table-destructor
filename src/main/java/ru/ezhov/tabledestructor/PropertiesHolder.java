package ru.ezhov.tabledestructor;

import java.io.FileReader;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Чтение настроек
 * <p>
 *
 * @author ezhov_da
 */
public class PropertiesHolder {
    private static final Logger LOG = Logger.getLogger(PropertiesHolder.class.getName());
    private static PropertiesHolder propertiesHolder;
    private static String pathToFile;
    private Properties propertie;

    private PropertiesHolder() {
        propertie = new Properties();
    }

    public static void instance(String pathToFile) {
        PropertiesHolder.pathToFile = pathToFile;
    }

    public static String getProperty(String property) throws Exception {
        if (Objects.isNull(propertiesHolder)) {
            propertiesHolder = new PropertiesHolder();
            propertiesHolder.propertie.load(new FileReader(pathToFile));
        }
        return propertiesHolder.propertie.getProperty(property);
    }

    public static void setProperty(String property, String val) throws Exception {
        if (Objects.isNull(propertiesHolder)) {
            propertiesHolder = new PropertiesHolder();
            propertiesHolder.propertie.load(new FileReader(pathToFile));
        }
        propertiesHolder.propertie.setProperty(property, val);
    }
}
