/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.spoonman.zealotrush;

import eu.spoonman.zealotrush.info.UnitInfo;

/**
 *
 * @author spoonman
 */
public class BuildOrder {

    private UnitInfo unitInfo;
    private int minerals;
    private int gas;
    private int supplies;
    private int seconds;

    public BuildOrder(UnitInfo unitInfo, int minerals, int gas, int supplies, int seconds) {
        this.unitInfo = unitInfo;
        this.minerals = minerals;
        this.gas = gas;
        this.supplies = supplies;
        this.seconds = seconds;
    }

    public UnitInfo getUnitInfo() {
        return unitInfo;
    }

    public void setUnitInfo(UnitInfo unitInfo) {
        this.unitInfo = unitInfo;
    }

    public int getGas() {
        return gas;
    }

    public void setGas(int gas) {
        this.gas = gas;
    }

    public int getMinerals() {
        return minerals;
    }

    public void setMinerals(int minerals) {
        this.minerals = minerals;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getSupplies() {
        return supplies;
    }

    public void setSupplies(int supplies) {
        this.supplies = supplies;
    }
}
