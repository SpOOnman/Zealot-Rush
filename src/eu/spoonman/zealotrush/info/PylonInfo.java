/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.spoonman.zealotrush.info;

import eu.spoonman.zealotrush.UnitEnum;

/**
 *
 * @author spoonman
 */
public class PylonInfo extends UnitInfo {

    public PylonInfo() {
        super(UnitEnum.PYLON, 100, 0, 25, 0);
        setSuppliesGain(8);
    }
}
