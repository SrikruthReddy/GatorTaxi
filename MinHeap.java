class MinHeap {
    private MinHeapNode[] heap = new MinHeapNode[2001];
    //Getters
    public MinHeapNode[] getHeap() {
        return heap;
    }
    //Setters
    public void setHeap(MinHeapNode[] heap) {
        this.heap = heap;
    }
    int size = 0;
    public void insert(MinHeapNode node) {
        heap[size] = node;
        node.getCorrespondingRBTNode().setCorrespondingMinHeapNodeIndex(size);
        size++;
        int i = size - 1;
        //heapifyUp
        while (i > 0 && compare(heap[i], heap[(i - 1) / 2])) {
            MinHeapNode temp = heap[i];
            heap[i] = heap[(i - 1) / 2];
            heap[i].getCorrespondingRBTNode().setCorrespondingMinHeapNodeIndex(i);
            heap[(i - 1) / 2] = temp;
            heap[(i - 1) / 2].getCorrespondingRBTNode().setCorrespondingMinHeapNodeIndex((i - 1) / 2);
            i = (i - 1) / 2;
        }
    }
    public MinHeapNode removeMin() {
        if(size == 0)
            return null;
        MinHeapNode min = heap[0];
        heap[0] = heap[size - 1];
        heap[0].getCorrespondingRBTNode().setCorrespondingMinHeapNodeIndex(0);
        size--;
        heapify(0);
        return min;
    }
    public MinHeapNode arbitraryRemove(int index) {
        if(size == 0)
            return null;
        MinHeapNode nodeToBeDeleted = heap[index];
        if(index == size - 1) {
            size--;
            return nodeToBeDeleted;
        }
        heap[index] = heap[size - 1];
        heap[index].getCorrespondingRBTNode().setCorrespondingMinHeapNodeIndex(index);
        size--;
        heapify(index);
        return nodeToBeDeleted;
    }
    //heapifyDown
    public void heapify(int i) {
        int smallest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        if (left < size && compare(heap[left], heap[smallest])) {
            smallest = left;
        }
        if (right < size && compare(heap[right], heap[smallest])) {
            smallest = right;
        }
        if (smallest != i) {
            MinHeapNode temp = heap[i];
            heap[i] = heap[smallest];
            heap[i].getCorrespondingRBTNode().setCorrespondingMinHeapNodeIndex(i);
            heap[smallest] = temp;
            heap[smallest].getCorrespondingRBTNode().setCorrespondingMinHeapNodeIndex(smallest);
            heapify(smallest);
        }
    }
    //update the trip and heapify up of down depending on the circumstances
    public void updateTrip(int i) {
        while(i > 0 && compare(heap[i], heap[(i - 1) / 2])) {
            MinHeapNode temp = heap[i];
            heap[i] = heap[(i - 1) / 2];
            heap[i].getCorrespondingRBTNode().setCorrespondingMinHeapNodeIndex(i);
            heap[(i - 1) / 2] = temp;
            heap[(i - 1) / 2].getCorrespondingRBTNode().setCorrespondingMinHeapNodeIndex((i - 1) / 2);
            i = (i - 1) / 2;
        }
        heapify(i);
    }
    //compare 2 nodes
    public boolean compare(MinHeapNode node1, MinHeapNode node2) {
        if(node1.getRideCost() < node2.getRideCost()) {
            return true;
        }
        else if(node1.getRideCost() == node2.getRideCost()) {
            if(node1.getTripDuration() < node2.getTripDuration()) {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }
    public void print() {
        for(int i = 0; i < size; i++) {
            System.out.print(heap[i].getRideNumber() + " ");
        }
        System.out.println();
    }
}