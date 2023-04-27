import java.io.*;
class RBT {
    private RBTNode root;
    //Getter
    public RBTNode getRoot() {
        return root;
    }
    //Setter
    public void setRoot(RBTNode root) {
        this.root = root;
    }
    //Constructor
    RBT() {
        this.root = null;
    }
    //Methods
    public void insert(RBTNode newNode)
    {
        if(root == null) {
            root = newNode;
            return;
        }
        RBTNode curr = root;
        //Inserting the node in RBT
        while(curr != null) {
            if(curr.getRideNumber() < newNode.getRideNumber()) {
                if(curr.getRightChild() != null) {
                    curr = curr.getRightChild();
                }
                else {
                    curr.setRightChild(newNode);
                    newNode.setParent(curr);
                    break;
                }
            }
            else {
                if(curr.getLeftChild() != null) {
                    curr = curr.getLeftChild();
                }
                else {
                    curr.setLeftChild(newNode);
                    newNode.setParent(curr);
                    break;
                }
            }
        }
        //Check if tree is balanced, else rebalance
        if(newNode.getIsRed() && newNode.getParent().getIsRed() && newNode.getParent().getParent() != null) {
            rebalanceRBTAfterInsert(newNode, newNode.getParent(), newNode.getParent().getParent());
        }
        else if(newNode.getParent().getIsRed()) {
            newNode.getParent().setIsRed(false);
        }
    }
    public RBTNode delete(RBTNode nodeToBeDeleted, MinHeap minHeap) {
        // Search for the node
        if(nodeToBeDeleted == null) {
            return null;
        }
        // Delete the node
        RBTNode siblingOfActuallyDeletedNode = deleteAsIfItIsAStandardBST(nodeToBeDeleted, minHeap);
        if(siblingOfActuallyDeletedNode == null) {
            return nodeToBeDeleted;
        }
        RBTNode py = siblingOfActuallyDeletedNode.getParent();
        RBTNode y = getOtherChild(siblingOfActuallyDeletedNode, py);
        // Rebalance the tree if needed
        rebalanceRBTAfterDelete(y, py);
        return nodeToBeDeleted;
    }
    public RBTNode search(int rideNumber) {
        RBTNode curr = root;
        while(curr != null) {
            if(curr.getRideNumber() == rideNumber) {
                return curr;
            }
            else if(curr.getRideNumber() < rideNumber) {
                curr = curr.getRightChild();
            }
            else {
                curr = curr.getLeftChild();
            }
        }
        return null;
    }
    //Deletes the node as in a standard non-balanced BST and returns the sibling of the root of the deficient subtree.
    public RBTNode deleteAsIfItIsAStandardBST(RBTNode node, MinHeap minHeap) {
        //If the node is a leaf
        if(node.getLeftChild() == null && node.getRightChild() == null) {
            if(root == node) {
                root = null;
                return null;
            }
            else {
                if(node.getParent().getLeftChild() == node) {
                    node.getParent().setLeftChild(null);
                }
                else {
                    node.getParent().setRightChild(null);
                }
            }
            if(node.getIsRed() == true) {
                return null;
            }
            else {
                if(node.getParent().getLeftChild() == null) {
                    return node.getParent().getRightChild();
                }
                else {
                    return node.getParent().getLeftChild();
                }
            }
        }
        //If node is a 1-degree node
        else if(node.getLeftChild() == null || node.getRightChild() == null) {
            if(root == node) {
                if(node.getLeftChild() == null) {
                    root = node.getRightChild();
                    root.setParent(null);
                }
                else {
                    root = node.getLeftChild();
                    root.setParent(null);
                }
                return null;
            }
            else {
                RBTNode sibling = getOtherChild(node, node.getParent());
                if(node.getLeftChild() != null) {
                    if(node.getParent().getLeftChild() == node) {
                        node.getParent().setLeftChild(node.getLeftChild());
                    }
                    else {
                        node.getParent().setRightChild(node.getLeftChild());
                    }
                    node.getLeftChild().setParent(node.getParent());
                    if(node.getLeftChild().getIsRed()) {
                        node.getLeftChild().setIsRed(false);
                        return null;
                    }
                }
                else {
                    if(node.getParent().getLeftChild() == node) {
                        node.getParent().setLeftChild(node.getRightChild());
                    }
                    else {
                        node.getParent().setRightChild(node.getRightChild());
                    }
                    node.getRightChild().setParent(node.getParent());
                    if(node.getRightChild().getIsRed()) {
                        node.getRightChild().setIsRed(false);
                        return null;
                    }
                }

                if(node.getIsRed()) {
                    return null;
                }
                else {
                    return sibling;
                }
            }
        }
        //If node is a 2-degree node
        else {
            RBTNode rightMostNodeInLeftSubtree = node.getLeftChild();
            while(rightMostNodeInLeftSubtree.getRightChild() != null) {
                rightMostNodeInLeftSubtree = rightMostNodeInLeftSubtree.getRightChild();
            }
            // Replace data in node with that of rightMostNodeInLeftSubtree
            node.setRideCost(rightMostNodeInLeftSubtree.getRideCost());
            node.setRideNumber(rightMostNodeInLeftSubtree.getRideNumber());
            node.setTripDuration(rightMostNodeInLeftSubtree.getTripDuration());
            minHeap.getHeap()[rightMostNodeInLeftSubtree.getCorrespondingMinHeapNodeIndex()].setCorrespondingRBTNode(node);
            node.setCorrespondingMinHeapNodeIndex(rightMostNodeInLeftSubtree.getCorrespondingMinHeapNodeIndex());

            // Delete rightMostNodeInLeftSubtree
            RBTNode siblingOfTheRootOfTheDeficientSubtree = deleteAsIfItIsAStandardBST(rightMostNodeInLeftSubtree, minHeap);
            return siblingOfTheRootOfTheDeficientSubtree;
        }
    }
    // Rebalancing after insert has 5 cases as explained by professor in class
    public void rebalanceRBTAfterInsert(RBTNode p, RBTNode pp, RBTNode gp) {
        RBTNode z = getOtherChild(pp, gp);
        //Case 1 : XYr
        if(z != null && z.getIsRed() == true) {
            pp.setIsRed(false);
            gp.setIsRed(true);
            z.setIsRed(false);
            p = gp;
            pp = gp.getParent();
            if(pp != null) {
                gp = pp.getParent();
                if(gp != null) {
                    rebalanceRBTAfterInsert(p, pp, gp);
                }
                else {
                    pp.setIsRed(false);
                }
            }
            else {
                p.setIsRed(false);
            }
        }
        //Other 4 cases LLb, LRb, RLb, and RRb
        else {
            //LLb
            if(gp.getLeftChild() == pp && pp.getLeftChild() == p) {
                rotateLL(p, pp, gp);
                pp.setIsRed(false);
                gp.setIsRed(true);
            }
            //LRb
            else if(gp.getLeftChild() == pp && pp.getLeftChild() != p) {
                rotateLR(p, pp, gp);
                p.setIsRed(false);
                gp.setIsRed(true);
            }
            //RLb
            else if(gp.getLeftChild() != pp && pp.getLeftChild() == p)
            {
                rotateRL(p, pp, gp);
                p.setIsRed(false);
                gp.setIsRed(true);
            }
            //RRb
            else {
                rotateRR(p, pp, gp);
                pp.setIsRed(false);
                gp.setIsRed(true);
            }
        }
    }
    public void rebalanceRBTAfterDelete(RBTNode rootOfTheDeficientSubtree, RBTNode parentOfTheRootOfTheDeficientSubtree) {
        if(rootOfTheDeficientSubtree != null && rootOfTheDeficientSubtree.getIsRed()) {
            rootOfTheDeficientSubtree.setIsRed(false);
            return;
        }
        if(parentOfTheRootOfTheDeficientSubtree == null) {
            return;
        }
        // let py be the parent of rootOfTheDeficientSubtree
        RBTNode py = parentOfTheRootOfTheDeficientSubtree;
        // R?? cases
        if(rootOfTheDeficientSubtree == py.getRightChild()) {
            
            // let v be the sibling of rootOfTheDeficientSubtree
            RBTNode v = py.getLeftChild();
            // Rb? case
            if(v.getIsRed() != true) {
                //Rb0 - v does not have any red children
                if(getNumberOfRedChildren(v) == 0) {
                    //Rb0, case 1
                    if(py.getIsRed() == false) {
                        v.setIsRed(true);
                        rebalanceRBTAfterDelete(py, py.getParent());
                    }
                    //Rb0, case 2
                    else {
                        py.setIsRed(false);
                        v.setIsRed(true);
                        return;
                    }
                }
                //Rb1
                else if(getNumberOfRedChildren(v) == 1) {
                    //Rb1, case 1, left child of v is red
                    if(v.getLeftChild() != null && v.getLeftChild().getIsRed()) {
                        rotateLL(v.getLeftChild(), v, py);
                        v.getLeftChild().setIsRed(false);
                        v.setIsRed(py.getIsRed());
                        py.setIsRed(false);
                        return;
                    }
                    //Rb1, case 2, right child of v is red
                    if(v.getRightChild() != null && v.getRightChild().getIsRed()) {
                        RBTNode w = v.getRightChild();
                        rotateLR(w, v, py);
                        w.setIsRed(py.getIsRed());
                        py.setIsRed(false);
                        return;
                    }
                }
                // Rb2 - v has 2 red children
                else {
                    RBTNode w = v.getRightChild();
                    rotateLR(w, v, py);
                    w.setIsRed(py.getIsRed());
                    py.setIsRed(false);
                    return;
                }

            }
            // Rr?
            else {
                RBTNode w = v.getRightChild();
                // Rr0 - no child of w is red
                if(getNumberOfRedChildren(w) == 0) {
                    rotateLL(v.getLeftChild(), v, py);
                    v.setIsRed(false);
                    py.getLeftChild().setIsRed(true);
                    return;
                }
                //Rr1 - 1 child of w is red
                else if(getNumberOfRedChildren(w) == 1) {
                    
                    // Case 1, left child of w is red
                    if(w.getLeftChild() != null && w.getLeftChild().getIsRed() == true) {
                        rotateLR(w, v, py);
                        v.getRightChild().setIsRed(false);
                        return;
                    }
                    // Case 2, right child of w is red
                    if(w.getRightChild() != null && w.getRightChild().getIsRed() == true) {
                        RBTNode x = w.getRightChild();
                        specialRotationLR(x, w, v, py);
                        x.setIsRed(false);
                        return;
                    }
                }
                //Rr2 - 2 children of 2 are red
                else  {
                    RBTNode x = w.getRightChild();
                    specialRotationLR(x, w, v, py);
                    x.setIsRed(false);
                    return;
                }
                
            }
        }
        // L?? cases
        else {
            RBTNode v = py.getRightChild();
            // Lb? case
            if(v.getIsRed() != true) {
                // Lb0
                if(getNumberOfRedChildren(v) == 0) {
                    //Lb0, Case 1
                    if(py.getIsRed() == false) {
                        if(root.getRideNumber() == 7)
                            root = root;
                        v.setIsRed(true);
                        rebalanceRBTAfterDelete(py, py.getParent());
                        return;
                    }
                    //Lb0, Case 2
                    else {
                        py.setIsRed(false);
                        v.setIsRed(true);
                        return;
                    }
                }
                //Lb1
                else if(getNumberOfRedChildren(v) == 1) {
                    //Lb1, Case 1
                    if(v.getLeftChild() != null && v.getLeftChild().getIsRed()) {
                        RBTNode w = v.getLeftChild();
                        rotateRL(w, v, py);
                        w.setIsRed(py.getIsRed());
                        py.setIsRed(false);
                        return;
                    }
                    //Lb1, Case 2
                    if(v.getRightChild() != null && v.getRightChild().getIsRed()) {
                        rotateRR(v.getRightChild(), v, py);
                        v.getRightChild().setIsRed(false);
                        v.setIsRed(py.getIsRed());
                        py.setIsRed(false);
                        return;
                    }
                }
                //Lb2
                else {
                    RBTNode w = v.getLeftChild();
                    rotateRL(w, v, py);
                    w.setIsRed(py.getIsRed());
                    py.setIsRed(false);
                    return;
                }
            }
            //Lr?
            else {
                RBTNode w = v.getLeftChild();
                //Lr0 No child of w is Red
                if(getNumberOfRedChildren(w) == 0) {
                    rotateRR(w, v, py);
                    v.setIsRed(false);
                    py.getRightChild().setIsRed(true);
                    return;
                }
                //Lr1
                else if(getNumberOfRedChildren(w) == 1) {
                    //Lr1, Case 1 - left child of w is Red
                    if(w.getLeftChild().getIsRed()) {
                        RBTNode x = w.getLeftChild();
                        specialRotationRL(x, w, v, py);
                        x.setIsRed(false);
                        return;
                    }
                    //Lr1, Case 2- Right child of w is Red
                    else {
                        rotateRL(w, v, py);
                        v.getLeftChild().setIsRed(true);
                        return;
                    }
                }
                //Lr2
                else {
                    RBTNode x = w.getLeftChild();
                    specialRotationRL(x, w, v, py);
                    x.setIsRed(false);
                    return;
                }   
            }

        }

    }
    // LL rotation
    public void rotateLL(RBTNode p, RBTNode pp, RBTNode gp) {
        if(gp.getParent() != null) {
            if(gp.getParent().getLeftChild() == gp) {
                gp.getParent().setLeftChild(pp);
            }
            else {
                gp.getParent().setRightChild(pp);
            }
            pp.setParent(gp.getParent());
        }
        else {
            setRoot(pp);
            pp.setParent(null);
        }
        if(pp.getRightChild() != null) {
            gp.setLeftChild(pp.getRightChild());
            pp.getRightChild().setParent(gp);
        }
        else {
            gp.setLeftChild(null);
        }
        pp.setRightChild(gp);
        gp.setParent(pp);
    }
    // LR rotation
    public void rotateLR(RBTNode p, RBTNode pp, RBTNode gp) {
        if(gp.getParent() != null) {
            if(gp.getParent().getLeftChild() == gp) {
                gp.getParent().setLeftChild(p);
            }
            else {
                gp.getParent().setRightChild(p);
            }
            p.setParent(gp.getParent());
        }
        else {
            setRoot(p);
            p.setParent(null);
        }
        if(p.getRightChild() != null) {
            gp.setLeftChild(p.getRightChild());
            p.getRightChild().setParent(gp);
        }
        else {
            gp.setLeftChild(null);
        }
        if(p.getLeftChild() != null) {
            pp.setRightChild(p.getLeftChild());
            p.getLeftChild().setParent(pp);
        }
        else {
            pp.setRightChild(null);
        }
        pp.setParent(p);
        gp.setParent(p);
        p.setLeftChild(pp);
        p.setRightChild(gp);
    }
    // RL rotation
    public void rotateRL(RBTNode p, RBTNode pp, RBTNode gp) {
        if(gp.getParent() != null) {
            if(gp.getParent().getLeftChild() == gp) {
                gp.getParent().setLeftChild(p);
            }
            else {
                gp.getParent().setRightChild(p);
            }
            p.setParent(gp.getParent());
        }
        else {
            setRoot(p);
            p.setParent(null);
        }
        if(p.getRightChild() != null) {
            pp.setLeftChild(p.getRightChild());
            p.getRightChild().setParent(pp);
        }
        else {
            pp.setLeftChild(null);
        }
        if(p.getLeftChild() != null) {
            gp.setRightChild(p.getLeftChild());
            p.getLeftChild().setParent(gp);
        }
        else {
            gp.setRightChild(null);
        }
        pp.setParent(p);
        gp.setParent(p);
        p.setLeftChild(gp);
        p.setRightChild(pp);
    }
    // RR rotation
    public void rotateRR(RBTNode p, RBTNode pp, RBTNode gp) {
        if(gp.getParent() != null) {
            if(gp.getParent().getLeftChild() == gp) {
                gp.getParent().setLeftChild(pp);
            }
            else {
                gp.getParent().setRightChild(pp);
            }
            pp.setParent(gp.getParent());
        }
        else {
            setRoot(pp);
            pp.setParent(null);
        }
        if(pp.getLeftChild() != null) {
            gp.setRightChild(pp.getLeftChild());
            pp.getLeftChild().setParent(gp);
        }
        else {
            gp.setRightChild(null);
        }
        pp.setLeftChild(gp);
        gp.setParent(pp);
    }
    public void specialRotationLR(RBTNode x, RBTNode w, RBTNode v, RBTNode py) {
        if(x.getRightChild() != null) {
            py.setLeftChild(x.getRightChild());
            x.getRightChild().setParent(py);
        }
        else {
            py.setLeftChild(null);
        }
        if(py.getParent() != null) {
            x.setParent(py.getParent());
            if(x.getParent().getLeftChild() == py) {
                x.getParent().setLeftChild(x);
            }
            else {
                x.getParent().setRightChild(x);
            }
        }
        else {
            root = x;
            x.setParent(null);
        }
        if(x.getLeftChild() != null) {
            w.setRightChild(x.getLeftChild());
            x.getLeftChild().setParent(w);
        }
        else {
            w.setRightChild(null);
        }
        
        x.setRightChild(py);
        py.setParent(x);
        x.setLeftChild(v);
        v.setParent(x);
    }
    public void specialRotationRL(RBTNode x, RBTNode w, RBTNode v, RBTNode py) {
        if(x.getLeftChild() != null) {
            py.setRightChild(x.getLeftChild());
            x.getLeftChild().setParent(py);
        }
        else {
            py.setRightChild(null);
        }
        if(py.getParent() != null) {
            x.setParent(py.getParent());
            if(x.getParent().getLeftChild() == py) {
                x.getParent().setLeftChild(x);
            }
            else {
                x.getParent().setRightChild(x);
            }
        }
        else {
            root = x;
            x.setParent(null);
        }
        if(x.getRightChild() != null) {
            w.setLeftChild(x.getRightChild());
            x.getRightChild().setParent(w);
        }
        else {
            w.setLeftChild(null);
        }
        x.setLeftChild(py);
        x.setRightChild(v);
        py.setParent(x);
        v.setParent(x);
    }
    //finds the range of tuples between 2 given ride numbers.
    public void printRange(int low, int high, StringBuilder tuples)  {
        printRange(root, low, high, tuples);
    }
    public void printRange(RBTNode node, int low, int high, StringBuilder tuples) {
        if(node == null) {
            return;
        }
        if(node.getRideNumber() >= low) {
            printRange(node.getLeftChild(), low, high, tuples);
        }
        if(node.getRideNumber() >= low && node.getRideNumber() <= high) {
            tuples.append("(" + node.getRideNumber() + "," + node.getRideCost() + "," + node.getTripDuration() + ")" + ",");
        }
        if(node.getRideNumber() <= high) {
            printRange(node.getRightChild(), low, high, tuples);
        }
    }
    //returns the sibling of a child node
    public RBTNode getOtherChild(RBTNode child, RBTNode parent) {
        if(parent.getLeftChild() == child) {
            return parent.getRightChild();
        }
        else {
            return parent.getLeftChild();
        }
    }
    //returns number of red children
    public int getNumberOfRedChildren(RBTNode node) {
        int count = 0;
        if(node.getLeftChild() != null && node.getLeftChild().getIsRed()){
            count++;
        }
        if(node.getRightChild() != null && node.getRightChild().getIsRed()) {
            count++;
        }
        return count;
    }
//    public void printRBTInOrder() {
//        printInOrder(root);
//    }
//    public void printInOrder(RBTNode node) {
//        if (node == null)
//            return;
//
//        /* first recur on left child */
//        printInOrder(node.getLeftChild());
//
//        /* then print the data of node */
//        if(node.getParent() != null)
//            System.out.print(node.getRideNumber() + " " + "[" + node.getParent().getRideNumber() + " " + node.getLeftChild() + " " + node.getRightChild() + " " + node.getIsRed() + "]" + " ");
//        else
//            System.out.print(node.getRideNumber() + " " + "[" + node.getParent() + " " + node.getLeftChild() + " " + node.getRightChild() + " " + node.getIsRed() + "]" + " ");
//
//        /* now recur on right child */
//        printInOrder(node.getRightChild());
//    }
}