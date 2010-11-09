/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.spoonman.zealotrush.info;

/**
 *
 * @author spoonman
 */
public class ZealotInfo extends UnitInfo {

    public ZealotInfo() {
        super(100, 0, 38, 2);
        getRequirements().add(GatewayInfo.class);
        setProductionBlocks(GatewayInfo.class);
    }
}
