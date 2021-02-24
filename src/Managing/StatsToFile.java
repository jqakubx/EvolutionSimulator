package Managing;

import Elements.Genotype;
import Statistics.SimulationStatistic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatsToFile {
    ArrayList<SimulationStatistic> statisticList = new ArrayList<>();
    int sim;
    public StatsToFile(int n) {
        this.sim = n;
    }
    public void add(SimulationStatistic stats) {
        this.statisticList.add(stats);
    }

    public void toFile(int N) throws IOException {


        File myObj = new File("statystyki.txt");
        FileWriter fileWriter = new FileWriter("statystyki.txt");
        fileWriter.write("Statystki wykonane dla " + N + " pierwszych epok dla symulacji " + this.sim);
        if(N > statisticList.size())
            fileWriter.write("Zła liczba epok.");
        else {
            double avgAnimals = 0;
            double avgAnimalsAll;
            for (int i = 0; i < N; i++) {
                avgAnimals += statisticList.get(i).getAliveAnimals();
            }
            avgAnimalsAll = avgAnimals / (double) N;
            avgAnimalsAll *= 100;
            avgAnimalsAll = Math.round(avgAnimalsAll);
            avgAnimalsAll /= 100;
            fileWriter.write(String.format("%n") + "Średnia liczba zwierząt: " + avgAnimalsAll);

            double avgGrass = 0;
            double avgGrassAll;
            for (int i = 0; i < N; i++) {
                avgGrass += statisticList.get(i).getGrassess();
            }
            avgGrassAll = avgGrass / (double) N;
            avgGrassAll *= 100;
            avgGrassAll = Math.round(avgGrassAll);
            avgGrassAll /= 100;
            fileWriter.write(String.format("%n") + "Średnia liczba roślin: " + avgGrassAll);

            double avgEnergy = 0;
            double avgEnergyAll;
            for (int i = 0; i < N; i++) {
                avgEnergy += statisticList.get(i).getAverageEnergy();
            }
            avgEnergyAll = avgEnergy / (double) N;
            avgEnergyAll *= 100;
            avgEnergyAll = Math.round(avgEnergyAll);
            avgEnergyAll /= 100;
            fileWriter.write(String.format("%n") + "Średnia energia zwierząt: " + avgEnergyAll);

            double avgLive = 0;
            double avgLiveAll;
            for (int i = 0; i < N; i++) {
                avgLive += statisticList.get(i).getAverageLiveDeathAnimals();
            }
            avgLiveAll = avgLive / (double) N;
            avgLiveAll *= 100;
            avgLiveAll = Math.round(avgLiveAll);
            avgLiveAll /= 100;
            fileWriter.write(String.format("%n") + "Średnia długość życia martwych zwierząt: " + avgLiveAll);

            double avgChild = 0;
            double avgChildAll;
            for (int i = 0; i < N; i++) {
                avgChild += statisticList.get(i).getAverageAmountOfChildren();
            }
            avgChildAll = avgChild / (double) N;
            avgChildAll *= 100;
            avgChildAll = Math.round(avgChildAll);
            avgChildAll /= 100;
            fileWriter.write(String.format("%n") + "Średnia ilość dzieci dla żyjących zwierząt: " + avgChildAll);

            HashMap<Genotype, Integer> genotypes = new HashMap<>();
            for(int i = 0; i < N; i++) {
                HashMap<Genotype, Integer> genotypes2 = statisticList.get(i).getGenotypes();
                for(Map.Entry<Genotype, Integer> entry : genotypes2.entrySet()) {
                    if(genotypes.containsKey(entry.getKey()))
                        genotypes.put(entry.getKey(), entry.getValue() + genotypes.get(entry.getKey()));
                    else
                        genotypes.put(entry.getKey(), entry.getValue());
                }
            }
            int max = 0;
            Genotype genotype = null;
            for(Map.Entry<Genotype, Integer> entry: genotypes.entrySet()) {
                if(entry.getValue() > max) {
                    max = entry.getValue();
                    genotype = entry.getKey();
                }
            }

            fileWriter.write(String.format("%n") + "Najczęstszy genotyp: " + genotype.toString());
        }
        fileWriter.close();
    }
}
