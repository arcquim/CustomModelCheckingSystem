/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arcquim.custombdd;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class CustomBDDItemTest {

    @Test
    public void testEquals() {
        System.out.println("equals test");
        CustomBDDItem firstItem = new CustomBDDItem(0, 1, 0, null);
        CustomBDDItem secondItem = new CustomBDDItem(0, 1, 0, null);
        CustomBDDItem thirdItem = new CustomBDDItem(0, 1, 0, 0);
        assertTrue(firstItem.equals(secondItem));
        assertTrue(secondItem.equals(firstItem));
        assertFalse(firstItem.equals(thirdItem));
    }
    
}
