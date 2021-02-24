package Managing;

import Elements.Animal;
import Engine.IEngine;
import Engine.SimulationEngine;
import Map.IWorldMap;
import Map.WorldMap;
import Unit.Vector2d;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.json.simple.parser.ParseException;


import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends Application{
    private int width;
    private int height;
    private boolean start1 = true;
    private boolean start2 = true;
    private int nAnimalsN = 0;
    private int nParentsN = 0;
    private int toN = 0;
    private StatsToFile statsToFile1 = new StatsToFile(1);
    private StatsToFile statsToFile2 = new StatsToFile(2);

    TextField nParents = new TextField("0");
    TextField nAnimals = new TextField("0");
    TextField statsInput = new TextField("0");
    TextField statsInput2 = new TextField("0");
    @Override
    public void start(Stage stage) throws Exception {
        IEngine simulation = addSimulation();
        IEngine simulation2 = addSimulation();
        Button button1 = new Button("START/STOP");
        Button button2 = new Button("START/STOP");
        Button button3 = new Button("Genotyp");
        Button button4 = new Button("Genotyp");

        HBox buttons = new HBox(button1, button2);
        HBox buttons2 = new HBox(button3, button4);
        buttons.setSpacing(10);
        buttons.setAlignment(Pos.CENTER);
        buttons2.setSpacing(10);
        buttons2.setAlignment(Pos.CENTER);

        Group group = getView(simulation, 1, width);
        Group group2 = getView(simulation2, 3, width);
        group2.setLayoutX(width*2);
        Pane pane = new Pane(group, group2, getStatistics(simulation, simulation2, width, height, buttons, buttons2));

        stage.setTitle("Evolution Symulator");
        stage.show();
        stage.setScene(new Scene(pane, width*3, height));
        Stage stage1 = new Stage();
        stage1.setX(0.0);
        stage1.setY(0.0);
        final boolean[] isAnimalShow = {false};
        final Animal[] animal = new Animal[1];

        final Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), (a) -> {
            try {
                stage.setScene(new Scene(new Pane(getViewandUpdate(simulation, 1, width, start1), getViewandUpdate(simulation2, 3, width, start2), getStatistics(simulation, simulation2, width, height, buttons, buttons2)), width*3, height));
                if(!start1) {
                    stage.getScene().setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            int x = (int) mouseEvent.getX();
                            int y = (int) mouseEvent.getY();
                            if((x >= 0 && x < width && y >= 0 && y < height)) {

                                x = x/16 + 1;
                                y = y/16 + 1;

                                animal[0] = simulation.getAnimalAlive(new Vector2d(x, y));

                                if(animal[0] != null) {
                                    simulation.setLookedAnimal(animal[0]);
                                    nAnimals = new TextField("0");
                                    nParents = new TextField("0");
                                    isAnimalShow[0] = true;
                                    stage1.show();
                                }

                            }
                        }
                    });
                }
                if(!start2) {
                    stage.getScene().setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            int x = (int) mouseEvent.getX();
                            int y = (int) mouseEvent.getY();
                            if((x >= width*2 && x < width*3 && y >= 0 && y < height)) {
                                if (x > width) {
                                    x -= 2 * width;
                                }
                                x = x/16 + 1;
                                y = y/16 + 1;

                                animal[0] = simulation2.getAnimalAlive(new Vector2d(x, y));

                                if(animal[0] != null) {
                                    simulation2.setLookedAnimal(animal[0]);
                                    nAnimals = new TextField("0");
                                    nParents = new TextField("0");
                                    isAnimalShow[0] = true;
                                    stage1.show();
                                }

                            }
                        }
                    });
                }
                if(isAnimalShow[0]) {
                    if(animal[0] != null) {
                        Text genotype, alive;
                        Text childrenAfterNTurn, oldChildrenFromNTurn, childrenAfterNTurnValue, oldChildrenFromNTurnValue;
                        Text AnimalsTurn = new Text("Epoka do której liczone dzieci i potomkowie: ");
                        Text birthDate = new Text("Urodzone w " + String.valueOf(animal[0].getBirthDay()) + " epoce.");
                        childrenAfterNTurn = new Text("Liczba dzieci do " + nAnimalsN + " epoki");
                        oldChildrenFromNTurn = new Text("Licza potomków od " + nAnimalsN + " epoki");
                        childrenAfterNTurnValue = new Text(String.valueOf(animal[0].getAmountOfChildrenAfterN(nAnimalsN)));
                        oldChildrenFromNTurnValue = new Text(String.valueOf(animal[0].getAmounOfOldChildrenN(nAnimalsN)-1));
                        nAnimalsN = Integer.valueOf(nAnimals.getText());
                        nAnimals = new TextField(String.valueOf(nAnimalsN));

                        genotype = new Text(animal[0].getGenotype().toString());
                        if (animal[0].getDeathDay() == 0)
                            alive = new Text("Zwierze żyje");
                        else
                            alive = new Text("Zwierze zmarło w " + animal[0].getDeathDay() + " epoce.");
                        isAnimalShow[0] = true;
                        if(nAnimalsN == 0)
                            stage1.setScene(new Scene(new VBox(genotype, alive, birthDate, AnimalsTurn, nAnimals )));


                        else {
                            stage1.setScene(new Scene(new VBox(genotype, alive, birthDate, childrenAfterNTurn, childrenAfterNTurnValue, oldChildrenFromNTurn, oldChildrenFromNTurnValue)));
                        }

                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }));
        timeline.setDelay(Duration.seconds(1));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        button1.setOnAction(value -> this.StartStop(1));
        button2.setOnAction(value -> this.StartStop(2));
        button3.setOnAction(value -> simulation.setGen());
        button4.setOnAction(value -> simulation2.setGen());

        stage1.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        simulation.setLookedAnimal(null);
                        simulation2.setLookedAnimal(null);
                    }
                });
            }
        });
    }

    public void StartStop(int number) {
        if(number == 1)
            start1 = !start1;
        if(number == 2)
            start2 = !start2;
    }

    public Group getView(IEngine simulation, int count, int width) throws FileNotFoundException {
        Group group = View.makeImages(simulation, count);
        group.setLayoutX(width*(count-1));
        if(count == 1)
            statsToFile1.add(simulation.getMap().getStatistics());
        else
            statsToFile2.add(simulation.getMap().getStatistics());
        return group;
    }
    public Group getViewandUpdate(IEngine simulation, int count, int width, boolean start) throws FileNotFoundException {
        if(start)
            simulation.makeTurn();
        Group group = View.makeImages(simulation, count);
        group.setLayoutX(width*(count-1));
        if(count == 1)
            statsToFile1.add(simulation.getMap().getStatistics());
        else
            statsToFile2.add(simulation.getMap().getStatistics());
        return group;
    }

    public IEngine addSimulation() throws IOException, ParseException {
        ParserJSON loader = new ParserJSON();
        IWorldMap map = new WorldMap(loader.getAnimals(), loader.getGrass(), loader.getWidth(), loader.getHeight(),  loader.getWidthJungle(), loader.getHeightJungle(), loader.getStartEnergy());
        IEngine engine = new SimulationEngine(map, loader.getPlantEnergy(), loader.getStartEnergy());
        Animal.moveEnergy = loader.getMoveEnergy();
        this.width = loader.getWidth() * 16;
        this.height = loader.getHeight() * 16;
        return engine;
    }
    public VBox getStatistics(IEngine simulation, IEngine simulation2, int width, int height, HBox buttons, HBox buttons2){
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.BASELINE_CENTER);
        vbox.setPrefWidth(width);
        vbox.setPrefHeight(height);

        Text dayInfo = new Text("Epoka");
        Text day1 = new Text(String.valueOf(simulation.getDay()));
        Text day2 = new Text(String.valueOf(simulation2.getDay()));
        HBox day = new HBox(day1, day2);
        day.setSpacing(5);
        day.setAlignment(Pos.CENTER);

        Text amountAnimalInfo = new Text("Liczba żywych zwierząt");
        Text amountAnimal1 = new Text(String.valueOf(simulation.getMap().getStatistics().getAliveAnimals()));
        Text amountAnimal2 = new Text(String.valueOf(simulation2.getMap().getStatistics().getAliveAnimals()));
        HBox amountAnimal = new HBox(amountAnimal1, amountAnimal2);
        amountAnimal.setSpacing(5);
        amountAnimal.setAlignment(Pos.CENTER);

        Text amountGrassInfo = new Text("Liczba roślin");
        Text amountGrass1 = new Text(String.valueOf(simulation.getMap().getStatistics().getGrassess()));
        Text amountGrass2 = new Text(String.valueOf(simulation2.getMap().getStatistics().getGrassess()));
        HBox amountGrass = new HBox(amountGrass1, amountGrass2);
        amountGrass.setSpacing(5);
        amountGrass.setAlignment(Pos.CENTER);

        Text averageInfo = new Text("Średnia energia");
        Text average1 = new Text(String.valueOf(simulation.getMap().getStatistics().getAverageEnergy()));
        Text average2 = new Text(String.valueOf(simulation2.getMap().getStatistics().getAverageEnergy()));
        HBox average = new HBox(average1, average2);
        average.setSpacing(5);
        average.setAlignment(Pos.CENTER);

        Text averageLiveInfo = new Text("Średnia długość życia martwych zwierząt");
        Text averageLive1 = new Text(String.valueOf(simulation.getMap().getStatistics().getAverageLiveDeathAnimals()));
        Text averageLive2 = new Text(String.valueOf(simulation2.getMap().getStatistics().getAverageLiveDeathAnimals()));
        HBox averageLive = new HBox(averageLive1, averageLive2);
        averageLive.setSpacing(5);
        averageLive.setAlignment(Pos.CENTER);

        Text mostGenotypeInfo = new Text("Najczęstszy genotyp");
        Text mostGenotype1 = new Text(simulation.getMap().getStatistics().getMostCommonGenotype());
        Text mostGenotype2 = new Text(simulation2.getMap().getStatistics().getMostCommonGenotype());

        Text averageChildrenInfo = new Text("Średnia liczba dzieci dla żyjących zwierząt");
        Text averageChildren1 = new Text(String.valueOf(simulation.getMap().getStatistics().getAverageAmountOfChildren()));
        Text averageChildren2 = new Text(String.valueOf(simulation2.getMap().getStatistics().getAverageAmountOfChildren()));
        HBox averageChildren = new HBox(averageChildren1, averageChildren2);
        averageChildren.setSpacing(5);
        averageChildren.setAlignment(Pos.CENTER);

        if(Integer.valueOf(statsInput.getText()) > 0) {
            int v = Integer.valueOf(statsInput.getText());
            try {
                statsToFile1.toFile(v);
            } catch (IOException e) {
                System.out.println("Błąd w zapisie");
            }
        }

        if(Integer.valueOf(statsInput2.getText()) > 0) {
            int v = Integer.valueOf(statsInput2.getText());
            try {
                statsToFile2.toFile(v);
            } catch (IOException e) {
                System.out.println("Błąd w zapisie");
            }
        }

        Text stats = new Text("1. Statystyki do epoki: ");
        statsInput = new TextField(String.valueOf(toN));
        HBox statsH = new HBox(stats, statsInput);
        statsH.setSpacing(5);
        statsH.setAlignment(Pos.CENTER);

        Text stats2 = new Text("2. Statystyki do epoki: ");
        statsInput2 = new TextField(String.valueOf(toN));
        HBox statsH2 = new HBox(stats2, statsInput2);
        statsH2.setSpacing(5);
        statsH2.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(dayInfo, day, amountAnimalInfo, amountAnimal, amountGrassInfo, amountGrass, averageInfo, average, averageLiveInfo, averageLive, buttons, mostGenotypeInfo, mostGenotype1, mostGenotype2, averageChildrenInfo, averageChildren, buttons2, statsH, statsH2);
        vbox.setLayoutX(width);


        return vbox;
    }

}
