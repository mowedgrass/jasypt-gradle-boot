package com.github.mowedgrass.jasyptgradleboot.task
import org.gradle.logging.StyledTextOutput
import org.gradle.logging.StyledTextOutputFactory

import static org.gradle.logging.StyledTextOutput.Style.*

class ConsoleOutput {

    protected StyledTextOutput output
    protected StyledTextOutput infoOutput
    protected StyledTextOutput warnOutput
    protected StyledTextOutput errorOutput

    public ConsoleOutput(StyledTextOutputFactory outputFactory) {
        output = outputFactory.create(getClass())
        infoOutput = output.withStyle(Info)
        warnOutput = output.withStyle(UserInput)
        errorOutput = output.withStyle(Failure)
    }

    public plain(Object string) {
        output.println(string)
    }

    public info(Object string) {
        infoOutput.println(string)
    }

    public warn(Object string) {
        warnOutput.println(string)
    }

    public error(Object string) {
        errorOutput.println(string)
    }
}
