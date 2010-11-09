/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.spoonman.zealotrush;

import eu.spoonman.zealotrush.info.GatewayInfo;
import eu.spoonman.zealotrush.info.NexusInfo;
import eu.spoonman.zealotrush.info.ProbeInfo;
import eu.spoonman.zealotrush.info.PylonInfo;
import eu.spoonman.zealotrush.info.UnitInfo;
import eu.spoonman.zealotrush.info.ZealotInfo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author spoonman
 */
public class Player {

    private int minerals;
    private int gas;
    private int supplies;
    private int suppliesMax;

    private int seconds;

    private List<UnitInfo> possibleUnits;
    private List<Unit> units;
    private List<BuildOrder> buildOrders;

    public Player() {
        this.minerals = 0;
        this.gas = 0;
        this.supplies = 0;
        this.suppliesMax = 0;

        this.seconds = 0;

        this.possibleUnits = new ArrayList<UnitInfo>();
        this.units = new ArrayList<Unit>();
        this.buildOrders = new ArrayList<BuildOrder>();
    }

    public void initialize() {
        this.possibleUnits.add(new NexusInfo());
        this.possibleUnits.add(new GatewayInfo());
        this.possibleUnits.add(new PylonInfo());
        this.possibleUnits.add(new ProbeInfo());
        this.possibleUnits.add(new ZealotInfo());

        this.setMinerals(150);
        this.setGas(0);
        this.setSuppliesMax(8);
        
        produce(this.possibleUnits.get(0), true).productImmidiately();
        for(int i = 0 ; i < 6 ; i++)
            produce(this.possibleUnits.get(3), true).productImmidiately();
    }

    public void tick() {
        for(Unit unit : this.units) {
            unit.tick();
        }
    }

    public void executeOrder(UnitInfo unitInfo) {
        if (canProduce(unitInfo)) {
            produce(unitInfo, false);
            BuildOrder buildOrder = new BuildOrder(unitInfo, getMinerals(), getGas(), getSupplies(), getSeconds());
            this.buildOrders.add(buildOrder);
        }
    }

    public boolean canProduce(UnitInfo unitInfo) {
        boolean result = true;

        result = result && getMinerals() >= unitInfo.getMineralCost();
        result = result && getGas() >= unitInfo.getGasCost();
        result = result && getSupplies() >= unitInfo.getSuppliesCost();

        for (Class clazz : unitInfo.getRequirements()) {
            if (result == false)
                break;
            boolean found = findFreeRequirement(clazz) != null;
            result = result && found;
        }

        return result;
    }

    protected Unit findFreeRequirement(Class clazz) {
        boolean found = false;
        for (Unit unit : getUnits()) {
            if (unit.getUnitInfo().getClass().equals(clazz) && unit.getUnitState() == UnitState.HOLD) {
                return unit;
            }
        }
        return null;
    }

    public Unit produce(UnitInfo unitInfo, boolean noCost) {
        Unit unit = new Unit(this, unitInfo);
        this.units.add(unit);

        if (!noCost) {
            this.setMinerals(this.getMinerals() - unitInfo.getMineralCost());
            this.setGas(this.getGas() - unitInfo.getGasCost());
            this.setSupplies(this.getSupplies() + unitInfo.getSuppliesCost());
            Unit producer = findFreeRequirement(unitInfo.getClass());
            // unit is already in production, but set its' producer
            producer.changeState(UnitState.IS_PRODUCTING);
            unit.setProducer(producer);
        }
        
        return unit;
    }

    public List<BuildOrder> getBuildOrders() {
        return buildOrders;
    }

    public void setBuildOrders(List<BuildOrder> buildOrders) {
        this.buildOrders = buildOrders;
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

    public int getSuppliesMax() {
        return suppliesMax;
    }

    public void setSuppliesMax(int suppliesMax) {
        this.suppliesMax = suppliesMax;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

}
