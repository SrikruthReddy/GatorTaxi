class MinHeapNode extends RideNode {
    private RBTNode correspondingRBTNode;
    MinHeapNode(int rideNumber, int rideCost, int tripDuration) {
        super(rideNumber, rideCost, tripDuration);
        this.correspondingRBTNode = null;
    }
    // Getters
    public RBTNode getCorrespondingRBTNode() {
        return correspondingRBTNode;
    }
    // Setters
    public void setCorrespondingRBTNode(RBTNode correspondingRBTNode) {
        this.correspondingRBTNode = correspondingRBTNode;
    }
}