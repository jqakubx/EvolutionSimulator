package Engine;

import Elements.Animal;
import Unit.Vector2d;

public interface IPositionChangeObserver {
    void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal);
    void deadAnimal(Vector2d position, Animal animal);
    void addAnimal(Vector2d position, Animal animal);

    void changeEnergy(int value);

    void changeChildren(int i);
}
