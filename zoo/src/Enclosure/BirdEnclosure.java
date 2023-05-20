package Enclosure;

public class BirdEnclosure extends Enclosure{
    private int nrOfNests;
    private int height;

    public BirdEnclosure(int surface, int capacity, String name, int nrOfNests, int height) {
        super(surface, capacity, name);
        this.nrOfNests = nrOfNests;
        this.height = height;
    }

    public int getNrOfNests() {
        return nrOfNests;
    }

    public void setNrOfNests(int nrOfNests) {
        this.nrOfNests = nrOfNests;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    @Override
    public String toString() {
        return "Bird enclosure: " + this.name + " with id: " + this.enclosureId;
    }
}
