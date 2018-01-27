package ru.ezhov.tabledestructor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Форма, которая позволяет запускать переименование и удаление через интерфейс
 * <p>
 *
 * @author ezhov_da
 */
public class FrameExecute {
    private static final Logger LOG = Logger.getLogger(FrameExecute.class.getName());
    private JFrame frame;

    private JLabel labelPath;
    private JTextField textFieldFileProperties;
    private JButton buttonCommitConfig;

    private JCheckBox checkBoxTest;
    private JButton buttonRename;
    private JButton buttonDrop;
    private JTextPane textPane;

    private ShredderExecuteble shredderExecuteble;

    public FrameExecute(ShredderExecuteble shredderExecuteble) {
        this.shredderExecuteble = shredderExecuteble;

        textFieldFileProperties = new JTextField();

        checkBoxTest = new JCheckBox("Запустить тестово (показывает таблицы без изменений в БД)");
        checkBoxTest.addActionListener(e ->
        {
            String exec;
            if (checkBoxTest.isSelected()) {
                exec = "false";
            } else {
                exec = "true";
            }
            setExecute(exec);
        });
        buttonRename = new JButton("Переименовать");
        buttonRename.addActionListener(e -> execute(Action.RENAME));
        buttonDrop = new JButton("Удалить");
        buttonDrop.addActionListener(e -> execute(Action.DROP));
        textPane = new JTextPane();
        //
        JPanel panel = new JPanel(new GridBagLayout());
        Insets insets = new Insets(5, 5, 5, 5);
        panel.add(checkBoxTest, new GridBagConstraints(0, 0, 2, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, insets, 0, 0));
        panel.add(buttonRename, new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets, 0, 0));
        panel.add(buttonDrop, new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets, 0, 0));
        panel.add(new JScrollPane(textPane), new GridBagConstraints(0, 2, 2, 1, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0));
        //
        frame = new JFrame();

        labelPath = new JLabel("Укажите абсолютный путь к файлу настроек:");
        JPanel panelPath = new JPanel(new BorderLayout());
        panelPath.add(labelPath, BorderLayout.WEST);
        panelPath.add(textFieldFileProperties, BorderLayout.CENTER);
        buttonCommitConfig = new JButton("Применить настройки");
        buttonCommitConfig.addActionListener((ActionEvent e) ->
        {
            setExecute("false");
        });
        panelPath.add(buttonCommitConfig, BorderLayout.EAST);
        panelPath.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        frame.add(panelPath, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.setTitle("~!Уничтожитель таблиц!~");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int) (dimension.width * 0.7), 400);
        frame.setLocationRelativeTo(null);
    }

    private void setExecute(String execute) {
        try {
            String text = textFieldFileProperties.getText();
            if ("".equals(text) || !new File(text).exists()) {

                JOptionPane.showMessageDialog(frame,
                        "Укажите абсолютный путь к файлу настроек",
                        "Ошибка",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                checkBoxTest.setSelected(!Boolean.valueOf(execute));
                PropertiesHolder.instance(text);
                PropertiesHolder.setProperty("is.execute", execute);
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, null, ex);
            JOptionPane.showMessageDialog(frame,
                    "Не удалось установить свойство выполнения: "
                            + "is.execute",
                    "Ошибка",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    private void execute(Action action) {
        SwingUtilities.invokeLater(() ->
        {
            shredderExecuteble.setAction(action);
            List<Table> list = shredderExecuteble.get();

            String s = list
                    .stream()
                    .map(t -> t.getName() + " -> " + t.getQuery())
                    .collect(Collectors.joining("\n"));
            textPane.setText(s);
            textPane.setCaretPosition(0);
        });
    }
}
