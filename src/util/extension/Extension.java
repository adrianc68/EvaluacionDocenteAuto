package util.extension;

import javafx.scene.control.Tab;

public class Extension {
    String name;
    boolean loaded;
    TypeExtension type;
    Tab tab;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public TypeExtension getType() {
        return type;
    }

    public void setType(TypeExtension type) {
        this.type = type;
    }

    public Tab getTab() {
        return tab;
    }

    public void setTab(Tab tab) {
        this.tab = tab;
    }

    @Override
    public String toString() {
        return "Extension{" +
                "name='" + name + '\'' +
                ", loaded=" + loaded +
                ", type=" + type +
                ", tab=" + tab +
                '}';
    }

}
