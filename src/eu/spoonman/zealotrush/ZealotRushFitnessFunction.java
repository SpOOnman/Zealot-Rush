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

	@Override
	protected double evaluate(IChromosome a_subject) {
		int errors = 0;

		Gene[] genes = a_subject.getGenes();
                
                List<Integer> orders = new ArrayList<Integer>();
                for(Gene gene : genes) {
                    orders.add((Integer) gene.getAllele());
                    
                }
                Player player = new Player();
                player.simulate(300, orders);
                int zealots = player.getUnitCount(ZealotInfo.class);

                return (5 - zealots) * player.getTargetTimeStamp();
	}

}
