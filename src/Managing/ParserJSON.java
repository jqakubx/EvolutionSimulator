package Managing;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class ParserJSON {
    private static final String filePath = "src/Managing/parameters.json";
    private final int animals;
    private final int width;
    private final int height;
    private final int widthJungle;
    private final int heightJungle;
    private final int startEnergy;
    private final int moveEnergy;
    private final int plantEnergy;
    private final int grass;

    public int getGrass() {
        return grass;
    }
    public int getAnimals() {
        return animals;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getWidthJungle() {
        return widthJungle;
    }

    public int getHeightJungle() {
        return heightJungle;
    }

    public int getPlantEnergy() {return plantEnergy;}
    public int getStartEnergy() {
        return startEnergy;
    }

    public int getMoveEnergy() {
        return moveEnergy;
    }

    public ParserJSON() throws IllegalArgumentException, IOException, ParseException {
        FileReader reader = new FileReader(filePath);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
        long test = (long) jsonObject.get("animals");
        this.animals = (int) test;
        test = (long) jsonObject.get("width");
        this.width = (int) test;
        test = (long) jsonObject.get("height");
        this.height = (int) test;
        test = (long) jsonObject.get("widthJungle");
        this.widthJungle = (int) test;
        test = (long) jsonObject.get("heightJungle");
        this.heightJungle = (int) test;
        test = (long) jsonObject.get("startEnergy");
        this.startEnergy = (int) test;
        test = (long) jsonObject.get("moveEnergy");
        this.moveEnergy = (int) test;
        test = (long) jsonObject.get("grass");
        this.grass = (int) test;
        test = (long) jsonObject.get("plantEnergy");
        this.plantEnergy = (int) test;
    }
}
