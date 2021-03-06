/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.spoonman.zealotrush;

import eu.spoonman.zealotrush.info.UnitInfo;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author spoonman
 */
public class Unit {

    private Player player;
    private Unit producer;
    private UnitInfo unitInfo;
    private UnitState unitState;
    private int timeInState;

    public Unit(Player player, UnitInfo unitInfo) {
        this.player = player;
        this.unitInfo = unitInfo;
    }

    public void productImmidiately() {
        changeState(this.unitInfo.getUnitStateAfterProduction());
    }

    public boolean isInProduction() {
        return this.unitState == UnitState.UNDER_CONSTRUCTION && this.timeInState < this.unitInfo.getProductionTime();
    }

    public void changeState(UnitState unitState) {
        this.getPlayer().eventUnitChangedState(this, this.getUnitState(), unitState, this.getTimeInState());
        this.unitState = unitState;
        this.timeInState = 0;
    }

    public void tick() {
        this.timeInState += 1;

        switch (this.unitState) {
            case GETHERING:
                tickGathering();
                break;
            case UNDER_CONSTRUCTION:
                tickIsInProduction();
                break;
            default:
                break;
        }
    }

    private void tickGathering() {
        if (this.unitInfo.getMineralGainTime() == 0)
            return;
        
        if (this.timeInState % (this.unitInfo.getMineralGainTime() + 1) == 0) {
            this.player.eventUnitGatheredMinerals(this, this.unitInfo.getMineralGain());
        }
    }

    private void tickIsInProduction() {
        // Did we finish production?
        if (this.timeInState == this.unitInfo.getProductionTime() - 1) {
            this.player.eventUnitHasBeenProduced(this);
            changeState(this.unitInfo.getUnitStateAfterProduction());

            if (this.producer != null)
                this.producer.changeState(UnitState.HOLD);
        }

    }

    @Override
    public String toString() {
        return this.getUnitInfo().toString();
    }

    

    public UnitInfo getUnitInfo() {
        return unitInfo;
    }

    public void setUnitInfo(UnitInfo unitInfo) {
        this.unitInfo = unitInfo;
    }

    public UnitState getUnitState() {
        return unitState;
    }

    public void setUnitState(UnitState unitState) {
        this.unitState = unitState;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Unit getProducer() {
        return producer;
    }

    public void setProducer(Unit producer) {
        this.producer = producer;
    }

    public int getTimeInState() {
        return timeInState;
    }

    public void setTimeInState(int timeInState) {
        this.timeInState = timeInState;
    }

    
}
