package ru.ezhov.tabledestructor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Уничтожитель таблиц
 * <p>
 *
 * @author ezhov_da
 */
public class TableDestructor {
    private static final Logger LOG = Logger.getLogger(TableDestructor.class.getName());

    public static void main(String[] args) {
        loadLogger();

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-context.xml");
        if (args.length == 0) {
            SwingUtilities.invokeLater(() ->
            {
                FrameExecute frameExecute = (FrameExecute) applicationContext.getBean("basicFrame");
                frameExecute.setVisible(true);
            });
        } else {
            ArgsExecutor frameExecute = (ArgsExecutor) applicationContext.getBean("argsExecutor");
            frameExecute.executeArgs(args);
        }
    }

    private static void loadLogger() {
        try {
            LogManager.getLogManager().readConfiguration(
                    TableDestructor.class.getResourceAsStream("/logger.properties"));
        } catch (IOException | SecurityException ex) {
            Logger.getLogger(TableDestructor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
