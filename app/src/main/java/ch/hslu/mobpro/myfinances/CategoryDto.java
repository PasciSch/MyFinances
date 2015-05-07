package ch.hslu.mobpro.myfinances;

public class CategoryDto {
    private long id;
    private String name;
    private boolean gain;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
