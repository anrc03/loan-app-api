package com.enigma.loan_app.constant;

public enum EInstallmentType {
    ONE_MONTH(1),
    THREE_MONTHS(3),
    SIXTH_MONTHS(6),
    NINE_MONTHS(9),
    TWELVE_MONTHS(12);

    public final Integer intValue;

    EInstallmentType(Integer intValue) {
        this.intValue = intValue;
    }
}
