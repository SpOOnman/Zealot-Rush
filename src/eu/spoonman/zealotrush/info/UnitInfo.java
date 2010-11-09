/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.spoonman.zealotrush.info;

import eu.spoonman.zealotrush.UnitEnum;
import eu.spoonman.zealotrush.UnitState;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author spoonman
 */
public abstract class UnitInfo {

    private final static Pattern NAME_PATTERN = Pattern.compile(".*\\.([A-Za-z]+)Info");

    private UnitEnum unitEnum;

    private int mineralCost;
    private int gasCost;
    private int productionTime;
    private int suppliesCost;

    private UnitState unitStateAfterProduction;

    private List<Class> requirements;
    private Class productionBlocks;

    private int mineralGain;
    private int mineralGainTime;
    private int gasGain;
    private int gasGainTime;
    private int suppliesGain;

    public UnitInfo(UnitEnum unitEnum, int mineralCost, int gasCost, int productionTime, int suppliesCost) {
        this.unitEnum = unitEnum;
        this.mineralCost = mineralCost;
        this.gasCost = gasCost;
        this.productionTime = productionTime;
        this.suppliesCost = suppliesCost;

        this.unitStateAfterProduction = UnitState.HOLD;

        this.requirements = new ArrayList<Class>();
        this.productionBlocks = null;

        this.suppliesGain = 0;
    }

    @Override
    public String toString() {
        Matcher matcher = NAME_PATTERN.matcher(this.getClass().getName());
        matcher.matches();
        return matcher.group(1);
    }

    public int getProductionTime() {
        return productionTime;
    }

    public void setProductionTime(int buildingTime) {
        this.productionTime = buildingTime;
    }

    public int getGasCost() {
        return gasCost;
    }

    public void setGasCost(int gasCost) {
        this.gasCost = gasCost;
    }

    public int getMineralCost() {
        return mineralCost;
    }

    public void setMineralCost(int mineralCost) {
        this.mineralCost = mineralCost;
    }

    public int getSuppliesCost() {
        return suppliesCost;
    }

    public void setSuppliesCost(int suppliesCost) {
        this.suppliesCost = suppliesCost;
    }

    public List<Class> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<Class> requirements) {
        this.requirements = requirements;
    }

    public Class getProductionBlocks() {
        return productionBlocks;
    }

    public void setProductionBlocks(Class blocks) {
        this.productionBlocks = blocks;
    }

    public int getSuppliesGain() {
        return suppliesGain;
    }

    public void setSuppliesGain(int suppliesGain) {
        this.suppliesGain = suppliesGain;
    }

    public int getGasGain() {
        return gasGain;
    }

    public void setGasGain(int gasGain) {
        this.gasGain = gasGain;
    }

    public int getGasGainTime() {
        return gasGainTime;
    }

    public void setGasGainTime(int gasGainTime) {
        this.gasGainTime = gasGainTime;
    }

    public int getMineralGain() {
        return mineralGain;
    }

    public void setMineralGain(int mineralGain) {
        this.mineralGain = mineralGain;
    }

    public int getMineralGainTime() {
        return mineralGainTime;
    }

    public void setMineralGainTime(int mineralGainTime) {
        this.mineralGainTime = mineralGainTime;
    }

    public UnitState getUnitStateAfterProduction() {
        return unitStateAfterProduction;
    }

    public void setUnitStateAfterProduction(UnitState unitStateAfterProduction) {
        this.unitStateAfterProduction = unitStateAfterProduction;
    }
}
