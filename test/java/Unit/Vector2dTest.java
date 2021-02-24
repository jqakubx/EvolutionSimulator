package Unit;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {

    @Test
    void hashCodeTest() {
        Vector2d vector1 = new Vector2d(2, 2);
        Vector2d vector2 = new Vector2d(2, 2);
        assertEquals(vector1.hashCode(), vector2.hashCode());
    }

    @Test
    void equalsTest() {
        Vector2d vector1 = new Vector2d(2, 2);
        Vector2d vector2 = new Vector2d(2, 2);
        assertTrue(vector1.equals(vector2));
    }
}
