package com.github.mowedgrass.jasyptgradleboot.task.file.property

import com.github.mowedgrass.jasyptgradleboot.task.file.property.PropertyLineFormatter
import com.github.mowedgrass.jasyptgradleboot.task.file.property.pattern.ProcessPatternMatches
import spock.lang.Specification

class PropertyLineFormatterSpec extends Specification {

    PropertyLineFormatter propertyLineFormatter

    def setup() {
        propertyLineFormatter = new PropertyLineFormatter()
    }

    def 'should format property line'() {
        given:
        def preceding = 'data: '
        def initialValue = 'plain'
        def processedValue = 'encrypted'
        def succeeding = ''

        and:
        def matches = new ProcessPatternMatches(preceding, initialValue, succeeding)

        when:
        def formatted = propertyLineFormatter.format(matches, processedValue, 'ENC(%s)')

        then:
        preceding + 'ENC(' + processedValue + ')' == formatted
    }
}
