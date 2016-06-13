package com.arcquim.custombdd;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class CustomBDDTest {

    @Test
    public void testIsZero() {
        CustomFactory factory = new CustomFactory();
        factory.setVarNum(4);
        BDD zeroBDD = factory.zero();
        assertTrue(zeroBDD.isZero());
    }

    @Test
    public void testIsOne() {
        CustomFactory factory = new CustomFactory();
        factory.setVarNum(4);
        BDD oneBdd = factory.one();
        assertTrue(oneBdd.isOne());
    }

    @Test
    public void testVar() {
        CustomFactory factory = new CustomFactory();
        factory.setVarNum(5);
        BDD bdd = factory.one;
        assertEquals(bdd.var(), -1);
        bdd = factory.nithVar(1);
        assertEquals(bdd.var(), 1);
        bdd = bdd.or(factory.ithVar(0));
        assertEquals(bdd.var(), 0);
    }

    @Test
    public void testNot() {
        CustomFactory factory = new CustomFactory();
        factory.setVarNum(4);
        BDD oneBdd = factory.one();
        assertTrue(oneBdd.not().isZero());
    }

    @Test
    public void testExist() {
        CustomFactory factory = new CustomFactory();
        factory.setVarNum(4);
        BDD bdd = factory.ithVar(0).or(factory.nithVar(1)).and(factory.ithVar(3));
        BDD bddForExist = factory.ithVar(3);
        BDD actualResult = bdd.exist(bddForExist);
        BDD expectedResult = factory.nithVar(0).and(factory.nithVar(1))
                .or(factory.ithVar(0));
        //<0:0, 1:0><0:1>
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testForAll() {
        CustomFactory factory = new CustomFactory();
        factory.setVarNum(4);
        BDD bdd = factory.ithVar(0).or(factory.nithVar(1)).and(factory.ithVar(3));
        BDD bddForAll = factory.ithVar(3);
        BDD actualResult = bdd.forAll(bddForAll);
        BDD expectedResult = factory.zero();
        assertEquals(expectedResult, actualResult);
    }

    //@Test
    public void testUnique() {
        CustomFactory factory = new CustomFactory();
        factory.setVarNum(4);
        BDD bdd = factory.ithVar(0).or(factory.nithVar(1)).and(factory.ithVar(3));
        BDD bddForUnique = factory.ithVar(3);
        BDD actualResult = bdd.unique(bddForUnique);
        BDD expectedResult = factory.nithVar(0).and(factory.nithVar(1))
                .or(factory.ithVar(0));
        //<0:0, 1:0><0:1>
        assertEquals(expectedResult, actualResult);
    }

    //@Test
    public void testApply() {
        CustomFactory factory = new CustomFactory();
        factory.setVarNum(4);
        BDD bdd = factory.ithVar(0).or(factory.nithVar(1)).and(factory.ithVar(3));
        BDD otherBdd = factory.nithVar(3).xor(factory.ithVar(0));
        BDD actualResult = bdd.apply(otherBdd, BDDFactory.or);
        BDD expectedResult = otherBdd.or(bdd);
        assertEquals(actualResult, expectedResult);
        
        actualResult = bdd.apply(otherBdd, BDDFactory.and);
        expectedResult = otherBdd.and(bdd);
        assertEquals(expectedResult, actualResult);
        
        actualResult = otherBdd.apply(bdd, BDDFactory.xor);
        expectedResult = bdd.xor(otherBdd);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testOr() {
        CustomFactory factory = new CustomFactory();
        factory.setVarNum(6);
        BDD firstBdd = factory.nithVar(2).xor(factory.ithVar(0)).and(factory.ithVar(5)).not();
        BDD secondBdd = factory.ithVar(4).and(factory.ithVar(2)).or(factory.nithVar(0));
        BDD actualResult = firstBdd.or(secondBdd);
        BDD actualReverseResult = secondBdd.or(firstBdd);
        BDD expectedResult = factory.nithVar(0).or(factory.ithVar(0).and(factory.nithVar(2)))
                .or(factory.ithVar(0).and(factory.ithVar(2)).and(factory.nithVar(4)).and(factory.nithVar(5)))
                .or(factory.ithVar(0).and(factory.ithVar(2)).and(factory.ithVar(4)));
        //<0:0><0:1, 2:0><0:1, 2:1, 4:0, 5:0><0:1, 2:1, 4:1>;
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedResult, actualReverseResult);
        assertEquals(actualReverseResult, actualResult);
        
        firstBdd = factory.one();
        secondBdd = factory.zero();
        assertEquals(firstBdd.or(secondBdd), factory.one);
        
        firstBdd = factory.ithVar(2);
        BDD newResult = actualResult.or(firstBdd);
        expectedResult = factory.one();
        assertEquals(expectedResult, newResult);
    }
    
    @Test
    public void testAnd() {
        CustomFactory factory = new CustomFactory();
        factory.setVarNum(6);
        BDD firstBdd = factory.nithVar(2).xor(factory.ithVar(0)).and(factory.ithVar(5)).not();
        BDD secondBdd = factory.ithVar(4).and(factory.ithVar(2)).or(factory.nithVar(0));
        BDD actualResult = firstBdd.and(secondBdd);
        BDD actualReverseResult = secondBdd.and(firstBdd);
        BDD expectedResult = factory.nithVar(0).and(factory.nithVar(2)).and(factory.nithVar(5))
                .or(factory.nithVar(0).and(factory.ithVar(2)))
                .or(factory.ithVar(0).and(factory.ithVar(2)).and(factory.ithVar(4)).and(factory.nithVar(5)));
        //<0:0, 2:0, 5:0><0:0, 2:1><0:1, 2:1, 4:1, 5:0>
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedResult, actualReverseResult);
        assertEquals(actualReverseResult, actualResult);
        
        firstBdd = factory.one();
        secondBdd = factory.zero();
        assertEquals(firstBdd.and(secondBdd), factory.zero);
        
        firstBdd = factory.ithVar(2);
        BDD newResult = actualResult.and(firstBdd);
        expectedResult = factory.nithVar(0).and(factory.ithVar(2))
                .or(factory.ithVar(0).and(factory.ithVar(2)).and(factory.ithVar(4)).and(factory.nithVar(5)));
        //<0:0, 2:1><0:1, 2:1, 4:1, 5:0>
        assertEquals(expectedResult, newResult);
    }
    
    @Test
    public void testXor() {
        CustomFactory factory = new CustomFactory();
        factory.setVarNum(6);
        BDD firstBdd = factory.nithVar(2).xor(factory.ithVar(0)).and(factory.ithVar(5)).not();
        BDD secondBdd = factory.ithVar(4).and(factory.ithVar(2)).or(factory.nithVar(0));
        BDD actualResult = firstBdd.xor(secondBdd);
        BDD actualReverseResult = secondBdd.xor(firstBdd);
        BDD expectedResult = factory.nithVar(0).and(factory.nithVar(2)).and(factory.ithVar(5))
                .or(factory.ithVar(0).and(factory.nithVar(2)))
                .or(factory.ithVar(0).and(factory.ithVar(2)).and(factory.nithVar(4)).and(factory.nithVar(5)))
                .or(factory.ithVar(0).and(factory.ithVar(2)).and(factory.ithVar(4)).and(factory.ithVar(5)));
        //<0:0, 2:0, 5:1><0:1, 2:0><0:1, 2:1, 4:0, 5:0><0:1, 2:1, 4:1, 5:1>
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedResult, actualReverseResult);
        assertEquals(actualReverseResult, actualResult);
        
        firstBdd = factory.one();
        secondBdd = factory.zero();
        assertEquals(firstBdd.xor(secondBdd), factory.one);
        
        firstBdd = factory.ithVar(2);
        BDD newResult = actualResult.xor(firstBdd);
        expectedResult = factory.nithVar(0).and(factory.nithVar(2)).and(factory.ithVar(5))
                .or(factory.nithVar(0).and(factory.ithVar(2)))
                .or(factory.ithVar(0).and(factory.nithVar(2)))
                .or(factory.ithVar(0).and(factory.ithVar(2)).and(factory.nithVar(4)).and(factory.ithVar(5)))
                .or(factory.ithVar(0).and(factory.ithVar(2)).and(factory.ithVar(4)).and(factory.nithVar(5)));;
        //<0:0, 2:0, 5:1><0:0, 2:1><0:1, 2:0><0:1, 2:1, 4:0, 5:1><0:1, 2:1, 4:1, 5:0>
        assertEquals(expectedResult, newResult);
    }

    @Test
    public void testReplaceVariableWithValue() {
        CustomFactory customFactory = new CustomFactory();
        customFactory.setVarNum(3);
        CustomBDD sourceBdd = (CustomBDD) customFactory.one();
        assertEquals(sourceBdd.replaceVariableWithValue(1, true), customFactory.one);
        assertEquals(sourceBdd.replaceVariableWithValue(0, false), customFactory.one);
        assertEquals(sourceBdd.replaceVariableWithValue(2, true), customFactory.one);
        
        sourceBdd = (CustomBDD) customFactory.ithVar(2);
        assertEquals(sourceBdd.replaceVariableWithValue(2, true), customFactory.one);
        assertEquals(sourceBdd.replaceVariableWithValue(2, false), customFactory.zero);
        assertEquals(sourceBdd.replaceVariableWithValue(1, true), sourceBdd);
        
        //TODO expand to more sophisticated cases
    }

    @Test
    public void testFree() {
        CustomFactory factory = new CustomFactory();
        factory.setVarNum(4);
        BDD bdd = factory.ithVar(0).or(factory.nithVar(1)).and(factory.ithVar(3));
        bdd.free();
        String expectedResult = "";
        String actualResult = bdd.toString();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testEquals() {
        BDDFactory factory = new CustomFactory();
        factory.setVarNum(2);
        CustomBDD firstBdd = new CustomBDD(factory);
        CustomBDD secondBdd = new CustomBDD(factory);
        BDD thirdBdd = factory.ithVar(0);
        BDD fourthBdd = factory.ithVar(0);
        BDD fifthBdd = factory.nithVar(1);
        BDD sixthBdd = factory.ithVar(1);
        assertTrue(firstBdd.equals(secondBdd));
        assertTrue(thirdBdd.equals(fourthBdd));
        assertFalse(firstBdd.equals(sixthBdd));
        assertFalse(fifthBdd.equals(sixthBdd));
        //TODO expand to more sophisticated cases
    }

    @Test
    public void testClone() throws Exception {
        BDDFactory factory = new CustomFactory();
        factory.setVarNum(3);
        CustomBDD firstBdd = (CustomBDD) factory.ithVar(2);
        CustomBDD clonedBdd = firstBdd.clone();
        assertTrue(firstBdd.equals(clonedBdd) && clonedBdd.equals(firstBdd));
    }
    
    @Test
    public void testToString() {
        BDDFactory factory = new CustomFactory();
        factory.setVarNum(3);
        BDD bdd = factory.one();
        String actualResult = bdd.toString();
        String expectedResult = "<>";
        assertEquals(expectedResult, actualResult);
        
        bdd = factory.ithVar(0).and(factory.nithVar(1)).xor(factory.ithVar(2));
        actualResult = bdd.toString();
        expectedResult = "<0:0, 2:1><0:1, 1:0, 2:0><0:1, 1:1, 2:1>";
        assertEquals(expectedResult, actualResult);
        
        bdd = factory.ithVar(2);
        actualResult = bdd.toString();
        expectedResult = "<2:1>";
        assertEquals(expectedResult, actualResult);
    }
    
}
