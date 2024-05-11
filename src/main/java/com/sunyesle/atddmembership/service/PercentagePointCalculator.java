package com.sunyesle.atddmembership.service;

import org.springframework.stereotype.Component;

@Component
public class PercentagePointCalculator implements PointCalculator {
    private static final int PERCENTAGE = 1;

    @Override
    public int calculatePoint(Integer amount) {
        return amount * PERCENTAGE / 100;
    }
}