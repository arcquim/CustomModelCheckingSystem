package com.arcquim.custombdd;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
class CustomBDDItem {
    
    public static final CustomBDDItem ZERO_ITEM = new CustomBDDItem(0, 0, null, null);
    public static final CustomBDDItem ONE_ITEM = new CustomBDDItem(1, 1, null, null);
    
    public static final Integer DEFAULT_ZERO_ITEM_KEY = 0;
    public static final Integer DEFAULT_ONE_ITEM_KEY = 1;
    public static final Integer DEFAULT_NEXT_ITEM_KEY = 2;
    
    private Integer zeroLink;
    private Integer oneLink;
    private Integer variable;
    private Integer parentLink;
    private List<Integer> additionalParents;
    
    public CustomBDDItem(Integer zeroLink, Integer oneLink, Integer variable, Integer parentLink) {
        this.oneLink = oneLink;
        this.zeroLink = zeroLink;
        this.variable = variable;
        this.parentLink = parentLink;
    }
    
    public CustomBDDItem(CustomBDDItem item) {
        this.zeroLink = item.zeroLink;
        this.oneLink = item.oneLink;
        this.variable = item.variable;
        this.parentLink = item.parentLink;
        if (item.additionalParents != null) {
            this.additionalParents = new ArrayList<>(item.additionalParents);
        }
    }

    public Integer getZeroLink() {
        return zeroLink;
    }

    public void setZeroLink(Integer zeroLink) {
        this.zeroLink = zeroLink;
    }

    public Integer getOneLink() {
        return oneLink;
    }

    public void setOneLink(Integer oneLink) {
        this.oneLink = oneLink;
    }

    public Integer getVariable() {
        return variable;
    }

    public void setVariable(Integer variable) {
        this.variable = variable;
    }

    public Integer getParentLink() {
        return parentLink;
    }

    public void setParentLink(Integer parentLink) {
        this.parentLink = parentLink;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CustomBDDItem other = (CustomBDDItem) obj;
        boolean variablesEqual = this.variable == null ? 
                other.variable == null : this.variable.equals(other.variable);
        boolean zeroLinksEqual = this.zeroLink == null ?
                other.zeroLink == null : this.zeroLink.equals(other.zeroLink);
        boolean oneLinksEqual = this.oneLink == null ?
                other.oneLink == null : this.oneLink.equals(other.oneLink);
        boolean parentLinksEqual = this.parentLink == null ?
                other.parentLink == null : this.parentLink.equals(other.parentLink);
        return variablesEqual && zeroLinksEqual && oneLinksEqual && parentLinksEqual;
    }
    
    public CustomBDDItem addAdditionalParent(Integer additionalParentLink) {
        if (additionalParents == null) {
            additionalParents = new ArrayList<>();
        }
        additionalParents.add(additionalParentLink);
        return this;
    }
    
    public CustomBDDItem removeAdditionalParent(Integer additionalParentLink) {
        if (additionalParents != null) {
            additionalParents.remove(additionalParentLink);
        }
        return this;
    }
    
    public CustomBDDItem replaceAdditionalParent(Integer oldAdditionalParent, Integer newAdditionalParent) {
        if (additionalParents != null) {
            int oldParentIndex = additionalParents.indexOf(oldAdditionalParent);
            if (oldParentIndex >= 0) {
                additionalParents.set(oldParentIndex, newAdditionalParent);
            }
        }
        return this;
    }
    
    public List<Integer> getAdditionalParents() {
        return additionalParents;
    }
    
    public void setAdditionalParents(List<Integer> additionalParents) {
        if (additionalParents == null) {
            this.additionalParents = additionalParents;
        }
        else {
            this.additionalParents = new ArrayList<>(additionalParents);
        }
    }
    
}
