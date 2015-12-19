package com.github.mowedgrass.jasyptgradleboot.task.file.property.pattern

import com.github.mowedgrass.jasyptgradleboot.task.file.property.pattern.ProcessPatternMatcher
import spock.lang.Specification

class ProcessPatternMatcherSpec extends Specification {

    ProcessPatternMatcher matcher

    public static final String SAMPLE_PATTERN = /^(.*[:=]\s?)PLAIN\((.*)\)(\s?)$/

    def setup() {
        matcher = new ProcessPatternMatcher()
    }

    def 'should get matches'() {
        when:
        def matches = matcher.getMatches(line, SAMPLE_PATTERN)

        then:
        match == matches.isPresent()
        matches.ifPresent {
            with(matches.get()) {
                expectedPreceding == preceding
                expectedValue == value
                expectedSuceeding == succeeding
            }
        }

        where:
        line                  | match | expectedPreceding | expectedValue | expectedSuceeding
        'data: PLAIN(text)'   | true  | 'data: '          | 'text'        | ''
        'test = PLAIN(text) ' | true  | 'test = '         | 'text'        | ' '
        ''                    | false | ''                | ''            | ''
        'data: UNKNOWN(text)' | false | ''                | ''            | ''
    }
}
