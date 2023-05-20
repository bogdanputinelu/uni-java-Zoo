package Animal;

public class Animal {
    private static int id = 0;
    private int animalId;
    private String name;
    private Category category;
    private String species;
    private int age;
    private int enclosureId;

    public Animal(String name, Category category, String species, int age) {
        this.name = name;
        this.category = category;
        this.species = species;
        this.age = age;
        this.enclosureId = -1;
        id++;
        this.animalId = id;
    }

    public int getAnimalId() {
        return animalId;
    }

    public int getEnclosureId() {
        return enclosureId;
    }

    public void setEnclosureId(int enclosureId) {
        this.enclosureId = enclosureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return this.name + ", specia " + this.species + ", with id: " + this.animalId;
    }
}
