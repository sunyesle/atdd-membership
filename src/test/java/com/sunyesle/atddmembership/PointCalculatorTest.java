package com.sunyesle.atddmembership;

import com.sunyesle.atddmembership.service.PercentagePointCalculator;
import com.sunyesle.atddmembership.service.PointCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PointCalculatorTest {

    private PointCalculator percentagePointCalculator;

    @BeforeEach
    void setUp() {
        percentagePointCalculator = new PercentagePointCalculator();
    }

    @Test
    void 포인트를_계산한다(){
        int point = percentagePointCalculator.calculatePoint(100);

        assertThat(point).isEqualTo(1);
    }
}
