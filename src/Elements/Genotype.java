package Elements;

import java.util.*;

public class Genotype {
    List<Integer> genes = new ArrayList<>();
    public Genotype() {
        Random r = new Random();
        int gene;
        for(int i = 0; i < 32; i++) {
            gene = r.nextInt(8);
            this.genes.add(gene);
        }
        makeCorrect();
    }

    public Genotype(Animal animal1, Animal animal2) {
        Genotype genotype1 = animal1.getGenotype();
        Genotype genotype2 = animal2.getGenotype();
        Random r = new Random();
        int first = r.nextInt(30) + 1;
        int second = r.nextInt(31-first) + first;
        for(int i = 0; i < first; i++) {
            this.genes.add(genotype1.getGene(i));
        }
        for(int i = first; i < second; i++) {
            this.genes.add(genotype1.getGene(i));
        }
        for(int i = second; i < 32; i++) {
            this.genes.add(genotype2.getGene(i));
        }
        makeCorrect();
    }

    private void makeCorrect() {
        List<Integer> numbers = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
        Random r  = new Random();
        while(!this.genes.containsAll(numbers)) {
            genes.set(r.nextInt(32), r.nextInt(8));
        }
        Collections.sort(this.genes);
    }

    public int getGene(int i) {
        return this.genes.get(i);
    }

    public int randomOrientation() {
        Random r = new Random();
        int a = r.nextInt(32);
        return this.genes.get(a);
    }

    @Override
    public String toString() {
        String result = "";
        for(int i = 0; i < 32; i++)
            result += String.valueOf(genes.get(i));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genotype genotype = (Genotype) o;
        return Objects.equals(genes, genotype.genes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genes);
    }
}
