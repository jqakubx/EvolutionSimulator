package Elements;

import Unit.Vector2d;

public class Grass extends AbstractWorldMapElement {

    public Grass(Vector2d placement) {
        this.placement = placement;
    }

    @Override
    public String toString() {
        return "*";
    }


}
