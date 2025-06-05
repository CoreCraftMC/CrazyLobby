package me.corecraft.paper.configs.objects;

public class SoundProperty {

    public String value;

    public double volume;

    public float pitch;

    public final SoundProperty populate() {
        this.value = "entity.villager.no";
        this.volume = 1.0;
        this.pitch = 1.0f;

        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public String getValue() {
        return this.value;
    }

    public double getVolume() {
        return this.volume;
    }

    public float getPitch() {
        return this.pitch;
    }
}