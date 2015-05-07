package ch.hslu.mobpro.myfinances;

public class CategoryDto {
    private float id;
    private String name;
    private boolean gain;

    public float getId() {
        return id;
    }

    public void setId(float id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGain() {
        return gain;
    }

    public void setGain(boolean gain) {
        this.gain = gain;
    }

    @Override
    public String toString() {
        String gainName = gain ? "In" : "Out";
        return String.format("%s %s", this.name, gainName);
    }
}
