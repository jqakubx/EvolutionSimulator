package Elements;

import java.util.Comparator;

public class AnimalEnergyComp implements Comparator<Animal> {

    @Override
    public int compare(Animal anim1, Animal anim2) {
        if(anim1.getEnergy() < anim2.getEnergy()) {
            return 1;
        }
        else {
            return -1;
        }
    }
}
