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
public class ZealotInfo extends UnitInfo {

    public ZealotInfo() {
        super(UnitEnum.ZEALOT, 100, 0, 38, 2);
        getRequirements().add(GatewayInfo.class);
        setProductionBlocks(GatewayInfo.class);
    }
}
