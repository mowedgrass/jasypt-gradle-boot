package com.github.mowedgrass.jasyptgradleboot.task.file.property.pattern

class ProcessPatternMatcher {

    public Optional<ProcessPatternMatches> getMatches(String line, String pattern) {
        def matches = Optional.empty()
        line.find(pattern) {
            matches = Optional.of(extract(it as List<String>))
        }

        matches
    }

    private ProcessPatternMatches extract(List<String> matches) {
        def (String line, String preceding, String value, String succeeding) = matches

        new ProcessPatternMatches(preceding, value, succeeding)
    }

}
