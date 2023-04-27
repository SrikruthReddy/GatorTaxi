import java.io.*;

public class gatorTaxi {
    public static void main(String[] args) throws IOException{
            String outputFilename = "output_file.txt";
            FileReader fr = new FileReader(new File(args[0]));
            FileWriter fw = new FileWriter(new File(outputFilename));
            BufferedReader bufferedReader = new BufferedReader(fr);
            BufferedWriter bufferedWriter = new BufferedWriter(fw);
            String input;
            MinHeap minHeap = new MinHeap();
            RBT rbt = new RBT();
            loop: while(true) {

                input = bufferedReader.readLine();
                if(input == null){
                    break;
                }
                // Parse the input
                String operation = input.substring(0, input.indexOf("("));
                String[] parameters = getParameters(input.substring(input.indexOf("(") + 1, input.indexOf(")")));
                // Perform the operation
                switch(operation) {
                    case "Insert": {

                        int rideNumber = Integer.parseInt(parameters[0]);
                        int rideCost = Integer.parseInt(parameters[1]);
                        int tripDuration = Integer.parseInt(parameters[2]);
                        RBTNode temp = rbt.search(rideNumber);
                        if(temp != null)
                        {
                            bufferedWriter.write("Duplicate RideNumber\n");
                            break loop;
                        }
                        MinHeapNode minHeapNode = new MinHeapNode(rideNumber, rideCost, tripDuration);
                        RBTNode rbtNode = new RBTNode(rideNumber, rideCost, tripDuration);
                        minHeapNode.setCorrespondingRBTNode(rbtNode);
                        minHeap.insert(minHeapNode);
                        rbt.insert(rbtNode);
                        break;
                    }
                    case "Print": {
                        if (parameters.length == 1) {
                            int k = Integer.parseInt(parameters[0]);
                            RBTNode node = rbt.search(k);
                            if (node == null) {
                                bufferedWriter.write("(0,0,0)" + "\n");
                            } else {
                                bufferedWriter.write("(" + node.getRideNumber() + "," + node.getRideCost() + "," + node.getTripDuration() + ")" + "\n");
                            }
                        } else {
                            StringBuilder outputString = new StringBuilder();
                            rbt.printRange(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]), outputString);
                            if(outputString.length() == 0) {
                                bufferedWriter.write("(0,0,0)" + "\n");
                            }
                            else {
                                outputString.deleteCharAt(outputString.length() - 1);
                                bufferedWriter.write(outputString.toString().trim());
                                bufferedWriter.newLine();
                            }
                        }
                        break;
                    }
                    case "UpdateTrip": {
                        int rideNumber = Integer.parseInt(parameters[0]);
                        int newTripDuration = Integer.parseInt(parameters[1]);
                        RBTNode rbtNode = rbt.search(rideNumber);
                        if (rbtNode != null) {

                            if (newTripDuration <= rbtNode.getTripDuration()) {
                                rbtNode.setTripDuration(newTripDuration);
                                minHeap.getHeap()[rbtNode.getCorrespondingMinHeapNodeIndex()].setTripDuration(newTripDuration);
                                minHeap.updateTrip(rbtNode.getCorrespondingMinHeapNodeIndex());

                            } else if (newTripDuration <= 2 * rbtNode.getTripDuration()) {
                                rbtNode.setTripDuration(newTripDuration);
                                rbtNode.setRideCost(rbtNode.getRideCost() + 10);
                                minHeap.getHeap()[rbtNode.getCorrespondingMinHeapNodeIndex()].setRideCost(rbtNode.getRideCost());
                                minHeap.getHeap()[rbtNode.getCorrespondingMinHeapNodeIndex()].setTripDuration(newTripDuration);
                                minHeap.updateTrip(rbtNode.getCorrespondingMinHeapNodeIndex());
                            } else {
                                minHeap.arbitraryRemove(rbtNode.getCorrespondingMinHeapNodeIndex());
                                rbt.delete(rbtNode, minHeap);
                            }
                        }
                        break;
                    }
                    case "CancelRide": {
                        int rideNumber = Integer.parseInt(parameters[0]);
                        RBTNode rbtNode = rbt.search(rideNumber);
                        if (rbtNode != null) {
                            minHeap.arbitraryRemove(rbtNode.getCorrespondingMinHeapNodeIndex());
                            rbt.delete(rbtNode, minHeap);
                        }
                        break;
                    }
                    case "GetNextRide": {
                        MinHeapNode node = minHeap.removeMin();

                        if (node == null) {
                            bufferedWriter.write("No active ride requests\n");
                        } else {
                            bufferedWriter.write("(" + node.getRideNumber() + "," + node.getRideCost() + "," + node.getTripDuration() + ")" + "\n");
                            RBTNode rbtNode = node.getCorrespondingRBTNode();
                            rbt.delete(rbtNode, minHeap);
                        }
                        break;
                    }
                }
                minHeap.print();
            }

            bufferedWriter.flush();
            bufferedReader.close();
            bufferedWriter.close();


    }
    //returns the parameters as a string array
    public static String[] getParameters(String input) {
        String[] parameters = input.split(",");
        for(int i = 0; i < parameters.length; i++) {
            parameters[i] = parameters[i].trim();
        }
        return parameters;
    }
}