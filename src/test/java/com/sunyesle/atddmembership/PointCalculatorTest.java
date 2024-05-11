package com.sunyesle.atddmembership;

import com.sunyesle.atddmembership.service.PercentagePointCalculator;
import com.sunyesle.atddmembership.service.PointCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PointCalculatorTest {

    private PointCalculator percentagePointCalculator;

    @BeforeEach
    void setUp() {
        percentagePointCalculator = new PercentagePointCalculator();
    }

    @ParameterizedTest
    @MethodSource("provideValue")
    void 포인트를_계산한다(int amount, int expectedPoint){
        int point = percentagePointCalculator.calculatePoint(amount);

        assertThat(point).isEqualTo(expectedPoint);
    }

    private static Stream<Arguments> provideValue() {
        return Stream.of(Arguments.of(100, 1)
                , Arguments.of(99, 0)
                , Arguments.of(55555, 555)
        );
    }
}
