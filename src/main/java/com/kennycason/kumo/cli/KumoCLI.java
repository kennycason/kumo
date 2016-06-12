package com.kennycason.kumo.cli;

import com.beust.jcommander.Parameter;

/**
 * Created by kenny on 6/11/16.
 *
 * Refer to CLI.md for usage.
 *
 */
public class KumoCLI {
    @Parameter(names = { "-log", "-verbose" }, description = "Level of verbosity")
    private Integer verbose = 1;

}
