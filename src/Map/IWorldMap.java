package Map;

import Elements.Animal;
import Elements.Grass;
import Statistics.SimulationStatistic;
import Unit.Vector2d;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public interface IWorldMap {

    boolean canMoveTo(Vector2d position);

    boolean isOccupied(Vector2d position);

    Map<Vector2d, Set<Animal>> retAnimals();

    Vector2d emptyFieldArround(Vector2d position);

    ArrayList<Animal> retStrongestAnimals(Vector2d placement);

    boolean isGrass(Vector2d position);

    Vector2d emptyFieldOutJungle();

    Vector2d emptyFieldInJungle();

    void addGrass(Vector2d vect1);

    void removeGrass(Vector2d key);

    Vector2d emptyField();

    Map<Vector2d, Grass> retGrass();

    int getWidth();
    int getHeight();
    int getWidthJungle();
    int getHeightJungle();
    int getHeightJungleEnd();
    int getWidthJungleEnd();

    SimulationStatistic getStatistics();

    void setDay(int day);
    int getDay();


}