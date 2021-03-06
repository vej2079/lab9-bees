package bee;

import world.BeeHive;
import world.QueensChamber;

/**
 * The male drone bee has a tough life.  His only job is to mate with the queen
 * by entering the queen's chamber and awaiting his royal highness for some
 * sexy time.  Unfortunately his reward from mating with the queen is his
 * endophallus gets ripped off and he perishes soon after mating.
 *
 * @author Sean Strout @ RIT CS
 * @author Victoria Jones
 */
public class Drone extends Bee {

    /** Whether the current instance has mated with the queen or not */
    private boolean mated;

    /** The queen's chamber */
    private QueensChamber chamber;

    /**
     * When the drone is created they should retrieve the queen's
     * chamber from the bee hive and initially the drone has not mated.
     *
     * @param beeHive the bee hive
     */
    public Drone(BeeHive beeHive){
        super(Role.DRONE, beeHive);
        this.mated = false;
        this.chamber = beeHive.getQueensChamber();
    }


    /**
     * When the drone runs, they check if the bee hive is active.  If so,
     * they perform their sole task of entering the queen's chamber.
     * If they return from the chamber, it can mean only one of two
     * things.  If they mated with the queen, they sleep for the
     * required mating time, and then perish (the beehive should be
     * notified of this tragic event).  You should display a message:<br>
     * <br>
     * <tt>*D* {bee} has perished!</tt><br>
     * <br>
     * <br>
     * Otherwise if the drone has not mated it means they survived the
     * simulation and they should end their run without any
     * sleeping.
     */
    public void run() {
        while (this.beeHive.isActive()) {
            chamber.enterChamber(this);
            // System.out.println("Bee currently running: " + this.toString());
            if (!mated) {
                try {
                    Thread.sleep(Queen.MATE_TIME_MS);
                }
                catch( InterruptedException ex ) {
                    System.out.println("Drone bee interrupted when sleeping!");
                }
                beeHive.beePerished(this);
                System.out.println("*D* " + this.toString() + " has perished!");
            }
            // this.notify();
        }
    }


    /**
     * Updates the current instance's mated field
     * based on whether the bee has mated or not.
     */
    public void setMated() {
        this.mated = true;
    }
}