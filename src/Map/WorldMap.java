package Map;

import Elements.Animal;
import Elements.Grass;
import Engine.IPositionChangeObserver;
import Statistics.SimulationStatistic;
import Unit.MapDirection;
import Unit.Vector2d;

import java.util.*;


public class WorldMap implements IWorldMap, IPositionChangeObserver {
    protected Map<Vector2d, Set<Animal>> animals = new HashMap<>();
    protected Map<Vector2d, Grass> grasses = new HashMap<>();
    public int day = 1;
    private final int width;
    private final int height;
    private final int widthJungle;
    private final int heightJungle;
    private final int widthJungleEnd;
    private final int heightJungleEnd;
    private final int startEnergy;
    private SimulationStatistic statistic;

    public WorldMap(int numberOfAnimals, int numberOfGrassess, int width, int height, int widthJungle, int heightJungle, int startEnergy) {
        this.statistic = new SimulationStatistic();
        this.width = width;
        this.height = height;
        this.widthJungle = this.width/2 - widthJungle/2+1;
        this.heightJungle = this.height/2 - heightJungle/2+1;
        this.widthJungleEnd = this.widthJungle + widthJungle-1;
        this.heightJungleEnd = this.heightJungle + heightJungle-1;
        this.startEnergy = startEnergy;
        Random r = new Random();
        int x, y;
        for(int i = 0; i < numberOfAnimals; i++) {
            x = r.nextInt(width);
            y = r.nextInt(height);
            while(isOccupied(new Vector2d(x, y))) {
                x = r.nextInt(width);
                y = r.nextInt(height);
            }
            Animal animal = new Animal(this, new Vector2d(x, y), this.startEnergy);

        }
        for(int i = 0; i < numberOfGrassess; i++) {

            x = r.nextInt(width);
            y = r.nextInt(height);
            while(isOccupied(new Vector2d(x, y))) {
                x = r.nextInt(width);
                y = r.nextInt(height);
            }
            Grass grass = new Grass(new Vector2d(x, y));
            this.grasses.put(grass.getPosition(), grass);
            this.statistic.addGrass();
        }

        System.out.println();
    }
    @Override
    public boolean isOccupied(Vector2d position) {
        if (animals.containsKey(position))
            return true;
        return grasses.containsKey(position);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows(new Vector2d(1, 1)) && position.precedes(new Vector2d(this.width, this.height));
    }

    @Override
    public void addAnimal(Vector2d position, Animal animal) {
        if(!animals.containsKey(animal.getPosition())) {
            animals.put(animal.getPosition(), new HashSet<>());
        }
        this.animals.get(animal.getPosition()).add(animal);
    }

    @Override
    public void changeEnergy(int value) {

    }

    @Override
    public void changeChildren(int i) {

    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        animals.get(oldPosition).remove(animal);
        if(animals.get(oldPosition).isEmpty())
            animals.remove(oldPosition);
        if(!animals.containsKey(newPosition)) {
            animals.put(newPosition, new HashSet<>());
        }
        animals.get(newPosition).add(animal);
    }

    @Override
    public void deadAnimal(Vector2d position, Animal animal) {
        this.animals.get(position).remove(animal);
        if(this.animals.get(position).isEmpty())
            this.animals.remove(position);
    }

    public Map<Vector2d, Set<Animal>> retAnimals() {
        return animals;
    }

    public Map<Vector2d, Grass> retGrass() {
        return grasses;
    }

    public Vector2d emptyFieldArround(Vector2d position) {
        Vector2d[] fields = {position.add(MapDirection.EAST.toUnitVector()), position.add(MapDirection.NORTH_EAST.toUnitVector()), position.add(MapDirection.SOUTH_EAST.toUnitVector()), position.add(MapDirection.NORTH.toUnitVector())
                            , position.add(MapDirection.SOUTH.toUnitVector()), position.add(MapDirection.SOUTH_WEST.toUnitVector()), position.add(MapDirection.NORTH_WEST.toUnitVector()), position.add(MapDirection.WEST.toUnitVector())};
        ArrayList<Vector2d> fieldsvs = new ArrayList(Arrays.asList(fields));
        for(Vector2d vect: fields) {
            if(vect.x < 1 || vect.y < 1 || vect.x > width || vect.y > height) {
                fieldsvs.remove(vect);
            }
        }
        for(Vector2d vect: fieldsvs) {
            if(!animals.containsKey(vect) && !grasses.containsKey(vect))
                return vect;
        }
        return null;
    }

    public ArrayList<Animal> retStrongestAnimals(Vector2d placement) {
        ArrayList<Animal> strongAnimals = new ArrayList<>();
        int highestPower = 0;
        if(!this.animals.containsKey(placement)) return null;
        for(Animal animal: this.animals.get(placement)) {
            if(animal.getEnergy() > highestPower)
                highestPower = animal.getEnergy();
        }
        for(Animal animal: this.animals.get(placement)) {
            if(animal.getEnergy() == highestPower)
                strongAnimals.add(animal);
        }
        return strongAnimals;
    }

    public boolean isGrass(Vector2d position) {
        return this.grasses.containsKey(position);
    }

    public Vector2d emptyFieldOutJungle() {
        Random r = new Random();
        int x = r.nextInt(this.width);
        int y = r.nextInt(this.height);
        Vector2d field = new Vector2d(x, y);
        int counter = 0;
        while((animals.containsKey(field) || grasses.containsKey(field) || (new Vector2d(x, y).follows(new Vector2d(widthJungle, heightJungle)) && new Vector2d(x, y).precedes(new Vector2d(widthJungleEnd, heightJungleEnd)) )) && counter < 50) {
            x = r.nextInt(this.width);
            y = r.nextInt(this.height);
            counter++;
        }
        if(counter < 50)
            return new Vector2d(x, y);
        else {
            for(int i = 0; i < width; i++) {
                for(int j = 0; j < height; j++) {
                    if((i < widthJungle || i > widthJungleEnd) && (j < heightJungle || j > heightJungleEnd)) {
                        if(!animals.containsKey(new Vector2d(i, j)) && !grasses.containsKey(new Vector2d(i, j)))
                            return new Vector2d(i, j);
                    }
                }
            }
        }

        return null;
    }

    public Vector2d emptyFieldInJungle() {
        Random r = new Random();
        int x = widthJungle + r.nextInt(widthJungleEnd-widthJungle+1);
        int y = heightJungle + r.nextInt(heightJungleEnd - heightJungle + 1);
        Vector2d field = new Vector2d(x, y);
        int counter = 0;
        while((animals.containsKey(field) || grasses.containsKey(field)) && counter < 40) {
            x = widthJungle + r.nextInt(widthJungleEnd-widthJungle+1);
            y = heightJungle + r.nextInt(heightJungleEnd - heightJungle + 1);
            counter++;
        }
        if(counter < 40)
            return new Vector2d(x, y);
        else {
            for(int i = widthJungle; i < widthJungleEnd; i++) {
                for(int j = heightJungle; j < heightJungleEnd; j++) {
                    if(!animals.containsKey(new Vector2d(i, j)) && !grasses.containsKey(new Vector2d(i, j)))
                        return new Vector2d(i, j);
                }
            }
        }

        return null;
    }

    @Override
    public Vector2d emptyField() {
        Random r = new Random();
        int x = r.nextInt(width);
        int y = r.nextInt(height);
        Vector2d field = new Vector2d(x, y);
        int counter = 0;
        while((animals.containsKey(field) || grasses.containsKey(field)) && counter < 20) {
            x = r.nextInt(width);
            y = r.nextInt(height);
            counter++;
        }
        if(counter < 20)
            return new Vector2d(x, y);
        else {
            for(int i = 0; i < width; i++) {
                for(int j = 0; j < height; j++) {
                    if(!animals.containsKey(new Vector2d(i, j)) && !grasses.containsKey(new Vector2d(i, j)))
                        return new Vector2d(i, j);
                }
            }
        }

        return null;
    }


    @Override
    public void addGrass(Vector2d vect1) {
        this.grasses.put(vect1, new Grass(vect1));
        this.statistic.addGrass();
    }

    @Override
    public void removeGrass(Vector2d key) {
        this.grasses.remove(key);
        this.statistic.removeGrass();
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidthJungle() {return widthJungle;}

    @Override
    public int getHeightJungle() {return heightJungle;}

    @Override
    public int getWidthJungleEnd() {return widthJungleEnd;}

    @Override
    public int getHeightJungleEnd() {return heightJungleEnd;}

    @Override
    public SimulationStatistic getStatistics() {
        return statistic;
    }

    @Override
    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public int getDay() {
        return this.day;
    }

}
