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
 * @author Victoria Jones
 */
public class QueensChamber {

    /** whether the queen is ready, in the sense that there are drone
     * bees in the chamber */
    private boolean queenReady;
    /** A queue of the drone bees in order of arrival */
    private ConcurrentLinkedQueue<bee.Drone> drones;

    /**
     * Constructs a queen's chamber including setting the queen's
     * readiness to false.
     */
    public QueensChamber() {
        this.queenReady = false;
        this.drones = new ConcurrentLinkedQueue<>();
    }


    /**
     * Allows a drone bee to enter the chamber and mate with the queen
     * should the drone be first in the queue (will also set the
     * queen's readiness to false, otherwise will wait in
     * line until it is its turn.
     *
     * When a bee enters the chamber, will print:
     * <br>
     * <tt>*QC* {bee} enters chamber</tt><br>
     * <br>
     *
     * When a bee leaves the chamber, will print:
     * <br>
     * <tt>*QC* {bee} leaves chamber</tt><br>
     * <br>
     *
     * Should an error occur when blocking the bee, will print the error
     * message {Drone bee} was interrupted when waiting in the chamber line.
     *
     * @param drone the bee that enters the chamber.
     */
    public synchronized void enterChamber(bee.Drone drone) {
        System.out.println("*QC* " + drone.toString() + " enters chamber");
        drones.add(drone);
        if (queenReady && (drones.peek() == drone)) {
            this.queenReady = false;
        } else {
            try {
                this.wait();
            } catch (InterruptedException ie) {
                System.out.println(drone.toString() + " was interrupted when waiting in the chamber line");
            }
        }
        queenReady = hasDrone();
        System.out.println("*QC* " + drone.toString() + " leaves chamber");
    }


    /**
     * A Drone bee is summoned by the queen when she
     * is ready (beehive has resources), after a drone bee
     * has mated with the queen, the drone bee will die thus
     * being removed from the drone queue. The queen's
     * readiness will be set to true.
     *
     * When the queen mates with a drone bee, will print:
     * <br>
     * <tt>*QC* Queen mates with {bee}</tt><br>
     * <br>
     */
    public synchronized void summonDrone() {
        System.out.println(hasDrone());
        if (queenReady) {
            bee.Drone currDrone = drones.peek();
            System.out.println("*QC* Queen mates with " + currDrone.toString());
            this.drones.remove(currDrone);
            currDrone.setMated();
            this.notifyAll();
            queenReady = true;
        }
    }


    /**
     * Following the simulation, the queen will dismiss
     * all remaining drone bees in the queue and set
     * the queen's readiness to be true.
     */
    public synchronized void dismissDrone() {
        if (hasDrone()) {
            queenReady = true;
            this.notifyAll();
            for (bee.Drone drone : drones) {
                drones.remove(drone);
            }
        }
    }


    /**
     * Whether there is another drone in the queue or not.
     * @return true if there are more drones in the queue,
     * false otherwise.
     */
    public boolean hasDrone() {
        return !drones.isEmpty();
    }

}