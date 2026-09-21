public class Bike {
    private boolean powerButton;
    private int currentSpeed;
    private int currentGear;

    public boolean powerOn() {
        this.powerButton = true;

        return this.powerButton;
    }

    public boolean powerOff() {
        this.powerButton = false;

        return this.powerButton;
    }

    public Bike() {

        this.currentSpeed = 0;
        this.currentGear  = 1;
    }

    public void setGear(int speed) {
        this.currentSpeed = speed;

        if (this.currentSpeed > 0 && this.currentSpeed < 20) {
            this.currentGear = 1;

        } else if (this.currentSpeed >= 21 && this.currentSpeed <= 30) {
            this.currentGear = 2;

        } else if (this.currentSpeed >= 31 && this.currentSpeed <= 40) {
            this.currentGear = 3;

        } else if (this.currentSpeed >= 41) {
            this.currentGear = 4;
        }


    }

    public int getGear() {
        return currentGear;
    }

    public void setSpeed(int currentSpeed) {
        this.currentSpeed = currentSpeed;
        if (this.currentSpeed > 0 && this.currentSpeed <= 20) {
            this.currentGear = 1;

        } else if (this.currentSpeed >= 21 && this.currentSpeed <= 30) {
            this.currentGear = 2;

        } else if (this.currentSpeed >= 31 && this.currentSpeed <= 40) {
            this.currentGear = 3;

        } else if (this.currentSpeed >= 41) {
            this.currentGear = 4;
        }


    }

    public int getSpeed() {
        return currentSpeed;
    }

    public void accelerate(int gear) {
        this.currentSpeed += gear;
    }

    public void decelerate(int gear) {
        this.currentSpeed -= gear;
    }


}
