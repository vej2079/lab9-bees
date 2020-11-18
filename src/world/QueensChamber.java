package world;

/**
 * The queen's chamber is where the mating ritual between the queen and her
 * drones is conducted.  The drones will enter the chamber in order.
 * If the queen is ready and a drone is in here, the first drone will
 * be summoned and mate with the queen.  Otherwise the drone has to wait.
 * After a drone mates they perish, which is why there is no routine
 * for exiting (like with the worker bees and the flower field).
 *
 * @author Sean Strout @ RIT CS
 * @author YOUR NAME HERE
 */
public class QueensChamber {

    private boolean queenReady;

    private bee.Bee currDrone;

    public QueensChamber() {
        this.queenReady = false;
        this.currDrone = null;

        // no drones in chamber
        // should there be a separate drone concurrent queue?

    }

    public synchronized void enterChamber(bee.Drone drone) {
        System.out.println("*QC* " + drone.toString() + " enters chamber");
        if (queenReady) {
            // remove drone from wait list
            this.currDrone = drone;
        }
        System.out.println("*QC* " + drone.toString() + " leaves chamber");
    }

    public synchronized void summonDrone() {
        if (queenReady && hasDrone()) {
            System.out.println("*QC* Queen mates with " + currDrone.toString());
            this.notifyAll();
        }
    }

    public void dismissDrone() {
        if (hasDrone()) {
            try {
                currDrone.join();
            } catch (InterruptedException ie) {
                System.out.println( "Got interrupted while joining in QC.");
            }
        }
    }

    public boolean hasDrone() {
        // change queenReady to false if not
        boolean ans = currDrone.isAlive(); // this isn't right
        queenReady = ans;
        return ans;
    }

}