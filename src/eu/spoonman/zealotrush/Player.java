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

    public static void main(String[] args) {
        Player player = new Player();
        player.initialize();
        int[] orders = {2, 3, 3, 3, 2, 1, 4, 4};
        int currentOrder = 0;

        for (int i = 0; i < 600; i++) {
            player.tick();
            if (player.executeOrder(orders[currentOrder])) {
                currentOrder++;
                if (currentOrder == orders.length)
                    currentOrder = 0;
            }
        }
    }

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
        for (int i = 0; i < 6; i++) {
            produce(this.possibleUnits.get(3), true).productImmidiately();
        }
    }

    public void tick() {
        for (Unit unit : this.units) {
            unit.tick();
        }
        this.seconds += 1;
    }

    public boolean executeOrder(int i) {
        UnitInfo unitInfo = this.possibleUnits.get(i);
        if (canProduce(unitInfo)) {
            produce(unitInfo, false);
            BuildOrder buildOrder = new BuildOrder(unitInfo, getMinerals(), getGas(), getSupplies(), getSeconds());
            this.buildOrders.add(buildOrder);
            return true;
        }
        return false;
    }

    public boolean canProduce(UnitInfo unitInfo) {
        boolean result = true;

        result = result && getMinerals() >= unitInfo.getMineralCost();
        result = result && getGas() >= unitInfo.getGasCost();
        result = result && getFreeSupplies() >= unitInfo.getSuppliesCost();
        result = result && unitInfo.getProductionBlocks() != null && findFreeProducer(unitInfo) != null;
        result = result && true;//findFreeRequirement(unitInfo) != null;

        return result;
    }

    protected Unit findFreeProducer(UnitInfo unitInfo) {
        for (Unit unit : getUnits()) {
            if (unit.getUnitInfo().getClass().equals(unitInfo.getProductionBlocks()) && unit.getUnitState() == UnitState.HOLD) {
                return unit;
            }
        }

        return null;
    }

//    protected Unit findFreeRequirement(UnitInfo unitInfo) {
//
//        boolean result = false;
//
//        for (Class clazz : unitInfo.getRequirements()) {
//            if (result == false)
//                break;
//            boolean found = false;
//            for (Unit unit : getUnits()) {
//                if (unit.getUnitInfo().getClass().equals(clazz) && unit.getUnitState() == UnitState.HOLD) {
//                    found = true;
//                }
//            }
//            result = result && found;
//        }
//        return result;
//    }
    public Unit produce(UnitInfo unitInfo, boolean noCost) {
        Unit unit = new Unit(this, unitInfo);
        this.units.add(unit);

        if (!noCost) {
            this.setMinerals(this.getMinerals() - unitInfo.getMineralCost());
            this.setGas(this.getGas() - unitInfo.getGasCost());
            this.setSupplies(this.getSupplies() + unitInfo.getSuppliesCost());
            Unit producer = findFreeProducer(unitInfo);
            // unit is already in production, but set its' producer
            producer.changeState(UnitState.IS_PRODUCTING);
            unit.setProducer(producer);
        }

        return unit;
    }

    public void eventUnitGatheredMinerals(Unit unit, int minerals) {
        this.setMinerals(this.getMinerals() + minerals);
        //printLine(String.format("Unit %s gathered %d minerals.", unit, minerals));
    }

    public void eventUnitChangedState(Unit unit, UnitState oldState, UnitState newState, int timeInState) {
        printLine(String.format("Unit %s state from %s to %s in %d secs.", unit, oldState, newState, timeInState));

    }

    public void printLine(String message) {
        System.out.println(String.format("%d. %d m, %d g, %d/%d s, %s", this.getSeconds(), this.getMinerals(), this.getGas(), this.getSupplies(), this.getSuppliesMax(), message));
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

    public int getFreeSupplies() {
        return this.getSuppliesMax() - this.getSupplies();
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
