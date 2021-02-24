package Statistics;

import Elements.Animal;
import Elements.Genotype;
import Elements.Grass;
import Engine.IPositionChangeObserver;
import Unit.Vector2d;

import java.util.HashMap;
import java.util.Map;

public class SimulationStatistic implements IPositionChangeObserver {
    private HashMap<Genotype, Integer> genotypes = new HashMap<>();
    int aliveAnimals;

    public HashMap<Genotype, Integer> getGenotypes() {
        return genotypes;
    }


    int grassess;
    int allEnergy;
    int deadAnimal;
    int daysDeadAnimals;
    int children = 0;

    public SimulationStatistic() {
        this.aliveAnimals = 0;
        this.grassess = 0;
        this.allEnergy = 0;
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {

    }

    @Override
    public void deadAnimal(Vector2d position, Animal animal) {
        this.aliveAnimals -= 1;
        this.deadAnimal += 1;
        this.daysDeadAnimals += (animal.getDeathDay()-animal.getBirthDay());
        Genotype genotype = animal.getGenotype();
        if(this.genotypes.get(genotype) == 1)
            this.genotypes.remove(genotype);
        else
            this.genotypes.put(genotype, this.genotypes.get(genotype)-1);
    }

    @Override
    public void addAnimal(Vector2d position, Animal animal) {
        this.aliveAnimals += 1;
        Genotype genotype = animal.getGenotype();
        if(!this.genotypes.containsKey(genotype))
            this.genotypes.put(genotype, 1);
        else
            this.genotypes.put(genotype, this.genotypes.get(genotype)+1);
    }

    @Override
    public void changeEnergy(int value) {
        this.allEnergy += value;
    }

    @Override
    public void changeChildren(int i) {
        this.children += 1;
    }

    public int getAliveAnimals() {
        return aliveAnimals;
    }

    public void addGrass() {
        this.grassess += 1;
    }

    public void removeGrass() {
        this.grassess -= 1;
    }

    public int getGrassess() {
        return grassess;
    }

    public double getAverageEnergy() {
        if(aliveAnimals == 0 || allEnergy <= 0) return 0;
        double k = allEnergy/(double)aliveAnimals;
        k *= 100;
        k = Math.round(k);
        k /= 100;
        return k;
    }

    public double getAverageLiveDeathAnimals() {
        if(deadAnimal == 0 || daysDeadAnimals <= 0) return 0;
        double k = daysDeadAnimals/(double)deadAnimal;
        k *= 100;
        k = Math.round(k);
        k /= 100;
        return k;
    }

    public double getAverageAmountOfChildren() {
        if(children == 0 | aliveAnimals == 0) return 0;
        double k = children/(double)aliveAnimals;
        k *= 100;
        k = Math.round(k);
        k /= 100;
        return k;
    }

    public String getMostCommonGenotype() {
        int most = 0;
        Genotype genotype = null;
        for (Map.Entry<Genotype, Integer> entry: genotypes.entrySet()) {
            if(entry.getValue() > most) {
                most = entry.getValue();
                    genotype = entry.getKey();
            }
        }
        if(genotype == null) return "Brak genotypów";
        String geno = genotype.toString() + " " + most;
        return geno;
    }

    public Genotype getMostGeno() {
        int most = 0;
        Genotype genotype = null;
        for (Map.Entry<Genotype, Integer> entry: genotypes.entrySet()) {
            if(entry.getValue() > most) {
                most = entry.getValue();
                genotype = entry.getKey();
            }
        }
        return genotype;
    }


}
