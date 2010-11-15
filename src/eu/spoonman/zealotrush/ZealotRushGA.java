/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.spoonman.zealotrush;

import java.util.List;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.DeltaFitnessEvaluator;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.impl.CrossoverOperator;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.MutationOperator;
import org.jgap.impl.SwappingMutationOperator;

/**
 *
 * @author spoonman
 */
public class ZealotRushGA {

    private static final int POPULATION_SIZE = 100;
    private static final int CHROME_SIZE = 15;
    private static final int EVOLUTIONS = 1000;

    public ZealotRushGA() throws Exception {
        Genotype genotype = this.setupGenoType();
        this.evolve(genotype);
    }

    private void evolve(Genotype genotype) {
        double previousFitness = Double.MAX_VALUE;
        for (int i = 0; i < EVOLUTIONS; i++) {
            long start = System.nanoTime();
            genotype.evolve();
            long end = System.nanoTime();
            long microseconds = (end - start) / 1000;
            double fitness = genotype.getFittestChromosome().getFitnessValue();
            if (false) {
                List<IChromosome> chr = genotype.getPopulation().getChromosomes();
                for (IChromosome c : chr) {
                    System.out.println("C " + getSolution(c));
                }

            }
            if (fitness < previousFitness) {
                previousFitness = fitness;
                System.out.println("Evo " + i + " solution " + this.getSolution(genotype.getFittestChromosome()) + " fit " + fitness + " took " + microseconds + "ms");
            }
        }
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

        //gaConf.setPreservFittestIndividual(true);
        //gaConf.setKeepPopulationSizeConstant(false);
        gaConf.setPreservFittestIndividual(true);
        gaConf.setKeepPopulationSizeConstant(false);

        gaConf.setPopulationSize(POPULATION_SIZE);
//        gaConf.getGeneticOperators().clear();
//        CrossoverOperator co = new CrossoverOperator(gaConf, 30.0, true);
//        MutationOperator  mo = new MutationOperator(gaConf, CHROME_SIZE/ 4);
//        gaConf.getGeneticOperators().add(co);
//        gaConf.getGeneticOperators().add(mo);
//        List geneticOperators = gaConf.getGeneticOperators();
//        for(Object o : geneticOperators)
//            System.out.println(o);

//        SwappingMutationOperator swapper = new SwappingMutationOperator(gaConf, 100);
//        MutationOperator mo = new MutationOperator(gaConf, 100);
//        CrossoverOperator co = new CrossoverOperator(gaConf, 100, true);
        SwappingMutationOperator swapper = new SwappingMutationOperator(gaConf, CHROME_SIZE);
        MutationOperator mo = new MutationOperator(gaConf, CHROME_SIZE * 1000);
        CrossoverOperator co = new CrossoverOperator(gaConf, CHROME_SIZE);
        gaConf.addGeneticOperator(co);
        gaConf.addGeneticOperator(swapper);
        gaConf.addGeneticOperator(mo);

        int chromeSize = CHROME_SIZE;

        IntegerGene gene = new IntegerGene(gaConf, 0, 4);

        IChromosome sampleChromosome = new Chromosome(gaConf, gene, chromeSize);
        gaConf.setSampleChromosome(sampleChromosome);

        ZealotRushFitnessFunction fitnessFunction = new ZealotRushFitnessFunction();
        fitnessFunction.setUpTargetZealots(4);
        gaConf.setFitnessFunction(fitnessFunction);

        return Genotype.randomInitialGenotype(gaConf);
    }

    public static void main(String[] args) throws Exception {
        new ZealotRushGA();
    }
}
