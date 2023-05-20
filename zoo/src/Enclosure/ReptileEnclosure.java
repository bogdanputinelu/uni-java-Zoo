package Enclosure;

public class ReptileEnclosure extends Enclosure{
    private int temperature;

    public ReptileEnclosure(int surface, int capacity, String name, int temperature) {
        super(surface, capacity, name);
        this.temperature = temperature;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
    @Override
    public String toString() {
        return "Reptile enclosure: " + this.name + " with id: " + this.enclosureId;
    }
}
