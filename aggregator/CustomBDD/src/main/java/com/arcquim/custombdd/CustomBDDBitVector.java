package com.arcquim.custombdd;

import net.sf.javabdd.BDDBitVector;
import net.sf.javabdd.BDDFactory;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class CustomBDDBitVector extends BDDBitVector {
    
    private BDDFactory factory;
    
    public CustomBDDBitVector(int i) {
        super(i);
    }
    
    public CustomBDDBitVector setFactory(BDDFactory factory) {
        this.factory = factory;
        return this;
    }

    @Override
    public BDDFactory getFactory() {
        return factory;
    }
    
}
