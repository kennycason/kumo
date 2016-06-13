package com.kennycason.kumo.cli;

import com.beust.jcommander.JCommander;

/**
 * Created by kenny on 6/12/16.
 */
public class KumoCli {

    private final CliParameters cliParameters = new CliParameters();

    public static void main(final String[] args) {
        new KumoCli().runWithArguments(args);
    }

    public void runWithArguments(final String[] args) {
        new JCommander(cliParameters).parse(args);
    }
    
}
