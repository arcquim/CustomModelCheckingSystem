package com.system;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public enum VerificationResult {
    
    EMPTY_MODEL_OR_PROPERTY, 
    PROPERTY_HOLDS, 
    PROPERTY_NOT_HOLDS;
    
    public static final String WRONG_INPUT = "Error: input is not correct";
    public static final String ERROR_DURING_VERIFICATION = "Error during the verification";
    public static final String PROPERTY_HOLDS_ON_MODEL = "Property holds";
    public static final String PROPERTY_DOES_NOT_HOLD_ON_MODEL = "Property does not hold";
    
    private static final Map<VerificationResult, String> resultMapping;
    
    static {
        resultMapping = new HashMap<>();
        resultMapping.put(EMPTY_MODEL_OR_PROPERTY, WRONG_INPUT);
        resultMapping.put(PROPERTY_HOLDS, PROPERTY_HOLDS_ON_MODEL);
        resultMapping.put(PROPERTY_NOT_HOLDS, PROPERTY_DOES_NOT_HOLD_ON_MODEL);
    }
    
    public String toDatabaseString() {
        String databaseString = resultMapping.get(this);
        return databaseString == null ? ERROR_DURING_VERIFICATION : databaseString;
    }
    
}
