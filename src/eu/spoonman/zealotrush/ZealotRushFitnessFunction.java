/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.spoonman.zealotrush;

import eu.spoonman.zealotrush.info.ZealotInfo;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.List;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.IChromosome;

/**
 *
 * @author spoonman
 */
public class ZealotRushFitnessFunction extends FitnessFunction {

    private int targetZealots = 0;

    public void setUpTargetZealots(int count) {
        this.targetZealots = count;
    }

    @Override
    protected double evaluate(IChromosome a_subject) {
        Gene[] genes = a_subject.getGenes();

        List<Integer> orders = new ArrayList<Integer>();
        for (Gene gene : genes) {
            orders.add((Integer) gene.getAllele());

        }
        Player player = new Player();
        player.simulate(300, orders, targetZealots);

        int zealots = player.getUnitCount(Player.ZEALOT);
        //System.out.println("Dla playera czas " + player.getSeconds() + " zealotow " + zealots);
        

        return (targetZealots + 1  - zealots) * player.getSeconds();
    }
}
