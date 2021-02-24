package Engine;

import Elements.Animal;
import Map.IWorldMap;
import Unit.Vector2d;

public interface IEngine {
    void setLookedAnimal(Animal animal);
    void makeTurn();
    IWorldMap getMap();
    int[][] retAnimalPositions();
    int[][] getFields();
    int getDay();

    String getGenotype(Vector2d vect);
    Animal getAnimalAlive(Vector2d vector2d);

    void setGen();
}
