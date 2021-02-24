package Elements;

import Unit.Vector2d;

public abstract class AbstractWorldMapElement implements IMapElement {
    protected Vector2d placement = new Vector2d(2,2);
    public Vector2d getPosition() {
        return this.placement;
    }
}
