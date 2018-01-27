package ru.ezhov.tabledestructor;

/**
 * @author ezhov_da
 */
public enum Action {
    DROP("-d"), RENAME("-r");

    private String act;

    private Action(String act) {
        this.act = act;
    }

    public static Action valOf(String val) {
        if (val.equals(DROP.getAct())) {
            return DROP;
        } else if (val.equals(RENAME.getAct())) {
            return RENAME;
        } else {
            return null;
        }
    }

    public boolean isEquals(String act) {
        return this.act.equals(act);
    }

    public String getAct() {
        return act;
    }
}
