/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.spoonman.zealotrush;

import nl.jamiecraane.gahelloworld.GaHelloWorld;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.DeltaFitnessEvaluator;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;

/**
 *
 * @author spoonman
 */
public class ZealotRushGA {

    private static final int CHROME_SIZE = 30;
    private static final int EVOLUTIONS = 3000;

    public ZealotRushGA() throws Exception {
        Genotype genotype = this.setupGenoType();
        this.evolve(genotype);
    }

    private void evolve(Genotype genotype) {
        String solution = this.getSolution(genotype.getFittestChromosome());
        System.out.println(solution);

        double previousFitness = Double.MAX_VALUE;
        int numEvolutions = 0;
        for (int i = 0; i < EVOLUTIONS; i++) {
            genotype.evolve();
            double fitness = genotype.getFittestChromosome().getFitnessValue();
            if (fitness < previousFitness) {
                previousFitness = fitness;
                solution = this.getSolution(genotype.getFittestChromosome());
                solution = this.getSolution(genotype.getFittestChromosome());
                System.out.println(solution);
            }

//            if (solution.equals(TARGET)) {
//                numEvolutions = i;
//                break;
//            }

            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
            }
        }

        solution = this.getSolution(genotype.getFittestChromosome());
        solution = this.getSolution(genotype.getFittestChromosome());
        System.out.println(solution);
        System.out.println("Needed [" + numEvolutions + "] evolutions for this");
    }

    private String getSolution(IChromosome a_subject) {
        StringBuffer solution = new StringBuffer();

        Gene[] genes = a_subject.getGenes();
        for (int i = 0; i < genes.length; i++) {
            Integer allele = (Integer) genes[i].getAllele();
            solution.append(allele);
        }

        return solution.toString();
    }

    private Genotype setupGenoType() throws Exception {
        Configuration gaConf = new DefaultConfiguration();
        gaConf.resetProperty(Configuration.PROPERTY_FITEVAL_INST);
        gaConf.setFitnessEvaluator(new DeltaFitnessEvaluator());

        gaConf.setPreservFittestIndividual(true);
        gaConf.setKeepPopulationSizeConstant(false);

        gaConf.setPopulationSize(50);

        int chromeSize = CHROME_SIZE;

        IntegerGene gene = new IntegerGene(gaConf, 0, 4);

        IChromosome sampleChromosome = new Chromosome(gaConf, gene, chromeSize);
        gaConf.setSampleChromosome(sampleChromosome);

        gaConf.setFitnessFunction(new ZealotRushFitnessFunction());

        return Genotype.randomInitialGenotype(gaConf);
    }

    public static void main(String[] args) throws Exception {
        new ZealotRushGA();
    }
}

