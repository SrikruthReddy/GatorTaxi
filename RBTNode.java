class  RBTNode extends RideNode {
    private boolean isRed;
    private RBTNode leftChild;
    private RBTNode rightChild;
    private RBTNode parent;
    private int correspondingMinHeapNodeIndex;
    RBTNode(int rideNumber, int rideCost, int tripDuration) {
        super(rideNumber, rideCost, tripDuration);
        this.isRed = true;
        this.leftChild = null;
        this.rightChild = null;
        this.parent = null;
    }

    // Getters
    public boolean getIsRed() {
        return isRed;
    }
    public RBTNode getLeftChild() {
        return leftChild;
    }
    public RBTNode getRightChild() {
        return rightChild;
    }
    public RBTNode getParent() {
        return parent;
    }
    // Setters
    public void setIsRed(boolean isRed) {
        this.isRed = isRed;
    }
    public void setLeftChild(RBTNode leftChild) {
        this.leftChild = leftChild;
    }
    public void setRightChild(RBTNode rightChild) {
        this.rightChild = rightChild;
    }
    public void setParent(RBTNode parent) {
        this.parent = parent;
    }
    public int getCorrespondingMinHeapNodeIndex() {
        return correspondingMinHeapNodeIndex;
    }
    public void setCorrespondingMinHeapNodeIndex(int correspondingMinHeapNodeIndex) {
        this.correspondingMinHeapNodeIndex = correspondingMinHeapNodeIndex;
    }
}