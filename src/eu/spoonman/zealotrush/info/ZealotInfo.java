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
public class ZealotInfo extends UnitInfo {

    public ZealotInfo(GatewayInfo gatewayInfo) {
        super(100, 0, 38, 2);
        getRequirements().add(gatewayInfo);
        setProductionBlocks(gatewayInfo);
    }
}
