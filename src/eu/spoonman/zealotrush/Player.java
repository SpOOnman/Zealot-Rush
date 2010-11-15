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
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author spoonman
 */
public class Player {

    public final static NexusInfo NEXUS = new NexusInfo();
    public final static PylonInfo PYLON = new PylonInfo();
    public final static GatewayInfo GATEWAY = new GatewayInfo(PYLON);
    public final static ProbeInfo PROBE = new ProbeInfo(NEXUS);
    public final static ZealotInfo ZEALOT = new ZealotInfo(GATEWAY);

    private final static Logger log = Logger.getLogger(Player.class);
    private int minerals;
    private int gas;
    private int supplies;
    private int suppliesMax;
    private int seconds;
    private List<UnitInfo> possibleUnits;
    private List<Unit> units;
    private Map<UnitInfo, Integer> unitCounts;
    private Map<UnitInfo, Integer> inProductionCounts;
    private int targetZealots;
    private int targetTimeStamp;
    private boolean logEvents;

    public static void main(String[] args) {
        Player player = new Player();
        player.setLogEvents(true);
        //int[] orders = {3,1,1,2,1,4,4,4,4,4,2,3,4,2,3};
        //int[] orders = {3,3,2,1,3,3,2,4,3,1,4,4,4,4 };
        //int[] orders = {2,1,1,3,1,4,4,4,4,4,4,2,1,0,2};
        int[] orders = {2,3,1,1,1,4,4,4,4,2,3,2,4,3,1};
        //int[] orders = {3,2,3,3,1,1,4,4,4,4};


        List<Integer> o = new ArrayList<Integer>();
        for (int x : orders) {
            o.add(x);
        }

        player.simulate(300, o, 5);
        System.out.println(player.getSeconds());
    }

    public Player() {
        this.minerals = 0;
        this.gas = 0;
        this.supplies = 0;
        this.suppliesMax = 0;
        this.targetTimeStamp = 1000;

        this.targetZealots = 10;

        this.seconds = 0;

        this.possibleUnits = new ArrayList<UnitInfo>();
        this.units = new ArrayList<Unit>();
        this.unitCounts = new HashMap<UnitInfo, Integer>();
        this.inProductionCounts = new HashMap<UnitInfo, Integer>();

        this.logEvents = false;
    }

    public void simulate(int time, List<Integer> orders, int targetZealots) {
        initialize();

        this.targetZealots = targetZealots;

        ListIterator<Integer> iter = orders.listIterator();
        Integer order = iter.next();

        for (int i = 0; i < time; i++) {
            tick();

            if (targetReached()) {
                break;
            }

            if (order != null && executeOrder(order)) {
                order = iter.hasNext() ? iter.next() : null;
            }
        }
    }

    public void initialize() {
        this.possibleUnits.add(NEXUS);
        this.possibleUnits.add(GATEWAY);
        this.possibleUnits.add(PYLON);
        this.possibleUnits.add(PROBE);
        this.possibleUnits.add(ZEALOT);

        this.minerals = 50;
        this.gas = 0;
        this.suppliesMax = 10;

        produce(NEXUS, true).productImmidiately();
        for (int i = 0; i < 7; i++) {
            produce(PROBE, true).productImmidiately();
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
            return true;
        }

        return false;
    }

    protected boolean targetReached() {
        Integer zealots = this.unitCounts.get(ZEALOT);
        return zealots != null && zealots.intValue() == targetZealots;

    }

    public boolean canProduce(UnitInfo unitInfo) {
        boolean result = true;

        result = result && getMinerals() >= unitInfo.getMineralCost();
        result = result && getGas() >= unitInfo.getGasCost();
        result = result && getFreeSupplies() >= unitInfo.getSuppliesCost();
        result = result && (unitInfo.getProductionBlocks() == null ? true : findFreeProducer(unitInfo) != null);
        result = result && findRequirement(unitInfo);

        return result;
    }

    protected Unit findFreeProducer(UnitInfo unitInfo) {
        for (Unit unit : getUnits()) {
            if (unit.getUnitInfo() == unitInfo.getProductionBlocks() && unit.getUnitState() == UnitState.HOLD) {
                return unit;
            }
        }

        return null;
    }

    protected boolean findRequirement(UnitInfo unitInfo) {

        for (UnitInfo requirement : unitInfo.getRequirements())
            if (getUnitCount(requirement) == 0)
                return false;

        return true;
            
    }
    public Unit produce(UnitInfo unitInfo, boolean noCost) {
        Unit unit = new Unit(this, unitInfo);
        this.units.add(unit);
        this.supplies += unitInfo.getSuppliesCost();

        if (this.inProductionCounts.get(unit.getUnitInfo()) == null) {
            this.inProductionCounts.put(unit.getUnitInfo(), 1);
        } else {
            this.inProductionCounts.put(unit.getUnitInfo(), this.inProductionCounts.get(unit.getUnitInfo()) + 1);
        }

        unit.changeState(UnitState.UNDER_CONSTRUCTION);

        if (noCost) {
            eventUnitHasBeenProduced(unit);
        } else {
            this.minerals -= unitInfo.getMineralCost();
            this.gas -= unitInfo.getGasCost();
            if (unitInfo.getProductionBlocks() != null) {
                Unit producer = findFreeProducer(unitInfo);
                // unit is already in production, but set its' producer
                producer.changeState(UnitState.IS_PRODUCTING);
                unit.setProducer(producer);
            }
        }

        return unit;
    }

    public void eventUnitGatheredMinerals(Unit unit, int minerals) {
        this.minerals += minerals;
        //printLine(String.format("Unit %s gathered %d minerals.", unit, minerals));
    }

    public void eventUnitHasBeenProduced(Unit unit) {
        this.suppliesMax += unit.getUnitInfo().getSuppliesGain();
        printLine(String.format("Unit %s has finished production.", unit));

        this.inProductionCounts.put(unit.getUnitInfo(), this.inProductionCounts.get(unit.getUnitInfo()) - 1);

        if (this.unitCounts.get(unit.getUnitInfo()) == null) {
            this.unitCounts.put(unit.getUnitInfo(), 1);
        } else {
            this.unitCounts.put(unit.getUnitInfo(), this.unitCounts.get(unit.getUnitInfo()) + 1);
        }
    }

    public void eventUnitChangedState(Unit unit, UnitState oldState, UnitState newState, int timeInState) {
        printLine(String.format("Unit %s state from %s to %s in %d secs.", unit, oldState, newState, timeInState));
    }

    public void printLine(String message) {

        if (this.logEvents) {
            log.info(String.format("%02d:%02d. %d m, %d g, %d/%d s, %s, %s",
                    this.getSeconds() / 60, this.getSeconds() % 60, this.getMinerals(), this.getGas(), this.getSupplies(), this.getSuppliesMax(), printSummary(), message));
        }
    }

    public int getUnitCount(UnitInfo unitInfo) {
        if (!(this.unitCounts.containsKey(unitInfo))) {
            return 0;
        }

        return this.unitCounts.get(unitInfo);
    }

    public int getUnitInProductionCount(UnitInfo unitInfo) {
        if (!(this.inProductionCounts.containsKey(unitInfo))) {
            return 0;
        }

        return this.inProductionCounts.get(unitInfo);

    }
    

    public String printSummary() {

        StringBuffer sb = new StringBuffer();
        for (UnitInfo unitInfo : this.possibleUnits) {
            sb.append(unitInfo.toString());
            sb.append(": ");
            sb.append(getUnitCount(unitInfo));
            int inProduction = getUnitInProductionCount(unitInfo);
            if (inProduction > 0) {
                sb.append("(" + inProduction + ")");
            }
            sb.append(", ");
        }

        return sb.toString();
    }

    public int getGas() {
        return gas;
    }

    public int getMinerals() {
        return minerals;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getFreeSupplies() {
        return this.getSuppliesMax() - this.getSupplies();
    }

    public int getSupplies() {
        return supplies;
    }

    public int getSuppliesMax() {
        return suppliesMax;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public int getTargetTimeStamp() {
        return targetTimeStamp;
    }

    public boolean isLogEvents() {
        return logEvents;
    }

    public void setLogEvents(boolean logEvents) {
        this.logEvents = logEvents;
    }
}
