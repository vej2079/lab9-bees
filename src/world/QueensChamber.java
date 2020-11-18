package world;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

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

    private bee.Drone currDrone;

    private ConcurrentLinkedQueue<bee.Drone> drones;

    public QueensChamber() {
        this.queenReady = false;
        this.currDrone = null;
        this.drones = new ConcurrentLinkedQueue<>();
    }

    public synchronized void enterChamber(bee.Drone drone) { // does this need to be sync?
        System.out.println("*QC* " + drone.toString() + " enters chamber");
        drones.add(drone);
        if (queenReady && (drones.peek() == drone)) {
            this.currDrone = drone;
            this.queenReady = false;
        } else {
            try {
                this.wait();
            } catch (InterruptedException ie) {
                System.out.println(drone.toString() + " was interrupted when waiting in the chamber line");
            }
        }
        System.out.println("*QC* " + drone.toString() + " leaves chamber");
    }

    public synchronized void summonDrone() {
        if (hasDrone()) { // && queenReady?
            // this.queenReady = true; // where does this go?? - set in hasDrone()
            System.out.println("*QC* Queen mates with " + currDrone.toString());
            this.drones.remove(currDrone);
            currDrone.setMated();
            this.notifyAll();
        }
    }

    public synchronized void dismissDrone() {
        if (hasDrone()) {
            // queenReady = true; // - set in hasDrone()
            this.notifyAll();
            for (bee.Drone drone : drones) {
                // drone.notifyAll();
                drones.remove(drone);
            }
        }
    }

    public boolean hasDrone() {
        // change queenReady to false if not
        boolean ans = !drones.isEmpty();
        queenReady = ans;
        return ans;
    }

}