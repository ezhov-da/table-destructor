package ru.ezhov.tabledestructor;

import org.apache.commons.cli.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ezhov_da
 */
public class ArgsExecutor {
    private static final Logger LOG = Logger.getLogger(ArgsExecutor.class.getName());

    private ShredderExecutor shredderExecutor;

    public ArgsExecutor(ShredderExecutor shredderExecutor) {
        this.shredderExecutor = shredderExecutor;
    }

    private static Options getOptions() {
        Options options = new Options();
        Option option = Option.builder("f")
                .required(true)
                .longOpt("file")
                .hasArg(true)
                .build();

        options.addOption(option);

        OptionGroup optionGroup = new OptionGroup();

        option = Option.builder("r")
                .required(true)
                .longOpt("rename")
                .build();

        optionGroup.addOption(option);

        option = Option.builder("d")
                .required(true)
                .longOpt("drop")
                .build();

        optionGroup.addOption(option);

        options.addOptionGroup(optionGroup);

        return options;
    }

    public void executeArgs(String[] args) {
        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine line = parser.parse(getOptions(), args);
            String opValFile = line.getOptionValue("f");

            LOG.log(Level.INFO, "file properties: {0}", opValFile);
            PropertiesHolder.instance(opValFile);

            boolean r = line.hasOption("r");

            Action action = r ? Action.RENAME : Action.DROP;

            shredderExecutor.setAction(action);
            List<Table> list = shredderExecutor.get();

            list.forEach(t -> LOG.info(t.getName()));
        } catch (ParseException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
}
