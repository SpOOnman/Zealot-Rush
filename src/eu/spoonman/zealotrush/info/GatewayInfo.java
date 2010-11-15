/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.spoonman.zealotrush.info;

import eu.spoonman.zealotrush.Player;

/**
 *
 * @author spoonman
 */
public class GatewayInfo extends UnitInfo {

    public GatewayInfo(PylonInfo pylon) {
        super(150, 0, 60, 0);
        getRequirements().add(pylon);
    }



}
