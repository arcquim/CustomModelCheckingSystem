package com.arcquim.custombdd;

import java.math.BigInteger;
import net.sf.javabdd.BDDDomain;
import net.sf.javabdd.BDDFactory;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class CustomBDDDomain extends BDDDomain {
    
    private BDDFactory factory;
    
    public CustomBDDDomain(int index, BigInteger bi) {
        super(index, bi);
    }
    
    public CustomBDDDomain setBDDFactory(BDDFactory factory) {
        this.factory = factory;
        return this;
    }

    @Override
    public BDDFactory getFactory() {
        return factory;
    }
}
