package Enclosure;

import java.util.ArrayList;
import java.util.List;

public class Enclosure {
    private static int id = 0;
    protected int enclosureId;
    private int surface;
    protected String name;
    private int maxCapacity;
    private int currentCapacity;
    private List<Integer> animalIds;

    public Enclosure(int surface, int maxCapacity, String name) {
        this.surface = surface;
        this.maxCapacity = maxCapacity;
        this.name = name;
        this.animalIds = new ArrayList<>();
        this.currentCapacity = 0;
        id++;
        this.enclosureId = id;
    }

    public int getEnclosureId() {
        return enclosureId;
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }
    public int getSurface() {
        return surface;
    }

    public void setSurface(int surface) {
        this.surface = surface;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public List<Integer> getAnimalIds() {
        return animalIds;
    }
    public void addAnimal(Integer animalId){
        animalIds.add(animalId);
        this.currentCapacity++;
    }
    public void removeAnimal(Integer animalId){
        animalIds.remove(animalId);
        this.currentCapacity--;
    }


}
