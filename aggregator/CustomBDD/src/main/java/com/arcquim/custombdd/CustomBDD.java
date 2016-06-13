package com.arcquim.custombdd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.BDDPairing;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class CustomBDD extends BDD implements Cloneable {
    
    //table that stores the OBDD
    private final Map<Integer, CustomBDDItem> bddMap;
    
    private final CustomFactory factory;
    
    private final Map<Integer, List<CustomBDDItem>> usedVariables = new HashMap<>();
    
    private static final String OPEN_BRACKET = "<";
    private static final String CLOSE_BRACKET = ">";
    private static final String COMMA_SEPARATOR = ", ";
    private static final String COLON_SEPARATOR = ":";
    private static final String ONE_BDD = "<>";
    private static final String ZERO_BDD = "";
    private static final String EMPTY_BDD = "";
    
    public CustomBDD(BDDFactory factory) {
        this(new HashMap<Integer, CustomBDDItem>(), factory);
    }
    
    public CustomBDD(Map<Integer, CustomBDDItem> bddMap, BDDFactory factory) {
        if (factory instanceof CustomFactory) {
            this.factory = (CustomFactory) factory;
        }
        else {
            this.factory = new CustomFactory();
            this.factory.setVarNum(factory.varNum());
        }
        this.bddMap = new HashMap<>(bddMap);
        initialize(true);
    }
    
    public CustomBDD(CustomBDD another) {
        this(another.bddMap, another.factory);
    }
    
    public void setBddMap(Map<Integer, CustomBDDItem> newBddMap, boolean reinit) {
        if (newBddMap != null) {
            bddMap.clear();
            bddMap.putAll(newBddMap);
            initialize(reinit);
        }
    }
    
    private void initialize(boolean reset) {
        usedVariables.clear();
        for (CustomBDDItem item : bddMap.values()) {
            if (item.getVariable() != null) {
                List<CustomBDDItem> items = usedVariables.get(item.getVariable());
                if (items == null) {
                    items = new LinkedList<>();
                    usedVariables.put(item.getVariable(), items);
                }
                items.add(item);
            }
        }
        if (reset) {
            subresults = null;
            reduced = false;
            result = null;
            var = null;
        }
    }

    @Override
    public BDDFactory getFactory() {
        return factory;
    }

    @Override
    public boolean isZero() {
        return factory.zero().equals(this);
    }

    @Override
    public boolean isOne() {
        return factory.one().equals(this);
    }
    
    private Integer var;

    /*
    *
    * @return index of the boolean variable that is a head of the OBDD
    */
    @Override
    public int var() {
        if (var == null) {
            Map<Integer, Integer> variablesOrder = factory.getVariablesOrderMapping();
            Integer minimalOrder = factory.varNum(), requiredVar = -1;
            for (Integer variable : usedVariables.keySet()) {
                Integer variableOrder = variablesOrder.get(variable);
                if (variableOrder < minimalOrder) {
                    requiredVar = variable;
                    minimalOrder = variableOrder;
                }
            }
            var = requiredVar;
        }
        return var;
    }
    
    protected void reorganize() {
        var = null;
    }

    @Override
    public BDD high() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BDD low() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BDD id() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /*
    *
    * @return CustomBDD representing the result of the not operator over this CustomBDD
    */
    @Override
    public BDD not() {
        //creating table for new OBDD
        Map<Integer, CustomBDDItem> mapForNewBDD = new HashMap<>(bddMap);
        CustomBDDItem zeroItem = mapForNewBDD.get(CustomBDDItem.DEFAULT_ZERO_ITEM_KEY);
        CustomBDDItem oneItem = mapForNewBDD.get(CustomBDDItem.DEFAULT_ONE_ITEM_KEY);
        if (zeroItem != null && oneItem != null) { //the BDD is not empty
            //start of terminal variables parents swapping
            CustomBDDItem newZeroItem = new CustomBDDItem(zeroItem);
            newZeroItem.setAdditionalParents(oneItem.getAdditionalParents());
            CustomBDDItem newOneItem = new CustomBDDItem(oneItem);
            newOneItem.setAdditionalParents(zeroItem.getAdditionalParents());
            mapForNewBDD.put(CustomBDDItem.DEFAULT_ZERO_ITEM_KEY, newZeroItem);
            mapForNewBDD.put(CustomBDDItem.DEFAULT_ONE_ITEM_KEY, newOneItem);
            //parent swapped; let's make those parents linking to new children
            List<Integer> newZeroAdditionalParents = newZeroItem.getAdditionalParents();
            if (newZeroAdditionalParents != null) {
                //processing parent of new zero (old one)
                for (Integer additionalParent : newZeroAdditionalParents) {
                    CustomBDDItem newItem = new CustomBDDItem(mapForNewBDD.get(additionalParent));
                    if (newItem.getZeroLink().equals(CustomBDDItem.DEFAULT_ONE_ITEM_KEY)) {
                        if (newItem.getOneLink().equals(CustomBDDItem.DEFAULT_ZERO_ITEM_KEY)) {
                            newItem.setZeroLink(null);
                        }
                        else {
                            newItem.setZeroLink(CustomBDDItem.DEFAULT_ZERO_ITEM_KEY);
                        }
                    }
                    else {
                        if (newItem.getZeroLink().equals(CustomBDDItem.DEFAULT_ZERO_ITEM_KEY)) {
                            newItem.setOneLink(null);
                        }
                        else {
                            newItem.setOneLink(CustomBDDItem.DEFAULT_ZERO_ITEM_KEY);
                        }
                    }
                    mapForNewBDD.put(additionalParent, newItem);
                }
            }
            List<Integer> newOneAdditionalParents = newOneItem.getAdditionalParents();
            if (newOneAdditionalParents != null) {
                //processing parent of new one (old zero)
                for (Integer additionalParent : newOneAdditionalParents) {
                    CustomBDDItem nextItem = mapForNewBDD.get(additionalParent);
                    if (nextItem.getZeroLink() != null && nextItem.getOneLink() != null) {
                        nextItem = new CustomBDDItem(nextItem);
                        mapForNewBDD.put(additionalParent, nextItem);
                    }
                    if (CustomBDDItem.DEFAULT_ZERO_ITEM_KEY.equals(nextItem.getZeroLink())) {
                        nextItem.setZeroLink(CustomBDDItem.DEFAULT_ONE_ITEM_KEY);
                        if (nextItem.getOneLink() == null) {
                            nextItem.setOneLink(CustomBDDItem.DEFAULT_ZERO_ITEM_KEY);
                        }
                    }
                    else {
                        nextItem.setOneLink(CustomBDDItem.DEFAULT_ONE_ITEM_KEY);
                        if (nextItem.getZeroLink() == null) {
                            nextItem.setZeroLink(CustomBDDItem.DEFAULT_ZERO_ITEM_KEY);
                        }
                    }
                }
            }
        }
        else {//the CustomBDD is constant; we just need to reverse this constant
            if (zeroItem != null) {
                return factory.one();
            }
            else {
                return factory.zero();
            }
        }
        return new CustomBDD(mapForNewBDD, factory);
    }

    @Override
    public BDD ite(BDD bdd, BDD bdd1) {
        if (bdd instanceof CustomBDD && bdd1 instanceof CustomBDD) {
            //processing CustomBDD instances only
            return ifThenElse((CustomBDD)bdd, (CustomBDD)bdd1);
        }
        throw new IllegalArgumentException("Only CustomBDD instances are allowed");
    }

    /*
    * This operation merges three OBDDs into one
    * @param stepResultOne the then-OBDD for the head of the OBDD
    * @param stepResultZero the else-OBDD for the head of the OBDD
    * @return CustomBDD representing the result of the ifThenElse operator over 
    * this CustomBDD, then-CustomBDD and else-CustomBDD
    */
    public CustomBDD ifThenElse(CustomBDD stepResultOne, CustomBDD stepResultZero) {
        Integer thisVar = this.var();
        CustomBDDItem newHeadItem = new CustomBDDItem(this.usedVariables.get(thisVar).get(0));
        Map<Integer, CustomBDDItem> items = new HashMap<>();
        
        //reducing arguments before merging, considering OBDDs 
        //under the non-head vertexes reduced
        stepResultZero.reduceVar();
        stepResultOne.reduceVar();
        
        if (stepResultOne.equals(stepResultZero)) {
            //in this case we do not need merging
            return stepResultZero;
        }
        //before merging we have to make ids unique in the tables of the arguments
        Integer newHeadItemIndex = makeIdsUnique(stepResultZero, stepResultOne);
        
        //getting heads on the agruments
        Integer stepResultZeroVar = stepResultZero.var();
        Integer stepResultOneVar = stepResultOne.var();
        CustomBDDItem varItemZero, varItemOne;
        Integer newHeadItemZeroLink = CustomBDDItem.DEFAULT_NEXT_ITEM_KEY,
                newHeadItemOneLink = CustomBDDItem.DEFAULT_NEXT_ITEM_KEY;
        if (stepResultZeroVar >= 0) {//stepResultZero is not a constant
            varItemZero = stepResultZero.usedVariables.get(stepResultZeroVar).get(0);
            //getting the immediate child of the head
            CustomBDDItem childItem = stepResultZero.bddMap.get(varItemZero.getZeroLink());
            if (childItem == null || childItem.getParentLink() == null) {
                childItem = stepResultZero.bddMap.get(varItemZero.getOneLink());
            }
            if (childItem == null) {
                return new CustomBDD(factory);
            }
            if (childItem.getParentLink() == null) {//the child is a terminal vertex
                List<Integer> additionalParents = childItem.getAdditionalParents();
                if (additionalParents == null || additionalParents.isEmpty()) {
                    return new CustomBDD(factory);
                }
                //searching for an index of the head vertex of stepResultZero
                for (Integer parent : additionalParents) {
                    CustomBDDItem parentItem = stepResultZero.bddMap.get(parent);
                    if (varItemZero == parentItem) {
                        newHeadItemZeroLink = parent;
                        break;
                    }
                }
            }
            else {//the child is non-terminal
                newHeadItemZeroLink = childItem.getParentLink();
            }
            //the head of stepResultZero is linking to the head of this CustomBDD
            varItemZero.setParentLink(newHeadItemIndex);
        }
        else {//the stepResultZero is just a constant
            newHeadItemZeroLink = stepResultZero.bddMap.get(CustomBDDItem.DEFAULT_ZERO_ITEM_KEY) == null ?
                    CustomBDDItem.DEFAULT_ONE_ITEM_KEY : CustomBDDItem.DEFAULT_ZERO_ITEM_KEY;
            varItemZero = stepResultZero.bddMap.get(newHeadItemZeroLink);
            varItemZero.addAdditionalParent(newHeadItemIndex);
        }
        
        if (stepResultOneVar >= 0) {//stepResultOne is not a constant
            varItemOne = stepResultOne.usedVariables.get(stepResultOneVar).get(0);
            //getting the immediate child of the head
            CustomBDDItem childItem = stepResultOne.bddMap.get(varItemOne.getZeroLink());
            if (childItem == null || childItem.getParentLink() == null) {
                childItem = stepResultOne.bddMap.get(varItemOne.getOneLink());
            }
            if (childItem == null) {
                return new CustomBDD(factory);
            }
            if (childItem.getParentLink() == null) {//the child is a terminal vertex
                List<Integer> additionalParents = childItem.getAdditionalParents();
                if (additionalParents == null || additionalParents.isEmpty()) {
                    return new CustomBDD(factory);
                }
                //searching for an index of the head vertex of stepResultOne
                for (Integer parent : additionalParents) {
                    CustomBDDItem parentItem = stepResultOne.bddMap.get(parent);
                    if (varItemOne == parentItem) {
                        newHeadItemOneLink = parent;
                        break;
                    }
                }
            }
            else {//the child is non-terminal
                newHeadItemOneLink = childItem.getParentLink();
            }
            //the head of stepResultOne is linking to the head of this CustomBDD
            varItemOne.setParentLink(newHeadItemIndex);
        }
        else {//the stepResultOne is just a constant
            newHeadItemOneLink = stepResultOne.bddMap.get(CustomBDDItem.DEFAULT_ONE_ITEM_KEY) == null ?
                    CustomBDDItem.DEFAULT_ZERO_ITEM_KEY : CustomBDDItem.DEFAULT_ONE_ITEM_KEY;
            varItemOne = stepResultOne.bddMap.get(newHeadItemOneLink);
            varItemOne.addAdditionalParent(newHeadItemIndex);
        }
        //making the head vertex of new CustomBDD linking to its children
        newHeadItem.setZeroLink(newHeadItemZeroLink);
        newHeadItem.setOneLink(newHeadItemOneLink);
        newHeadItem.setParentLink(null);
        newHeadItem.setAdditionalParents(null);
        stepResultZero.bddMap.put(newHeadItemIndex, newHeadItem);
        stepResultOne.bddMap.put(newHeadItemIndex, newHeadItem);
        items.putAll(stepResultZero.bddMap);
        //we need to merge stepResultOne and stepResultZero, because we should
        //have two terminal vertexes only
        stepResultOne.mergeIntoAnotherBddMap(stepResultZero.bddMap);
        items.putAll(stepResultOne.bddMap);
        return new CustomBDD(items, factory);
    }
    
    private void mergeIntoAnotherBddMap(Map<Integer, CustomBDDItem> anotherBddMap) {
        Integer thisZeroIndex = CustomBDDItem.DEFAULT_ZERO_ITEM_KEY, 
                thisOneIndex = CustomBDDItem.DEFAULT_ONE_ITEM_KEY, 
                thatZeroIndex = CustomBDDItem.DEFAULT_ZERO_ITEM_KEY, 
                thatOneIndex = CustomBDDItem.DEFAULT_ONE_ITEM_KEY;
        
        CustomBDDItem thisZeroItem = bddMap.get(thisZeroIndex), 
                thisOneItem = bddMap.get(thisOneIndex), 
                thatZeroItem = anotherBddMap.get(thatZeroIndex), 
                thatOneItem = anotherBddMap.get(thatOneIndex),
                buffer;
        
        int thisMapSize = bddMap.size(), thatMapSize = anotherBddMap.size();
        
        if (thisZeroItem != null) {
            if (thisZeroItem.getZeroLink().equals(CustomBDDItem.DEFAULT_ONE_ITEM_KEY)) {
                buffer = thisOneItem;
                thisOneItem = thisZeroItem;
                thisZeroItem = buffer;
                thisOneIndex = CustomBDDItem.DEFAULT_ZERO_ITEM_KEY;
                thisZeroIndex = CustomBDDItem.DEFAULT_ONE_ITEM_KEY;
            }
        }
        if (thisOneItem != null) {
            if (thisOneItem.getZeroLink().equals(CustomBDDItem.DEFAULT_ZERO_ITEM_KEY)) {
                buffer = thisOneItem;
                thisOneItem = thisZeroItem;
                thisZeroItem = buffer;
                thisZeroIndex = CustomBDDItem.DEFAULT_ONE_ITEM_KEY;
                thisOneIndex = CustomBDDItem.DEFAULT_ZERO_ITEM_KEY;
            }
        }
        if (thatZeroItem != null) {
            if (thatZeroItem.getZeroLink().equals(CustomBDDItem.DEFAULT_ONE_ITEM_KEY)) {
                buffer = thatOneItem;
                thatOneItem = thatZeroItem;
                thatZeroItem = buffer;
                thatOneIndex = CustomBDDItem.DEFAULT_ZERO_ITEM_KEY;
                thatZeroIndex = CustomBDDItem.DEFAULT_ONE_ITEM_KEY;
            }
        }
        if (thatOneItem != null) {
            if (thatOneItem.getZeroLink().equals(CustomBDDItem.DEFAULT_ZERO_ITEM_KEY)) {
                buffer = thatOneItem;
                thatOneItem = thatZeroItem;
                thatZeroItem = buffer;
                thatOneIndex = CustomBDDItem.DEFAULT_ZERO_ITEM_KEY;
                thatZeroIndex = CustomBDDItem.DEFAULT_ONE_ITEM_KEY;
            }
        }
        
        if (thisZeroItem != null && thatZeroItem != null) {
            List<Integer> thisZeroItemAdditionalParents = thisZeroItem.getAdditionalParents();
            List<Integer> thatZeroItemAdditionalParents = thatZeroItem.getAdditionalParents();
            if (thisZeroItemAdditionalParents == null) {
                thisZeroItem.setAdditionalParents(thatZeroItemAdditionalParents);
            }
            else {
                if (thatZeroItemAdditionalParents != null) {
                    for (Integer additionalParent : thatZeroItemAdditionalParents) {
                        if (!thisZeroItemAdditionalParents.contains(additionalParent)) {
                            thisZeroItemAdditionalParents.add(additionalParent);
                        }
                    }
                }
            }
        }
        
        if (thisOneItem != null && thatOneItem != null) {
            List<Integer> thisOneItemAdditionalParents = thisOneItem.getAdditionalParents();
            List<Integer> thatOneItemAdditionalParents = thatOneItem.getAdditionalParents();
            if (thisOneItemAdditionalParents == null) {
                thisOneItem.setAdditionalParents(thatOneItemAdditionalParents);
            }
            else {
                if (thatOneItemAdditionalParents != null) {
                    for (Integer additionalParent : thatOneItemAdditionalParents) {
                        if (!thisOneItemAdditionalParents.contains(additionalParent)) {
                            thisOneItemAdditionalParents.add(additionalParent);
                        }
                    }
                }
            }
        }
        
        if (thisZeroIndex != thatZeroIndex || thisOneIndex != thatOneIndex) {
            if (thisZeroItem != null) {
                List<Integer> additionalParents = thisZeroItem.getAdditionalParents();
                if (additionalParents != null) {
                    for (Integer parent : additionalParents) {
                        CustomBDDItem nextItem = anotherBddMap.get(parent);
                        if (nextItem != null) {
                            if (nextItem.getZeroLink().equals(thatZeroIndex)) {
                                nextItem.setZeroLink(thisZeroIndex);
                            }
                            else {
                                nextItem.setOneLink(thisZeroIndex);
                            }
                        }
                    }
                }
            }
            else {
                if (thatZeroItem != null) {
                    bddMap.put(thisZeroIndex, thatZeroItem);
                    List<Integer> additionalParent = thatZeroItem.getAdditionalParents();
                    if (additionalParent != null) {
                        for (Integer parent : additionalParent) {
                            CustomBDDItem nextItem = anotherBddMap.get(parent);
                            if (nextItem != null) {
                                if (nextItem.getZeroLink().equals(thatZeroIndex)) {
                                    nextItem.setZeroLink(thisZeroIndex);
                                }
                                else {
                                    nextItem.setOneLink(thisZeroIndex);
                                }
                            }
                        }
                    }
                }
            }
            if (thisOneItem != null && (thisMapSize > 2 || thatMapSize > 2)) {
                List<Integer> additionalParents = thisOneItem.getAdditionalParents();
                if (additionalParents != null) {
                    for (Integer parent : additionalParents) {
                        CustomBDDItem nextItem = anotherBddMap.get(parent);
                        if (nextItem != null) {
                            if (nextItem.getZeroLink().equals(thatOneIndex)) {
                                nextItem.setZeroLink(thisOneIndex);
                            }
                            else {
                                nextItem.setOneLink(thisOneIndex);
                            }
                        }
                    }
                }
            }
            else {
                if (thatOneItem != null) {
                    bddMap.put(thisOneIndex, thatOneItem);
                    List<Integer> additionalParent = thatOneItem.getAdditionalParents();
                    if (additionalParent != null) {
                        for (Integer parent : additionalParent) {
                            CustomBDDItem nextItem = anotherBddMap.get(parent);
                            if (nextItem != null) {
                                if (nextItem.getZeroLink().equals(thatOneIndex)) {
                                    nextItem.setZeroLink(thisOneIndex);
                                }
                                else {
                                    nextItem.setOneLink(thisOneIndex);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public BDD relprod(BDD bdd, BDD bdd1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BDD compose(BDD bdd, int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BDD veccompose(BDDPairing bddp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BDD constrain(BDD bdd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     *
     * @param bdd the CustomBDD instance describing the variables to apply quantifier over
     * @return CustomBDD instance: the result of existentional quantifier applying
     */
    @Override
    public BDD exist(BDD bdd) {
        if (bdd.getFactory() != factory || !(bdd instanceof CustomBDD)) {
            throw new IllegalArgumentException("Operations allowed only on "
                    + "BDDs with the same factory and implementation classes.");
        }
        CustomBDD customBDD = (CustomBDD) bdd;
        CustomBDD existResult = this;
        //we are considering the argument as a set of variables
        Set<Integer> thisBddVariables = usedVariables.keySet();
        for (Integer bddVariable : customBDD.usedVariables.keySet()) {
            //applying existentional quantifier over this OBDD and next variable
            if (thisBddVariables.contains(bddVariable)) {
                CustomBDD thisZero = existResult.replaceVariableWithValue(bddVariable, false);
                CustomBDD thisOne = existResult.replaceVariableWithValue(bddVariable, true);
                existResult = (CustomBDD) thisZero.or(thisOne);
            }
        }
        if (existResult == this) {//there are no variables to make quantification
            return new CustomBDD(existResult);
        }
        else {
            return existResult;
        }
    }

    /**
     *
     * @param bdd the CustomBDD instance describing the variables to apply quantifier over
     * @return CustomBDD instance: the result of universal quantifier applying
     */
    @Override
    public BDD forAll(BDD bdd) {
        if (bdd.getFactory() != factory || !(bdd instanceof CustomBDD)) {
            throw new IllegalArgumentException("Operations allowed only on "
                    + "BDDs with the same factory and implementation classes.");
        }
        CustomBDD customBDD = (CustomBDD) bdd;
        CustomBDD forAllResult = this;
        //we are considering the argument as a set of variables
        Set<Integer> thisBddVariables = usedVariables.keySet();
        for (Integer bddVariable : customBDD.usedVariables.keySet()) {
            //applying universal quantifier over this OBDD and next variable
            if (thisBddVariables.contains(bddVariable)) {
                CustomBDD thisZero = forAllResult.replaceVariableWithValue(bddVariable, false);
                CustomBDD thisOne = forAllResult.replaceVariableWithValue(bddVariable, true);
                forAllResult = (CustomBDD) thisZero.and(thisOne);
            }
        }
        if (forAllResult == this) {//there are no variables to make quantification
            return new CustomBDD(forAllResult);
        }
        else {
            return forAllResult;
        }
    }

    @Override
    public BDD unique(BDD bdd) {
        return null;
    }

    @Override
    public BDD restrict(BDD bdd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BDD restrictWith(BDD bdd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BDD simplify(BDD bdd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BDD support() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //counters for operation caches
    private static int orCount = 0;
    private static int andCount = 0;
    private static int xorCount = 0;

    @Override
    public BDD apply(BDD bdd, BDDFactory.BDDOp bddop) {
        if (bdd.getFactory() != this.factory || !bdd.getClass().equals(CustomBDD.class)) {
            // we are supporting only and only CustomBDD instances, that have the same factory object
            throw new IllegalArgumentException("Operations allowed only on "
                    + "BDDs with the same factory and implementation classes.");
        }
        CustomBDD customBDD = (CustomBDD) bdd;
        if (BDDFactory.or.equals(bddop)) {
            //perform the OR operation
            CustomBDD resultBdd = performOr(customBDD);
            //reduce the head of the result; non-head vertexes have been reduced already
            resultBdd.reduceVar();
            orCount++;
            if (orCount == 100) {//limit of the OR cache
                orCount = 0;
                factory.currentOrCache.clear();
            }
            //saying JVM to perform garbage collection
            System.gc();
            return resultBdd;
        }
        if (BDDFactory.and.equals(bddop)) {
            //perform the AND operation
            CustomBDD resultBdd = performAnd(customBDD);
            //reduce the head of the result; non-head vertexes have been reduced already
            resultBdd.reduceVar();
            andCount++;
            if (andCount == 100) {//limit of the AND cache
                andCount = 0;
                factory.currentAndCache.clear();
            }
            //saying JVM to perform garbage collection
            System.gc();
            return resultBdd;
        }
        if (BDDFactory.xor.equals(bddop)) {
            //perform the XOR operation
            CustomBDD resultBdd = performXor(customBDD);
            //reduce the head of the result; non-head vertexes have been reduced already
            resultBdd.reduceVar();
            xorCount++;
            if (xorCount == 100) {//limit of the XOR cache
                xorCount = 0;
                factory.currentXorCache.clear();
            }
            //saying JVM to perform garbage collection
            System.gc();
            return resultBdd;
        }
        //we do not support other binary operations over CustomBDD instances
        throw new UnsupportedOperationException();
    }
    
    private CustomBDD performOr(CustomBDD bdd) {
        String thisBdd = this.toString();
        String thatBdd = bdd.toString();
        CustomBDD cachedBdd = factory.currentOrCache.get(thisBdd + ";" + thatBdd);
        if (cachedBdd != null) {
            //certain luck: we have already performed OR over the same OBDDs
            Map<Integer, CustomBDDItem> newMap = new HashMap<>();
            for (Entry<Integer, CustomBDDItem> entry : cachedBdd.bddMap.entrySet()) {
                newMap.put(entry.getKey(), new CustomBDDItem(entry.getValue()));
            }
            return new CustomBDD(newMap, factory);
        }
        cachedBdd = factory.currentOrCache.get(thatBdd + ";" + thisBdd);
        if (cachedBdd != null) {
            //certain luck: we have already performed OR over the same OBDDs
            Map<Integer, CustomBDDItem> newMap = new HashMap<>();
            for (Entry<Integer, CustomBDDItem> entry : cachedBdd.bddMap.entrySet()) {
                newMap.put(entry.getKey(), new CustomBDDItem(entry.getValue()));
            }
            return new CustomBDD(newMap, factory);
        }
        if (bdd.isOne() || this.isOne()) {
            //constant one gives constant one with any boolean function
            return new CustomBDD(factory.one);
        }
        if (bdd.isZero()) {
            //constant zero gives the second argument with any boolean function
            return new CustomBDD(this);
        }
        if (this.isZero()) {
            //constant zero gives the second argument with any boolean function
            return new CustomBDD(bdd);
        }
        Map<Integer, Integer> variablesOrder = factory.getVariablesOrderMapping();
        //getting head variables indexes
        Integer thisVar = this.var();
        Integer thatVar = bdd.var();
        Integer thisVarOrder = variablesOrder.get(thisVar);
        Integer thatVarOrder = variablesOrder.get(thatVar);
        if (thisVarOrder.equals(thatVarOrder)) {//the head variables are the same
            CustomBDD thisZeroSubstitution = this.replaceVariableWithValue(thisVar, false);
            CustomBDD thisOneSubstitution = this.replaceVariableWithValue(thisVar, true);
            CustomBDD thatZeroSubstitution = bdd.replaceVariableWithValue(thatVar, false);
            CustomBDD thatOneSubstitution = bdd.replaceVariableWithValue(thatVar, true);
            //go one level lower
            CustomBDD stepResultZero = thisZeroSubstitution.performOr(thatZeroSubstitution);
            CustomBDD stepResultOne = thisOneSubstitution.performOr(thatOneSubstitution);
            //merge partly-getted OBDDs
            cachedBdd = ifThenElse(stepResultOne, stepResultZero);
            //put the result into the cache
            factory.currentOrCache.put(thisBdd + ";" + thatBdd, cachedBdd);
            //construct the result OBDD
            Map<Integer, CustomBDDItem> newMap = new HashMap<>();
            for (Entry<Integer, CustomBDDItem> entry : cachedBdd.bddMap.entrySet()) {
                newMap.put(entry.getKey(), new CustomBDDItem(entry.getValue()));
            }
            return new CustomBDD(newMap, factory);
        }
        
        if (thisVarOrder < thatVarOrder) {//the head variable of this CustomBDD is smaller
            CustomBDD thisZeroSubstitution = this.replaceVariableWithValue(thisVar, false);
            CustomBDD thisOneSubstitution = this.replaceVariableWithValue(thisVar, true);
            //go one level lower
            CustomBDD stepResultZero = thisZeroSubstitution.performOr(new CustomBDD(bdd));
            CustomBDD stepResultOne = thisOneSubstitution.performOr(bdd);
            //merge partly-getted OBDDs
            cachedBdd = ifThenElse(stepResultOne, stepResultZero);
            //put the result into the cache
            factory.currentOrCache.put(thisBdd + ";" + thatBdd, cachedBdd);
            //construct the result OBDD
            Map<Integer, CustomBDDItem> newMap = new HashMap<>();
            for (Entry<Integer, CustomBDDItem> entry : cachedBdd.bddMap.entrySet()) {
                newMap.put(entry.getKey(), new CustomBDDItem(entry.getValue()));
            }
            return new CustomBDD(newMap, factory);
        }
        
        if (thatVarOrder < thisVarOrder) {//the head variable of this CustomBDD is bigger
            CustomBDD thatZeroSubstitution = bdd.replaceVariableWithValue(thatVar, false);
            CustomBDD thatOneSubstitution = bdd.replaceVariableWithValue(thatVar, true);
            //go one level lower
            CustomBDD stepResultZero = performOr(thatZeroSubstitution);
            CustomBDD stepResultOne = performOr(thatOneSubstitution);
            //merge partly-getted OBDDs
            cachedBdd = bdd.ifThenElse(stepResultOne, stepResultZero);
            //put the result into the cache
            factory.currentOrCache.put(thisBdd + ";" + thatBdd, cachedBdd);
            //construct the result OBDD
            Map<Integer, CustomBDDItem> newMap = new HashMap<>();
            for (Entry<Integer, CustomBDDItem> entry : cachedBdd.bddMap.entrySet()) {
                newMap.put(entry.getKey(), new CustomBDDItem(entry.getValue()));
            }
            return new CustomBDD(newMap, factory);
        }
        return new CustomBDD(factory);
    }
    
    private CustomBDD performAnd(CustomBDD bdd) {
        String thisBdd = this.toString();
        String thatBdd = bdd.toString();
        CustomBDD cachedBdd = factory.currentAndCache.get(thisBdd + ";" + thatBdd);
        if (cachedBdd != null) {
            Map<Integer, CustomBDDItem> newMap = new HashMap<>();
            for (Entry<Integer, CustomBDDItem> entry : cachedBdd.bddMap.entrySet()) {
                newMap.put(entry.getKey(), new CustomBDDItem(entry.getValue()));
            }
            return new CustomBDD(newMap, factory);
        }
        cachedBdd = factory.currentAndCache.get(thatBdd + ";" + thisBdd);
        if (cachedBdd != null) {
            Map<Integer, CustomBDDItem> newMap = new HashMap<>();
            for (Entry<Integer, CustomBDDItem> entry : cachedBdd.bddMap.entrySet()) {
                newMap.put(entry.getKey(), new CustomBDDItem(entry.getValue()));
            }
            return new CustomBDD(newMap, factory);
        }
        if (bdd.isZero() || this.isZero()) {
            return new CustomBDD(factory.zero);
        }
        if (bdd.isOne()) {
            return new CustomBDD(this);
        }
        if (this.isOne()) {
            return new CustomBDD(bdd);
        }
        Map<Integer, Integer> variablesOrder = factory.getVariablesOrderMapping();
        Integer thisVar = this.var();
        Integer thatVar = bdd.var();
        Integer thisVarOrder = variablesOrder.get(thisVar);
        Integer thatVarOrder = variablesOrder.get(thatVar);
        if (thisVarOrder.equals(thatVarOrder)) {
            CustomBDD thisZeroSubstitution = this.replaceVariableWithValue(thisVar, false);
            CustomBDD thisOneSubstitution = this.replaceVariableWithValue(thisVar, true);
            CustomBDD thatZeroSubstitution = bdd.replaceVariableWithValue(thatVar, false);
            CustomBDD thatOneSubstitution = bdd.replaceVariableWithValue(thatVar, true);
            CustomBDD stepResultZero = thisZeroSubstitution.performAnd(thatZeroSubstitution);
            CustomBDD stepResultOne = thisOneSubstitution.performAnd(thatOneSubstitution);
            cachedBdd = ifThenElse(stepResultOne, stepResultZero);
            factory.currentAndCache.put(thisBdd + ";" + thatBdd, cachedBdd);
            Map<Integer, CustomBDDItem> newMap = new HashMap<>();
            for (Entry<Integer, CustomBDDItem> entry : cachedBdd.bddMap.entrySet()) {
                newMap.put(entry.getKey(), new CustomBDDItem(entry.getValue()));
            }
            return new CustomBDD(newMap, factory);
        }
        
        if (thisVarOrder < thatVarOrder) {
            CustomBDD thisZeroSubstitution = this.replaceVariableWithValue(thisVar, false);
            CustomBDD thisOneSubstitution = this.replaceVariableWithValue(thisVar, true);
            CustomBDD stepResultZero = thisZeroSubstitution.performAnd(bdd);
            CustomBDD stepResultOne = thisOneSubstitution.performAnd(bdd);
            cachedBdd = ifThenElse(stepResultOne, stepResultZero);
            factory.currentAndCache.put(thisBdd + ";" + thatBdd, cachedBdd);
            Map<Integer, CustomBDDItem> newMap = new HashMap<>();
            for (Entry<Integer, CustomBDDItem> entry : cachedBdd.bddMap.entrySet()) {
                newMap.put(entry.getKey(), new CustomBDDItem(entry.getValue()));
            }
            return new CustomBDD(newMap, factory);
        }
        
        if (thatVarOrder < thisVarOrder) {
            CustomBDD thatZeroSubstitution = bdd.replaceVariableWithValue(thatVar, false);
            CustomBDD thatOneSubstitution = bdd.replaceVariableWithValue(thatVar, true);
            CustomBDD stepResultZero = performAnd(thatZeroSubstitution);
            CustomBDD stepResultOne = performAnd(thatOneSubstitution);
            cachedBdd = bdd.ifThenElse(stepResultOne, stepResultZero);
            factory.currentAndCache.put(thisBdd + ";" + thatBdd, cachedBdd);
            Map<Integer, CustomBDDItem> newMap = new HashMap<>();
            for (Entry<Integer, CustomBDDItem> entry : cachedBdd.bddMap.entrySet()) {
                newMap.put(entry.getKey(), new CustomBDDItem(entry.getValue()));
            }
            return new CustomBDD(newMap, factory);
        }
        return new CustomBDD(factory);
    }
    
    private CustomBDD performXor(CustomBDD bdd) {
        String thisBdd = this.toString();
        String thatBdd = bdd.toString();
        CustomBDD cachedBdd = factory.currentXorCache.get(thisBdd + ";" + thatBdd);
        if (cachedBdd != null) {
            Map<Integer, CustomBDDItem> newMap = new HashMap<>();
            for (Entry<Integer, CustomBDDItem> entry : cachedBdd.bddMap.entrySet()) {
                newMap.put(entry.getKey(), new CustomBDDItem(entry.getValue()));
            }
            return new CustomBDD(newMap, factory);
        }
        cachedBdd = factory.currentXorCache.get(thatBdd + ";" + thisBdd);
        if (cachedBdd != null) {
            Map<Integer, CustomBDDItem> newMap = new HashMap<>();
            for (Entry<Integer, CustomBDDItem> entry : cachedBdd.bddMap.entrySet()) {
                newMap.put(entry.getKey(), new CustomBDDItem(entry.getValue()));
            }
            return new CustomBDD(newMap, factory);
        }
        if (bdd.isZero()) {
            return new CustomBDD(this);
        }
        if (bdd.isOne()) {
            return (CustomBDD) this.not();
        }
        if (this.isZero()) {
            return new CustomBDD(bdd);
        }
        if (this.isOne()) {
            return (CustomBDD) bdd.not();
        }
        Map<Integer, Integer> variablesOrder = factory.getVariablesOrderMapping();
        Integer thisVar = this.var();
        Integer thatVar = bdd.var();
        Integer thisVarOrder = variablesOrder.get(thisVar);
        Integer thatVarOrder = variablesOrder.get(thatVar);
        if (thisVarOrder.equals(thatVarOrder)) {
            CustomBDD thisZeroSubstitution = this.replaceVariableWithValue(thisVar, false);
            CustomBDD thisOneSubstitution = this.replaceVariableWithValue(thisVar, true);
            CustomBDD thatZeroSubstitution = bdd.replaceVariableWithValue(thatVar, false);
            CustomBDD thatOneSubstitution = bdd.replaceVariableWithValue(thatVar, true);
            CustomBDD stepResultZero = thisZeroSubstitution.performXor(thatZeroSubstitution);
            CustomBDD stepResultOne = thisOneSubstitution.performXor(thatOneSubstitution);
            cachedBdd = ifThenElse(stepResultOne, stepResultZero);
            factory.currentXorCache.put(thisBdd + ";" + thatBdd, cachedBdd);
            Map<Integer, CustomBDDItem> newMap = new HashMap<>();
            for (Entry<Integer, CustomBDDItem> entry : cachedBdd.bddMap.entrySet()) {
                newMap.put(entry.getKey(), new CustomBDDItem(entry.getValue()));
            }
            return new CustomBDD(newMap, factory);
        }
        
        if (thisVarOrder < thatVarOrder) {
            CustomBDD thisZeroSubstitution = this.replaceVariableWithValue(thisVar, false);
            CustomBDD thisOneSubstitution = this.replaceVariableWithValue(thisVar, true);
            CustomBDD stepResultZero = thisZeroSubstitution.performXor(bdd);
            CustomBDD stepResultOne = thisOneSubstitution.performXor(bdd);
            cachedBdd = ifThenElse(stepResultOne, stepResultZero);
            factory.currentXorCache.put(thisBdd + ";" + thatBdd, cachedBdd);
            Map<Integer, CustomBDDItem> newMap = new HashMap<>();
            for (Entry<Integer, CustomBDDItem> entry : cachedBdd.bddMap.entrySet()) {
                newMap.put(entry.getKey(), new CustomBDDItem(entry.getValue()));
            }
            return new CustomBDD(newMap, factory);
        }
        
        if (thatVarOrder < thisVarOrder) {
            CustomBDD thatZeroSubstitution = bdd.replaceVariableWithValue(thatVar, false);
            CustomBDD thatOneSubstitution = bdd.replaceVariableWithValue(thatVar, true);
            CustomBDD stepResultZero = performXor(thatZeroSubstitution);
            CustomBDD stepResultOne = performXor(thatOneSubstitution);
            cachedBdd = bdd.ifThenElse(stepResultOne, stepResultZero);
            factory.currentXorCache.put(thisBdd + ";" + thatBdd, cachedBdd);
            Map<Integer, CustomBDDItem> newMap = new HashMap<>();
            for (Entry<Integer, CustomBDDItem> entry : cachedBdd.bddMap.entrySet()) {
                newMap.put(entry.getKey(), new CustomBDDItem(entry.getValue()));
            }
            return new CustomBDD(newMap, factory);
        }
        return new CustomBDD(factory);
    }
    
    private static int makeIdsUnique(CustomBDD... bdds) {
        int idGenerator = CustomBDDItem.DEFAULT_NEXT_ITEM_KEY;
        if (bdds != null && bdds.length > 0) {
            for (CustomBDD bdd : bdds) {
                Map<Integer, CustomBDDItem> items = bdd.bddMap;
                Map<Integer, CustomBDDItem> newItems = new HashMap<>();
                Map<Integer, Integer> idsMapping = new HashMap<>();
                CustomBDDItem zeroItem = items.get(CustomBDDItem.DEFAULT_ZERO_ITEM_KEY);
                CustomBDDItem oneItem = items.get(CustomBDDItem.DEFAULT_ONE_ITEM_KEY);
                idsMapping.put(CustomBDDItem.DEFAULT_ZERO_ITEM_KEY, CustomBDDItem.DEFAULT_ZERO_ITEM_KEY);
                idsMapping.put(CustomBDDItem.DEFAULT_ONE_ITEM_KEY, CustomBDDItem.DEFAULT_ONE_ITEM_KEY);
                if (zeroItem != null) {
                    newItems.put(CustomBDDItem.DEFAULT_ZERO_ITEM_KEY, new CustomBDDItem(zeroItem));   
                }
                if (oneItem != null) {
                    newItems.put(CustomBDDItem.DEFAULT_ONE_ITEM_KEY, new CustomBDDItem(oneItem));
                }
                for (Entry<Integer, CustomBDDItem> item : items.entrySet()) {
                    if (!item.getKey().equals(CustomBDDItem.DEFAULT_ONE_ITEM_KEY)
                            && !item.getKey().equals(CustomBDDItem.DEFAULT_ZERO_ITEM_KEY))
                    {
                        CustomBDDItem nextItem = new CustomBDDItem(item.getValue());
                        newItems.put(idGenerator, nextItem);
                        idsMapping.put(item.getKey(), idGenerator);
                        idGenerator++;
                    }
                }
                for (CustomBDDItem newItem : newItems.values()) {
                    Integer parentLink = newItem.getParentLink();
                    Integer oneLink = newItem.getOneLink();
                    Integer zeroLink = newItem.getZeroLink();
                    if (parentLink != null) {
                        newItem.setParentLink(idsMapping.get(parentLink));
                    }
                    if (oneLink != null) {
                        newItem.setOneLink(idsMapping.get(oneLink));
                    }
                    if (zeroLink != null) {
                        newItem.setZeroLink(idsMapping.get(zeroLink));
                    }
                    List<Integer> additionalParents = newItem.getAdditionalParents();
                    if (additionalParents != null && !additionalParents.isEmpty()) {
                        for (Integer i = 0; i < additionalParents.size(); i++) {
                            additionalParents.set(i, idsMapping.get(additionalParents.get(i)));
                        }
                    }
                }
                bdd.setBddMap(newItems, false);
            }
        }
        return idGenerator;
    }
    
    private boolean reduced = false;
    
    private void reduceVar() {
        if (!reduced) {
            //we reduce the OBDD only once, because it's partly-immutable
            Integer headVar = var();
            if (headVar >= 0) {//the OBDD is not a constant
                CustomBDDItem headItem = usedVariables.get(headVar).get(0);
                CustomBDDItem childItem = bddMap.get(headItem.getZeroLink());
                Integer headItemIndex = childItem.getParentLink();
                if (headItemIndex == null) {
                    childItem = bddMap.get(headItem.getOneLink());
                    headItemIndex = childItem.getParentLink();
                }
                if (headItemIndex == null) {
                    for (Integer parent : childItem.getAdditionalParents()) {
                        CustomBDDItem nextItem = bddMap.get(parent);
                        if (nextItem == headItem) {
                            headItemIndex = parent;
                            break;
                        }
                    }
                }

                //creating two children OBDDs of the head vertex
                Map<Integer, CustomBDDItem> zeroChildBddMap = new HashMap<>(bddMap);
                Map<Integer, CustomBDDItem> oneChildBddMap = new HashMap<>(bddMap);
                zeroChildBddMap.put(CustomBDDItem.DEFAULT_ZERO_ITEM_KEY,
                        new CustomBDDItem(bddMap.get(CustomBDDItem.DEFAULT_ZERO_ITEM_KEY)));
                zeroChildBddMap.put(CustomBDDItem.DEFAULT_ONE_ITEM_KEY,
                        new CustomBDDItem(bddMap.get(CustomBDDItem.DEFAULT_ONE_ITEM_KEY)));
                //removing the old head
                zeroChildBddMap.remove(headItemIndex);
                deleteAllChildren(headItem.getZeroLink(), headItemIndex, zeroChildBddMap);
                CustomBDDItem oneItem = new CustomBDDItem(zeroChildBddMap.get(headItem.getOneLink()));
                zeroChildBddMap.put(headItem.getOneLink(), oneItem);
                oneItem.setParentLink(null);
                oneItem.removeAdditionalParent(headItemIndex);
                
                oneChildBddMap.put(CustomBDDItem.DEFAULT_ZERO_ITEM_KEY,
                        new CustomBDDItem(bddMap.get(CustomBDDItem.DEFAULT_ZERO_ITEM_KEY)));
                oneChildBddMap.put(CustomBDDItem.DEFAULT_ONE_ITEM_KEY,
                        new CustomBDDItem(bddMap.get(CustomBDDItem.DEFAULT_ONE_ITEM_KEY)));
                //removing the old head
                oneChildBddMap.remove(headItemIndex);
                deleteAllChildren(headItem.getOneLink(), headItemIndex, oneChildBddMap);
                CustomBDDItem zeroItem = new CustomBDDItem(oneChildBddMap.get(headItem.getZeroLink()));
                oneChildBddMap.put(headItem.getZeroLink(), zeroItem);
                zeroItem.setParentLink(null);
                zeroItem.removeAdditionalParent(headItemIndex);
                
                CustomBDD zeroChildBdd = new CustomBDD(zeroChildBddMap, factory);
                CustomBDD oneChildBdd = new CustomBDD(oneChildBddMap, factory);
                if (zeroChildBdd.equals(oneChildBdd)) {
                    //two immediate child-OBDDs are equal; so, the OBDD does not
                    //depends on the head variable
                    setBddMap(zeroChildBddMap, false);
                }
            }//we do not to reduce the constant OBDD
            reduced = true;
        }
    }
    
    /**
     *
     * @param variable variable to replace with given value
     * @param value boolean value to replace given variable with
     * @return the result of replacing
     */
    public CustomBDD replaceVariableWithValue(int variable, boolean value) {
        Map<Integer, CustomBDDItem> newBddMap = new HashMap<>(this.bddMap);
        List<CustomBDDItem> items = this.usedVariables.get(variable);
        if (items == null || items.isEmpty()) {//the OBDD is a constant
            return new CustomBDD(this);
        }
        for (CustomBDDItem item : items) {
            //replacing all items having the given variable with the given value
            Integer necessaryLink = value ? item.getOneLink() : item.getZeroLink();
            Integer unnecessaryLink = value ? item.getZeroLink() : item.getOneLink();
            CustomBDDItem parentItem = newBddMap.get(item.getParentLink());
            CustomBDDItem necessaryChildItem = newBddMap.get(necessaryLink);
            
            Integer itemLink = necessaryChildItem.getParentLink();
            if (itemLink == null) {
                List<Integer> terminalParents = necessaryChildItem.getAdditionalParents();
                if (terminalParents != null && !terminalParents.isEmpty()) {
                    for (Integer terminalParent : terminalParents) {
                        if (newBddMap.get(terminalParent) == item) {
                            itemLink = terminalParent;
                            break;
                        }
                    }
                }
            }
            newBddMap.remove(itemLink);
            
            //deleting all unnecessary vertexes
            deleteAllChildren(unnecessaryLink, itemLink, newBddMap);
            CustomBDDItem newNecessaryChildItem = new CustomBDDItem(newBddMap.get(necessaryLink));
            newNecessaryChildItem.setParentLink(item.getParentLink());
            List<Integer> childAdditionalParents = newNecessaryChildItem.getAdditionalParents();
            if (childAdditionalParents != null) {
                newNecessaryChildItem.setParentLink(null);
                newNecessaryChildItem.setAdditionalParents(childAdditionalParents);
                if (parentItem != null) {
                    newNecessaryChildItem.replaceAdditionalParent(
                            itemLink, item.getParentLink());
                }
                else {
                    newNecessaryChildItem.removeAdditionalParent(itemLink);
                }
            }
            newBddMap.put(necessaryLink, newNecessaryChildItem);
            
            if (parentItem != null) {
                CustomBDDItem newParentItem = new CustomBDDItem(
                        parentItem.getZeroLink().equals(itemLink) ? necessaryLink : parentItem.getZeroLink(), 
                        parentItem.getOneLink().equals(itemLink) ? necessaryLink : parentItem.getOneLink(), 
                        parentItem.getVariable(), 
                        parentItem.getParentLink());
                newBddMap.put(item.getParentLink(), newParentItem);
            }
        }
        
        if (!newBddMap.containsKey(CustomBDDItem.DEFAULT_ZERO_ITEM_KEY)) {
            //then it is just a constant OBDD
            CustomBDDItem item = newBddMap.get(CustomBDDItem.DEFAULT_ONE_ITEM_KEY);
            if (CustomBDDItem.DEFAULT_ZERO_ITEM_KEY.equals(item.getZeroLink())) {
                return (CustomBDD) factory.zero();
            }
            else {
                return (CustomBDD) factory.one();
            }
        }
        if (!newBddMap.containsKey(CustomBDDItem.DEFAULT_ONE_ITEM_KEY)) {
            //then it is just a constant OBDD
            CustomBDDItem item = newBddMap.get(CustomBDDItem.DEFAULT_ZERO_ITEM_KEY);
            if (CustomBDDItem.DEFAULT_ONE_ITEM_KEY.equals(item.getZeroLink())) {
                return (CustomBDD) factory.one();
            }
            else {
                return (CustomBDD) factory.zero();
            }
        }
        //constructing and returning new OBDD
        return new CustomBDD(newBddMap, this.factory);
    }
    
    private void deleteAllChildren(Integer itemNumber, Integer itemParentNumber, 
            Map<Integer, CustomBDDItem> map) {
        //get item to delete
        CustomBDDItem item = map.get(itemNumber);
        if (item != null) {
            if (item.getVariable() != null) {//the item is not a terminal vertex
                map.remove(itemNumber);
                deleteAllChildren(item.getZeroLink(), itemNumber, map);
                deleteAllChildren(item.getOneLink(), itemNumber, map);
            }
            else {//the item is a non-terminal vertex
                item = new CustomBDDItem(item);
                map.put(itemNumber, item);
                item.removeAdditionalParent(itemParentNumber);
                List<Integer> additionalParents = item.getAdditionalParents();
                if (additionalParents == null || additionalParents.isEmpty()) {
                    map.remove(itemNumber);
                }
            }
        }
    }

    @Override
    public BDD applyWith(BDD bdd, BDDFactory.BDDOp bddop) {
        this.setBddMap(((CustomBDD)apply(bdd, bddop)).bddMap, true);
        return this;
    }

    @Override
    public BDD applyAll(BDD bdd, BDDFactory.BDDOp bddop, BDD bdd1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BDD applyEx(BDD bdd, BDDFactory.BDDOp bddop, BDD bdd1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BDD applyUni(BDD bdd, BDDFactory.BDDOp bddop, BDD bdd1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BDD satOne() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BDD fullSatOne() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BDD satOne(BDD bdd, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List allsat() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BDD replace(BDDPairing bddp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BDD replaceWith(BDDPairing bddp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int nodeCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double pathCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double satCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int[] varProfile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean equals(BDD bdd) {
        return this.equals((Object)bdd);
    }

    @Override
    public int hashCode() {
        return bddMap.hashCode();
    }

    @Override
    public void free() {
        bddMap.clear();
        initialize(true);
    }
    
    protected Map<Integer, List<CustomBDDItem>> getUsedVariables() {
        return usedVariables;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CustomBDD other = (CustomBDD) obj;
        return toString().equals(other.toString());
    }

    @Override
    public CustomBDD clone() throws CloneNotSupportedException {
        return new CustomBDD(new HashMap<>(bddMap), factory);
    }
    
    private List<String> subresults;
    private String result;

    @Override
    public String toString() {
        if (result == null) {
            if (bddMap == null || bddMap.isEmpty()) {
                return EMPTY_BDD;
            }
            if (bddMap.size() == 1) {
                CustomBDDItem item = bddMap.values().iterator().next();
                if (item.getOneLink().equals(CustomBDDItem.ONE_ITEM.getOneLink())
                        && item.getZeroLink().equals(CustomBDDItem.ONE_ITEM.getZeroLink())) {
                    return ONE_BDD;
                }
                else {
                    return ZERO_BDD;
                }
            }
            Integer theSmallestVariable = var();
            result = "";
            String firstSubresult = OPEN_BRACKET;
            subresults = toSubString(usedVariables.get(theSmallestVariable).get(0), firstSubresult);
            for (String item : subresults) {
                if (item.endsWith(CLOSE_BRACKET)) {
                    result = result + item;
                }
            }
            subresults.clear();
        }
        return result;
    }
    
    private List<String> toSubString(CustomBDDItem item, String subresult) {
        if (item.getVariable() == null) {
            List<String> iterationResult = new ArrayList<>();
            if (item.getOneLink().equals(CustomBDDItem.DEFAULT_ONE_ITEM_KEY)
                    && item.getZeroLink().equals(CustomBDDItem.DEFAULT_ONE_ITEM_KEY)) {
                subresult = subresult + CLOSE_BRACKET;
                iterationResult.add(subresult);
                return iterationResult;
            }
            else {
                iterationResult.add("");
                return iterationResult;
            }
        }
        String inputForZeroChildren, inputForOneChildren;
        if (subresult.equals(OPEN_BRACKET)) {
            inputForZeroChildren = subresult + item.getVariable() + 
                    COLON_SEPARATOR + 0;
            inputForOneChildren = subresult + item.getVariable() + 
                    COLON_SEPARATOR + 1;
        }
        else {
            inputForZeroChildren = subresult + COMMA_SEPARATOR + item.getVariable() + COLON_SEPARATOR + 0;
            inputForOneChildren = subresult + COMMA_SEPARATOR + item.getVariable() + COLON_SEPARATOR + 1;
        }
        List<String> zeroChildResult = toSubString(bddMap.get(item.getZeroLink()), inputForZeroChildren);
        List<String> oneChildResult = toSubString(bddMap.get(item.getOneLink()), inputForOneChildren);
        zeroChildResult.addAll(oneChildResult);
        return zeroChildResult;
    }
    
}
