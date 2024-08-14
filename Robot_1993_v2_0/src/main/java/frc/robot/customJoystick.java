package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class customJoystick extends Joystick {
    private double deadbandCartesian = 0;
    private double deadbandRotation = 0;
    private static double maxAxisVal = 128.0;

    public customJoystick(int port){
        super(port);
    }

    public void setDeadbandCartesian(double deadband) {
        if (deadband < 0.0 || deadband > 1.0) {
           System.out.println("THROWING EXCEPTION FOR CARTESIAN DEADBAND" + deadband);
            throw new IllegalArgumentException("Deadband must be between 0 and 1.");
        }
        this.deadbandCartesian = deadband;
        System.out.println("the first dead band == " + deadband);
    }
    
    public void setDeadbandRotation(double deadband) {
        if (deadband < 0.0 || deadband > 1.0) {
            System.out.println("THROWING EXCEPTION FOR ROTATION DEADBAND" + deadband);
            throw new IllegalArgumentException("Deadband must be between 0 and 1.");
        }
        this.deadbandRotation = deadband;
        System.out.println("the first dead band == " + deadband);
    }

    /**
     * normalizes an axis to [-1, 1]
     * @param val axis input value to normalize
     * @return normalized value
     */
    private double normalizeAxis(double val){
        System.out.println("normalize axis: " + val/maxAxisVal);
        return val / maxAxisVal;
    }

    /**
     * 
     * @param input value to apply deadband to
     * @param deadband the positive end of deadband
     * @return if abs(input) > deadband: input, if abs(input) < deadband: 0.0
     */
    private double applyDeadband(double input, double deadband){
        if (Math.abs(input) < deadband) {
            System.out.println("apply deadband" + deadband);
            System.out.println("input:  " + input);
            return 0.0;
        }
        else{
            return input;
        }
    }

    /**
     * 
     * @return normalized x value to [-1, 1]
     */
    public double getXNormalized(){
        //System.out.println(applyDeadband(normalizeAxis(getX()), this.deadbandCartesian));
        System.out.println("-------------");
        System.out.println("x deadband: " + this.deadbandCartesian);
        System.out.println("normalize Axis - Get X: " + getX());
        System.out.println("normalize Axis - Get X: " + normalizeAxis(getX()));
        System.out.println("-----------------");
        return applyDeadband(normalizeAxis(getX()), this.deadbandCartesian);
    }

    /**
     * 
     * @return normalized y value to [-1, 1]
     */
    public double getYNormalized(){
        System.out.println("-----------------");
        System.out.println("y deadband" + this.deadbandCartesian);
        System.out.println("normalize Axis - Get Y: " + getY());
        System.out.println("normalize Axis - Get Y: " + normalizeAxis(getY()));
        System.out.println("-----------------");
        return -1 * applyDeadband(normalizeAxis(getY()), this.deadbandCartesian);
    }

    /**
     * 
     * @return normalized rotation value to [-1, 1]
     */
    public double getRotNormalized(){
        System.out.println("-----------------");
        System.out.println("rotational deadband" + this.deadbandCartesian);
        System.out.println("normalize Axis - Get rotation: " + normalizeAxis(getZ()));
        System.out.println("normalize Axis - Get rotation: " + normalizeAxis(getY()));
        System.out.println("-----------------");
        return -1 * applyDeadband(normalizeAxis(getZ()), this.deadbandRotation);
    }

}
