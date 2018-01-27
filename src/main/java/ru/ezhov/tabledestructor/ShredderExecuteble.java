package ru.ezhov.tabledestructor;

import java.util.List;
import java.util.function.Supplier;


/**
 * @author ezhov_da
 */
public interface ShredderExecuteble extends Supplier<List<Table>> {
    void setAction(Action action);
}
