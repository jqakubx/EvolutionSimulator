package Elements;

import Engine.*;
import Map.IWorldMap;
import Unit.MapDirection;
import Unit.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Animal extends AbstractWorldMapElement {
    public static int actId = 0;
    public static int moveEnergy = 0;
    private final Genotype genotype;
    private final int id;
    private final int birth;
    private int death = 0;
    private Animal parent1 = null;
    private Animal parent2 = null;
    private int energy;
    private int children = 0;
    private MapDirection orientation = MapDirection.randomDirection();
    private final List<IPositionChangeObserver> observers = new ArrayList<>();
    public IWorldMap map;
    private ArrayList<Animal> childrenList = new ArrayList<>();

    public Animal(IWorldMap map, Vector2d initialPosition, int startEnergy) {
        this.map = map;
        this.placement = initialPosition;
        this.id = Animal.actId;
        this.energy = startEnergy;
        Animal.actId++;
        this.genotype = new Genotype();
        this.observers.add((IPositionChangeObserver) this.map);
        this.observers.add(map.getStatistics());
        notifyAdd();
        notifyEnergy(startEnergy);
        this.birth = map.getDay();
    }

    public Animal(Animal animal1, Animal animal2, Vector2d placement) {
        this.genotype = new Genotype(animal1, animal2);
        this.placement = placement;
        this.id = Animal.actId;
        actId++;
        this.map = animal1.map;
        this.energy = animal1.getEnergy()/4 + animal2.getEnergy()/4;
        animal1.energy -= animal1.getEnergy()/4;
        animal2.energy -= animal2.getEnergy()/4;
        this.observers.add((IPositionChangeObserver) this.map);
        this.observers.add(map.getStatistics());
        notifyAdd();
        this.birth = map.getDay();
        animal1.setAmountChildren(animal1.getAmountChildren()+1);
        animal2.setAmountChildren(animal2.getAmountChildren()+1);
        notifyChildren(2);
        animal1.addChildrenToList(this);
        animal2.addChildrenToList(this);
    }

    public void addChildrenToList(Animal animal) {
        this.childrenList.add(animal);
    }



    public void move() {
        this.orientation = MapDirection.go(this.orientation, this.genotype.randomOrientation());
        if(map.canMoveTo(placement.add(orientation.toUnitVector()))) {
            this.placement = placement.add(orientation.toUnitVector());
            this.notifyObservers(placement.substract(orientation.toUnitVector()), placement);
        }
        this.energy -= Animal.moveEnergy;
        notifyEnergy(-1*Animal.moveEnergy);
    }

    public void notifyObservers(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observer: observers) {
            observer.positionChanged(oldPosition, newPosition, this);
        }
    }

    public void notifyChildren(int value) {
        for (IPositionChangeObserver observer: observers) {
            observer.changeChildren(value);
        }
    }
    public void notifyDead() {
        this.death = map.getDay();
        for (IPositionChangeObserver observer: observers) {
            observer.deadAnimal(this.getPosition(), this);
        }
        notifyEnergy(-1*this.energy);
    }

    public void notifyAdd() {
        for (IPositionChangeObserver observer: observers) {
            observer.addAnimal(this.getPosition(), this);
        }
    }

    public void notifyEnergy(int value) {
        for (IPositionChangeObserver observer: observers) {
            observer.changeEnergy(value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return id == animal.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Genotype getGenotype() {
        return this.genotype;
    }

    public int getEnergy() {
        return energy;
    }

    public void addEnergy(int food) {
        this.energy += food;
        notifyEnergy(food);
    }

    public int getBirthDay() {
        return birth;
    }

    public int getDeathDay() {
        return death;
    }

    public int getAmountChildren() {
        return this.children;
    }

    public void setAmountChildren(int value) {
        this.children = value;
    }

    public Animal getParent1() {
        return parent1;
    }

    public Animal getParent2() {
        return parent2;
    }

    public int getAmounOfOldChildrenN(int N) {
        if(this.birth > N) return 0;
        if(this.birth == N) return 1;
        int sum = 0;
        for(Animal animal: childrenList) {
            sum += animal.getAmounOfOldChildrenN(N);
        }
        return sum+1;

    }

    public int getAmountOfChildrenAfterN(int nAnimalsN) {
        int sum = 0;
        for(Animal animal: childrenList) {
            if(animal.getBirthDay() <= nAnimalsN)
                sum ++;
        }
        return sum;
    }
}
