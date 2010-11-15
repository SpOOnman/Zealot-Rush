/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.spoonman.zealotrush.info;

import eu.spoonman.zealotrush.UnitState;

/**
 *
 * @author spoonman
 */
public class ProbeInfo extends UnitInfo {

    public ProbeInfo(NexusInfo nexusInfo) {
        super(50, 0, 15, 1);
        setProductionBlocks(nexusInfo);
        setMineralGain(5);
        setMineralGainTime(4);
        setUnitStateAfterProduction(UnitState.GETHERING);
    }



}
