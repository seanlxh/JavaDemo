package javaDemo.model;

public class Tree {

    public String function;
    public int floor;
    public Tree child;
    public Tree brother;
    public Tree parent;

    public Tree(){
        floor = 0;
        function = "";
        child = null;
        brother = null;
        parent = null;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Tree getChild() {
        return child;
    }

    public void setChild(Tree child) {
        this.child = child;
    }

    public Tree getBrother() {
        return brother;
    }

    public void setBrother(Tree brother) {
        this.brother = brother;
    }

    public Tree getParent() {
        return parent;
    }

    public void setParent(Tree parent) {
        this.parent = parent;
    }

    public boolean equal(Tree obj) {
        boolean b1 = (function.equals(obj.getFunction()) && floor == obj.getFloor());
        return b1;
    }
}
