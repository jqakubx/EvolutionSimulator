package Managing;

import Engine.IEngine;
import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class View {
    public static Group group1 = null;
    public static Group group2 = null;
    public static Group makeImages(IEngine simulation, int number) throws FileNotFoundException {

        ImageView imageView;
        Image image;
        ColorAdjust colorAdjust;
        Group gr = new Group();
        if(number == 1) {
            if (View.group1 == null) {
                View.group1 = new Group();
                int[][] map1 = simulation.getFields();
                for (int i = 1; i < map1.length; i++) {
                    for (int j = 1; j < map1[0].length; j++) {
                        image = new Image(new FileInputStream(valueToImage(map1[i][j])));
                        imageView = new ImageView(image);
                        imageView.setX((i - 1) * 16);
                        imageView.setY((j - 1) * 16);
                        View.group1.getChildren().add(imageView);
                    }
                }
            }
        }
        if(number == 3) {
            if (View.group2 == null) {
                View.group2 = new Group();
                int[][] map1 = simulation.getFields();
                for (int i = 1; i < map1.length; i++) {
                    for (int j = 1; j < map1[0].length; j++) {
                        image = new Image(new FileInputStream(valueToImage(map1[i][j])));
                        imageView = new ImageView(image);
                        imageView.setX((i - 1) * 16);
                        imageView.setY((j - 1) * 16);
                        View.group2.getChildren().add(imageView);
                    }
                }
            }
        }


        Group group1 = new Group();
        if(number == 1)
            group1.getChildren().add(View.group1);
        if(number == 3)
            group1.getChildren().add(View.group2);

        int[][] map = simulation.retAnimalPositions();
        for(int i = 1; i < map.length; i++) {
            for(int j = 1; j < map[0].length; j++) {
                if(map[i][j] >= -80) {
                    image = new Image(new FileInputStream(valueToImage(map[i][j])));
                    imageView = new ImageView(image);
                    imageView.setX((i - 1) * 16);
                    imageView.setY((j - 1) * 16);
                    if(map[i][j] >= 0) {
                        if(map[i][j] == 999999) {
                            colorAdjust = new ColorAdjust(-1, 1, 0, 0.15);
                            imageView.setEffect(colorAdjust);
                        }
                        if(map[i][j] == 88888) {
                            colorAdjust = new ColorAdjust(0.3, 0.7, 0.3, 0.15);
                            imageView.setEffect(colorAdjust);
                        }
                        if(map[i][j] < 5) {
                            colorAdjust = new ColorAdjust(-0.7, 1, 0, 0.35);
                            imageView.setEffect(colorAdjust);
                        }
                        else if(map[i][j] < 10) {
                            colorAdjust = new ColorAdjust(-0.5, 1, 0, 0.35);
                            imageView.setEffect(colorAdjust);
                        }
                        else if(map[i][j] < 15) {
                            colorAdjust = new ColorAdjust(-0.3, 1, 0, 0.35);
                            imageView.setEffect(colorAdjust);
                        }
                    }
                    group1.getChildren().add(imageView);
                }
            }
        }
        return group1;
    }

    public static String valueToImage(int value) {
        if(value == -100) return "resources/field.png";
        if(value == -90) return "resources/field2.png";
        if(value == -80) return "resources/grasst.png";
        return "resources/animalt.png";
    }
}
