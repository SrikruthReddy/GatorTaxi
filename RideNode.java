class RideNode {
    int rideNumber;
    int rideCost;
    int tripDuration;

    RideNode(int rideNumber, int rideCost, int tripDuration) {
        this.rideNumber = rideNumber;
        this.rideCost = rideCost;
        this.tripDuration = tripDuration;
    }
    // Getters
    public int getRideNumber() {
        return rideNumber;
    }
    public int getRideCost() {
        return rideCost;
    }
    public int getTripDuration() {
        return tripDuration;
    }
    // Setters
    public void setRideNumber(int rideNumber) {
        this.rideNumber = rideNumber;
    }
    public void setRideCost(int rideCost) {
        this.rideCost = rideCost;
    }
    public void setTripDuration(int tripDuration) {
        this.tripDuration = tripDuration;
    }
}