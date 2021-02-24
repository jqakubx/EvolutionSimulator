package Unit;

import java.util.Random;

public enum MapDirection {
    NORTH,
    SOUTH,
    WEST,
    EAST,
    NORTH_EAST,
    NORTH_WEST,
    SOUTH_EAST,
    SOUTH_WEST;


    public static MapDirection randomDirection() {
        Random r = new Random();
        int number = r.nextInt(8);
        return MapDirection.values()[number];
    }


    public static MapDirection go(MapDirection direction, int val) {
        int a = direction.ordinal() + val;
        a %= 8;
        return MapDirection.values()[a];
    }


    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2d(0,1);
            case EAST -> new Vector2d(1,0);
            case SOUTH -> new Vector2d(0,-1);
            case WEST -> new Vector2d(-1, 0);
            case NORTH_EAST -> new Vector2d(1, 1);
            case NORTH_WEST -> new Vector2d(-1, 1);
            case SOUTH_EAST -> new Vector2d(1, -1);
            case SOUTH_WEST -> new Vector2d(-1, -1);
        };
    }
}
