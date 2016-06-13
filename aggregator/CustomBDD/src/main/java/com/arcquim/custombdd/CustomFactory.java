package com.arcquim.custombdd;

import java.io.PrintStream;
import java.lang.ref.WeakReference;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDBitVector;
import net.sf.javabdd.BDDDomain;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.BDDPairing;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class CustomFactory extends BDDFactory {
    
    public static String CUSTOM_FACTORY_CLASS = "com.arcquim.custombdd.CustomFactory";
    
    private static final Map<Integer, CustomBDDItem> MAP_FOR_ZERO_BDD;
    private static final Map<Integer, CustomBDDItem> MAP_FOR_ONE_BDD;
    private final List<WeakReference<CustomBDD>> bddsOfFactory = new LinkedList<>();
    
    static {
        MAP_FOR_ZERO_BDD = new HashMap<>();
        MAP_FOR_ZERO_BDD.put(CustomBDDItem.DEFAULT_ZERO_ITEM_KEY, CustomBDDItem.ZERO_ITEM);
        
        MAP_FOR_ONE_BDD = new HashMap<>();
        MAP_FOR_ONE_BDD.put(CustomBDDItem.DEFAULT_ONE_ITEM_KEY, CustomBDDItem.ONE_ITEM);
    }
    
    public final CustomBDD zero = new CustomBDD(MAP_FOR_ZERO_BDD, this);
    public final CustomBDD one = new CustomBDD(MAP_FOR_ONE_BDD, this);
    
    private final List<CustomBDD> defaultIthVariables = new LinkedList<>();
    private final List<CustomBDD> defaultNithVariables = new LinkedList<>();
    
    private boolean isInitialized = false;
    private int numberOfVariables;
    private int cacheSize;
    
    private PrintStream printStream = System.out;
    private Map<Integer, Integer> variablesOrderMapping = new HashMap<>();
            
    @Override
    public BDD zero() {
        return new CustomBDD(zero);
    }

    @Override
    public BDD one() {
        return new CustomBDD(one);
    }

    @Override
    protected void initialize(int numberOfVariables, int cacheSize) {
        this.numberOfVariables = numberOfVariables;
        this.cacheSize = cacheSize;
        createDefaultVariables();
        isInitialized = true;
    }
    
    public void setPrintStream(PrintStream printStream) {
        if (printStream != null) {
            this.printStream = printStream;
        }
    }
    
    private void createDefaultVariables() {
        defaultIthVariables.clear();
        defaultNithVariables.clear();
        variablesOrderMapping.clear();
        for (int i = 0; i < numberOfVariables; i++) {
            Map<Integer, CustomBDDItem> nextIthBddMap = new HashMap<>();
            CustomBDDItem nextIthItem = new CustomBDDItem(0, 1, i, null);
            nextIthBddMap.put(CustomBDDItem.DEFAULT_ZERO_ITEM_KEY, 
                    new CustomBDDItem(CustomBDDItem.ZERO_ITEM).addAdditionalParent(CustomBDDItem.DEFAULT_NEXT_ITEM_KEY));
            nextIthBddMap.put(CustomBDDItem.DEFAULT_ONE_ITEM_KEY, 
                    new CustomBDDItem(CustomBDDItem.ONE_ITEM).addAdditionalParent(CustomBDDItem.DEFAULT_NEXT_ITEM_KEY));
            nextIthBddMap.put(CustomBDDItem.DEFAULT_NEXT_ITEM_KEY, nextIthItem);
            CustomBDD nextBdd = new CustomBDD(nextIthBddMap, this);
            defaultIthVariables.add(nextBdd);
            
            Map<Integer, CustomBDDItem> nextNithBddMap = new HashMap<>();
            CustomBDDItem nextNithItem = new CustomBDDItem(1, 0, i, null);
            nextNithBddMap.put(CustomBDDItem.DEFAULT_ZERO_ITEM_KEY,
                    new CustomBDDItem(CustomBDDItem.ZERO_ITEM).addAdditionalParent(CustomBDDItem.DEFAULT_NEXT_ITEM_KEY));
            nextNithBddMap.put(CustomBDDItem.DEFAULT_ONE_ITEM_KEY,
                    new CustomBDDItem(CustomBDDItem.ONE_ITEM).addAdditionalParent(CustomBDDItem.DEFAULT_NEXT_ITEM_KEY));
            nextNithBddMap.put(CustomBDDItem.DEFAULT_NEXT_ITEM_KEY, nextNithItem);
            nextBdd = new CustomBDD(nextNithBddMap, this);
            defaultNithVariables.add(nextBdd);
            
            variablesOrderMapping.put(i, i);
        }
    }

    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public void done() {
        
    }

    @Override
    public void setError(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clearError() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int setMaxNodeNum(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double setMinFreeNodes(double d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int setMaxIncrease(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double setIncreaseFactor(double d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double setCacheRatio(double d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int setNodeTableSize(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int setCacheSize(int i) {
        this.cacheSize = i;
        return this.cacheSize;
    }

    @Override
    public int varNum() {
        return numberOfVariables;
    }

    @Override
    public int setVarNum(int i) {
        if (i < numberOfVariables) {
            throw new IllegalArgumentException("New variables number " + i + " cannot be more than "
                    + "previous number, " + numberOfVariables);
        }
        int oldNumberOfVariables = numberOfVariables;
        numberOfVariables = i;
        createDefaultVariables();
        isInitialized = true;
        return oldNumberOfVariables;
    }

    @Override
    public BDD ithVar(int i) {
        if (isVariableAllowed(i)) {
            return new CustomBDD(defaultIthVariables.get(i));
        }
        else {
            throw new IllegalArgumentException("The variable with number " 
                    + i + " is not legal for this fatory");
        }
    }

    @Override
    public BDD nithVar(int i) {
        if (isVariableAllowed(i)) {
            return new CustomBDD(defaultNithVariables.get(i));
        }
        else {
            throw new IllegalArgumentException("The variable with number " 
                    + i + " is not legal for this factory");
        }
    }
    
    private boolean isVariableAllowed(int i) {
        return i >= 0 && i < numberOfVariables;
    }

    @Override
    public void printAll() {
        for (WeakReference<CustomBDD> bdd : bddsOfFactory) {
            printTable(bdd.get());
        }
    }

    @Override
    public void printTable(BDD bdd) {
        printStream.println(bdd.toString());
    }

    @Override
    public int level2Var(int i) {
        for (Entry<Integer, Integer> entry : variablesOrderMapping.entrySet()) {
            if (entry.getValue().equals(i)) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("There is no level " + i);
    }

    @Override
    public int var2Level(int i) {
        if (variablesOrderMapping.containsKey(i)) {
            return variablesOrderMapping.get(i);
        }
        throw new IllegalArgumentException("There is no variable " + i);
    }
    
    public void addToCollection(CustomBDD customBDD) {
        bddsOfFactory.add(new WeakReference<>(customBDD));
    }

    @Override
    public void reorder(ReorderMethod rm) {
    }

    @Override
    public void autoReorder(ReorderMethod rm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void autoReorder(ReorderMethod rm, int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ReorderMethod getReorderMethod() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getReorderTimes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void disableReorder() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void enableReorder() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int reorderVerbose(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setVarOrder(int[] ints) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BDDPairing makePair() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void swapVar(int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int duplicateVar(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addVarBlock(BDD bdd, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addVarBlock(int i, int i1, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void varBlockAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clearVarBlocks() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void printOrder() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getVersion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int nodeCount(Collection clctn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getNodeTableSize() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getNodeNum() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getCacheSize() {
        return cacheSize;
    }

    @Override
    public int reorderGain() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void printStat() {
        
    }

    @Override
    protected BDDDomain createDomain(int i, BigInteger bi) {
        return new CustomBDDDomain(i, bi).setBDDFactory(this);
    }

    @Override
    protected BDDBitVector createBitVector(int i) {
        return new CustomBDDBitVector(i).setFactory(this);
    }

    public Map<Integer, Integer> getVariablesOrderMapping() {
        return variablesOrderMapping;
    }
    
    protected Map<String, CustomBDD> currentAndCache = new HashMap<>();
    protected Map<String, CustomBDD> currentOrCache = new HashMap<>();
    protected Map<String, CustomBDD> currentXorCache = new HashMap<>();
    
}
