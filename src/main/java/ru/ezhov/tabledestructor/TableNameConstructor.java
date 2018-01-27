package ru.ezhov.tabledestructor;

import java.util.Random;
import java.util.logging.Logger;

/**
 * Переделываем название таблиц в переименованные
 * <p>
 *
 * @author ezhov_da
 */
public class TableNameConstructor {
    private static final Logger LOG = Logger.getLogger(TableNameConstructor.class.getName());

    private static final int NAME_FOR_ADD_PREFIX = 5; //fdel_
    private static final int MAX_NAME_TABLE = 128;
    private static final int SUM_MAX_LENGTH = NAME_FOR_ADD_PREFIX + MAX_NAME_TABLE;

    public String getNewNameTable(String oldNameTable) {
        String random = getRandomHex();
        String newName = random + oldNameTable;

        if (newName.length() + NAME_FOR_ADD_PREFIX > SUM_MAX_LENGTH) {
            newName = newName.substring(0, MAX_NAME_TABLE - NAME_FOR_ADD_PREFIX);
        }

        return newName;
    }


    private String getRandomHex() {
        Random random = new Random();
        int i = random.nextInt(100000);
        String s = Integer.toHexString(i);
        return s;
    }

}
