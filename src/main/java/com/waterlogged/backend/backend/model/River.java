/**
 * Model a River object
 * @author Maxime Hutinet <maxime@hutinet.ch>
 * @author Justin Foltz <justin.foltz@gmail.com>
 */


package com.waterlogged.backend.backend.model;

public class River {
    private String id;
    private String name;
    private double level;
    private int difficulty;
    private Coordinate coordinate;

    public River() {}
        
    public River(String id, String name, double level, int difficulty, Coordinate coordinate) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.difficulty = difficulty;
        this.coordinate = coordinate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
    
}