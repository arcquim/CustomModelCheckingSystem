/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arcquim.custombdd;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import net.sf.javabdd.BDD;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class CustomFactoryTest {
    
    @Test
    public void testZero() {
        CustomFactory customFactory = new CustomFactory();
        customFactory.setVarNum(7);
        CustomBDD bdd = (CustomBDD) customFactory.zero();
        String actualResult = bdd.toString();
        String expectedResult = "";
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testOne() {
        CustomFactory customFactory = new CustomFactory();
        customFactory.setVarNum(7);
        CustomBDD bdd = (CustomBDD) customFactory.one();
        String actualResult = bdd.toString();
        String expectedResult = "<>";
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsInitialized() {
        CustomFactory customFactory = new CustomFactory();
        assertFalse(customFactory.isInitialized());
        customFactory.setVarNum(2);
        assertTrue(customFactory.isInitialized());
    }

    @Test
    public void testVarNum() {
        CustomFactory customFactory = new CustomFactory();
        customFactory.setVarNum(7);
        assertEquals(customFactory.varNum(), 7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVarNum() {
        CustomFactory customFactory = new CustomFactory();
        customFactory.setVarNum(7);
        assertEquals(customFactory.varNum(), 7);
        assertEquals(customFactory.nithVar(3), customFactory.nithVar(3));
        customFactory.ithVar(7);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetVarNumTwice() {
        CustomFactory customFactory = new CustomFactory();
        customFactory.setVarNum(7);
        assertEquals(customFactory.varNum(), 7);
        assertEquals(customFactory.nithVar(3), customFactory.nithVar(3));
        customFactory.setVarNum(3);
    }

    @Test
    public void testIthVar() {
        CustomFactory customFactory = new CustomFactory();
        customFactory.setVarNum(7);
        BDD bdd = customFactory.ithVar(2);
        String actualResult = bdd.toString();
        String expectedResult = "<2:1>";
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testNithVar() {
        CustomFactory customFactory = new CustomFactory();
        customFactory.setVarNum(7);
        BDD bdd = customFactory.nithVar(2);
        String actualResult = bdd.toString();
        String expectedResult = "<2:0>";
        assertEquals(expectedResult, actualResult);
    }
    
}
