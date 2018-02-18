package org.maksim.training.mtapp.service.util;

import java.math.BigDecimal;

public final class PriceUtils {
    private PriceUtils() { }

    public static BigDecimal multiply(BigDecimal price, double multiplier) {
        return price.multiply(new BigDecimal(multiplier)).setScale(1, BigDecimal.ROUND_HALF_UP);
    }
}