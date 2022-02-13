package frc.robot;

public class Ball {

    public enum LOCATION {
        INTAKE, TOWER, SHOOTER, FIELD
    }

    public enum COLOR {
        RED, BLUE, UNKNOWN
    }

    private LOCATION location;
    private COLOR color;

    public Ball() {
        this.location = LOCATION.FIELD;
        this.color = COLOR.UNKNOWN;
    }

    public Ball(LOCATION location, COLOR color) {
        this.location = location;
        this.color = color;
    }

    public LOCATION getLocation() {
        return location;
    }

    public COLOR getColor() {
        return color;
    }

    public void setLocation(LOCATION location) {
        this.location = location;
    }

    public void setColor(COLOR color) {
        this.color = color;
    }

    public void setLocationColor(LOCATION location, COLOR color) {
        setLocation(location);
        setColor(color);
    }

}