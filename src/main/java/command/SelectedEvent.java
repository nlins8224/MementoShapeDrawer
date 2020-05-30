package command;

public enum SelectedEvent {

    CIRCLE("circle"),
    SQUARE("square"),
    RECTANGLE("rectangle"),
    MOVE("move"),
    REMOVE("remove"),
    NONE("none");

    private final String description;

    SelectedEvent(String description){
        this.description = description;
    }

    public String toString(){
        return description;
    }

}
