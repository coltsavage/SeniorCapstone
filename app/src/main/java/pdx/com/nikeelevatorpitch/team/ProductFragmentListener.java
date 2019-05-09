package pdx.com.nikeelevatorpitch.team;

/**
 * Listener interface for listening to Product fragment level switching.
 * @author Eli
 */

public interface ProductFragmentListener {

    /**
     * Callback signalling to switch to the given product level.
     * @param productLvl - product level to switch to
     */
    void switchProductLevel(ProductFragmentType productLvl);
}
