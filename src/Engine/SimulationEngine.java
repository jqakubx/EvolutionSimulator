package Engine;

import Elements.Animal;
import Elements.AnimalEnergyComp;
import Elements.Grass;
import Map.IWorldMap;
import Unit.Vector2d;

import java.util.*;

public class SimulationEngine implements IEngine {
    IWorldMap map;
    private final int plantEnergy;
    private final int energyToReproduce;
    private int day = 1;
    private Animal lookedAnime = null;
    private boolean showGen = false;

    public SimulationEngine(IWorldMap map, int plantEnergy, int startEnergy) {
        this.map = map;
        this.plantEnergy = plantEnergy;
        this.energyToReproduce = startEnergy/2;
    }

    @Override
    public void setLookedAnimal(Animal animal) {
        this.lookedAnime = animal;
    }

    @Override
    public void makeTurn() {
        day+=1;
        map.setDay(this.day);
        Map<Vector2d, Set<Animal>> animals = getAllAnimals();
        deleteDeadAnimals(animals);
        animals = getAllAnimals();
        moveAnimals(animals);
        animals = getAllAnimals();
        feedAnimals(animals);
        animals = getAllAnimals();
        addAnimals(animals);
        addGrass();

        //System.out.println(Animal.aliveAnimals);
    }

    private void moveAnimals(Map<Vector2d, Set<Animal>> animals) {
        for(Map.Entry<Vector2d, Set<Animal>> entry : animals.entrySet()) {
            for(Animal animal: entry.getValue()) {
                animal.move();
            }
        }
    }
    private void deleteDeadAnimals(Map<Vector2d, Set<Animal>> animals) {
        for(Map.Entry<Vector2d, Set<Animal>> entry : animals.entrySet()) {
            for(Animal animal: entry.getValue()) {
                if(animal.getEnergy() <= 0) {
                    animal.notifyDead();
                    animal.notifyChildren(-1*animal.getAmountChildren());
                }
            }
        }
    }
    private void feedAnimals(Map<Vector2d, Set<Animal>> animals) {
        int feed;

        ArrayList<Animal> strongAnimals;
        for(Map.Entry<Vector2d, Set<Animal>> entry : animals.entrySet()) {
            if(map.isGrass(entry.getKey())) {

                strongAnimals = map.retStrongestAnimals(entry.getKey());
                feed = this.plantEnergy / strongAnimals.size();
                for (Animal animal : strongAnimals) {

                    animal.addEnergy(feed);
                }
                map.removeGrass(entry.getKey());
            }

        }
    }

    private void addAnimals(Map<Vector2d, Set<Animal>> animals) {
        List<Animal> strongestANimals;
        for (Map.Entry<Vector2d, Set<Animal>> entry: animals.entrySet()) {
            if(animals.get(entry.getKey()).size() >= 2) {
                strongestANimals = new ArrayList<>(animals.get(entry.getKey()));
                strongestANimals.sort(new AnimalEnergyComp());
                if(strongestANimals.get(0).getEnergy() >= energyToReproduce && strongestANimals.get(1).getEnergy() >= energyToReproduce) {
                    Vector2d positionBaby = map.emptyFieldArround(strongestANimals.get(0).getPosition());
                    if(positionBaby== null) {
                        positionBaby = map.emptyField();
                    }
                    if(positionBaby != null) {
                        Animal babyAnimal = new Animal(strongestANimals.get(0), strongestANimals.get(1), positionBaby);
                    }
                }
            }
        }
    }

    private void addGrass() {
        Vector2d vect1, vect2;
        vect1 = map.emptyFieldOutJungle();
        vect2 = map.emptyFieldInJungle();
        if(vect1 != null) this.map.addGrass(vect1);
        if(vect2 != null) this.map.addGrass(vect2);
    }

    private Map<Vector2d, Set<Animal>> getAllAnimals() {
        Map<Vector2d, Set<Animal>> oldAnimals = map.retAnimals();
        Map<Vector2d, Set<Animal>> newAnimals = new HashMap<>();
        for (Map.Entry<Vector2d, Set<Animal>> entry : oldAnimals.entrySet()) {
            newAnimals.put(entry.getKey(), new HashSet<>());
            for(Animal animal: oldAnimals.get(entry.getKey())) {
                newAnimals.get(entry.getKey()).add(animal);
            }
        }
        return newAnimals;
    }

    public IWorldMap getMap() {
        return map;
    }

    @Override
    public int[][] retAnimalPositions() {


        int[][] positions = new int[map.getWidth()+1][map.getHeight()+1];
        for(int i = 1; i <= map.getWidth(); i++) {
            for(int j = 1; j <= map.getHeight(); j++) {
                positions[i][j] = -100;
            }
        }
        Map<Vector2d, Set<Animal>> animals = getAllAnimals();
        Map<Vector2d, Grass> grassess = map.retGrass();
        for (Map.Entry<Vector2d, Grass> entry: grassess.entrySet()) {
            positions[entry.getKey().x][entry.getKey().y] = -80;
        }
        for (Map.Entry<Vector2d, Set<Animal>> entry: animals.entrySet()) {
            for(Animal animal: entry.getValue()) {
                if(animal.getEnergy() > positions[animal.getPosition().x][animal.getPosition().y])
                    positions[animal.getPosition().x][animal.getPosition().y] = animal.getEnergy();
                if(this.showGen && animal.getGenotype().equals(map.getStatistics().getMostGeno())) {
                    positions[animal.getPosition().x][animal.getPosition().y] = 88888;
                }
            }
        }
        if(lookedAnime != null) {
            if(lookedAnime.getDeathDay() != 0)
                lookedAnime = null;
            else
                positions[lookedAnime.getPosition().x][lookedAnime.getPosition().y] = 999999;
        }
        return positions;
    }

    public int[][] getFields() {
        int widthJungle = map.getWidthJungle();
        int widthEndJungle = map.getWidthJungleEnd();
        int heightJungle = map.getHeightJungle();
        int heightJungleEnd = map.getHeightJungleEnd();
        int[][] positions = new int[map.getWidth()+1][map.getHeight()+1];
        for(int i = 1; i <= map.getWidth(); i++) {
            for(int j = 1; j <= map.getHeight(); j++) {

                if(new Vector2d(i, j).follows(new Vector2d(widthJungle, heightJungle)) && new Vector2d(i, j).precedes(new Vector2d(widthEndJungle, heightJungleEnd)))
                    positions[i][j] = -90;
                else
                    positions[i][j] = -100;
            }

        }
        return positions;
    }

    public int getDay() {
        return day;
    }

    public String getGenotype(Vector2d vect) {
        ArrayList<Animal> animals = map.retStrongestAnimals(vect);
        if(animals == null) return "Brak zwierzecia. ";
        return animals.get(0).getGenotype().toString();
    }

    @Override
    public Animal getAnimalAlive(Vector2d vect) {
        ArrayList<Animal> animals = map.retStrongestAnimals(vect);
        if(animals == null) return null;
        return animals.get(0);
    }

    public void setGen() {
        this.showGen = !this.showGen;
    }
}
