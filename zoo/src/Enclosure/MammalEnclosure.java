package Enclosure;

public class MammalEnclosure extends Enclosure{
    private int shadedSurface;

    public MammalEnclosure(int surface, int capacity, String name, int shadedSurface) {
        super(surface, capacity, name);
        this.shadedSurface = shadedSurface;
    }

    public int getShadedSurface() {
        return shadedSurface;
    }

    public void setShadedSurface(int shadedSurface) {
        this.shadedSurface = shadedSurface;
    }

    @Override
    public String toString() {
        return "Mammal enclosure: " + this.name + " with id: " + this.enclosureId;
    }
}
