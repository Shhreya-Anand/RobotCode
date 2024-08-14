package frc.robot;

import com.ctre.phoenix6.hardware.TalonFX;

public class customMechDrive {
    private TalonFX frontLeft;
    private TalonFX frontRight;
    private TalonFX backLeft;
    private TalonFX backRight;

    // Constants
    private static final double PI_OVER_FOUR = Math.PI / 4.0;
    private static final double DEADZONE_XY = 0.25; // Adjust deadband for X and Y axes
    private static final double DEADZONE_ROTATION = 0.25; // Adjust deadband for rotation
    private static final double ALPHA = 0.1; // EMA smoothing factor
    private static final double MAX_CHANGE = 0.05; // Rate limiter max change per cycle

    // Previous values for smoothing
    private double previousXVal = 0.0;
    private double previousYVal = 0.0;
    private double previousRotation = 0.0;

    public customMechDrive(TalonFX frontLeft, TalonFX frontRight, TalonFX backLeft, TalonFX backRight) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
    }

    private double applyDeadband(double value, double deadband) {
        return (Math.abs(value) > deadband) ? value : 0.0;
    }

    private double applyExponentialMovingAverage(double newValue, double previousValue, double alpha) {
        return alpha * newValue + (1 - alpha) * previousValue;
    }

    private double rateLimiter(double value, double previousValue, double maxChange) {
        double delta = value - previousValue;
        if (Math.abs(delta) > maxChange) {
            value = previousValue + Math.signum(delta) * maxChange;
        }
        return value;
    }

    public void drive(double xVal, double yVal, double rotation) {
        // Apply deadband to joystick inputs
        xVal = applyDeadband(xVal, DEADZONE_XY);
        yVal = applyDeadband(yVal, DEADZONE_XY);
        rotation = applyDeadband(rotation, DEADZONE_ROTATION);

        // Apply exponential moving average for smoothing
        xVal = applyExponentialMovingAverage(xVal, previousXVal, ALPHA);
        yVal = applyExponentialMovingAverage(yVal, previousYVal, ALPHA);
        rotation = applyExponentialMovingAverage(rotation, previousRotation, ALPHA);

        // Apply rate limiter to avoid sudden jumps
        xVal = rateLimiter(xVal, previousXVal, MAX_CHANGE);
        yVal = rateLimiter(yVal, previousYVal, MAX_CHANGE);
        rotation = rateLimiter(rotation, previousRotation, MAX_CHANGE);

        // Update previous values for the next cycle
        previousXVal = xVal;
        previousYVal = yVal;
        previousRotation = rotation;

        // Calculate motor strengths
        double[] motorStrengths = calculateMotorStrengths(xVal, yVal, rotation);

        // Normalize and set motor outputs
        double[] normalizedMotorStrengths = normalizeMotorStrengths(motorStrengths);
        setMotorOutputs(normalizedMotorStrengths);
    }

    private double[] calculateMotorStrengths(double normalizedX, double normalizedY, double rotation) {
        double frontLeftStrength = normalizedY + normalizedX + rotation;
        double frontRightStrength = normalizedY - normalizedX - rotation;
        double backLeftStrength = normalizedY - normalizedX + rotation;
        double backRightStrength = normalizedY + normalizedX - rotation;
        return new double[] {frontLeftStrength, frontRightStrength, backLeftStrength, backRightStrength};
    }

    private double[] normalizeMotorStrengths(double[] motorStrengths) {
        double maxVal = findMaxAbsValue(motorStrengths);
        if (maxVal > 1.0) {
            for (int i = 0; i < motorStrengths.length; i++) {
                motorStrengths[i] /= maxVal;
            }
        }
        return motorStrengths;
    }

    private double findMaxAbsValue(double[] values) {
        double maxVal = Math.abs(values[0]);
        for (double value : values) {
            if (Math.abs(value) > maxVal) {
                maxVal = Math.abs(value);
            }
        }
        return maxVal;
    }

    private void setMotorOutputs(double[] motorStrengths) {
        frontLeft.set(motorStrengths[0]);
        frontRight.set(motorStrengths[1]);
        backLeft.set(motorStrengths[2]);
        backRight.set(motorStrengths[3]);
    }

    public void stopMotors() {
        frontLeft.stopMotor();
        frontRight.stopMotor();
        backLeft.stopMotor();
        backRight.stopMotor();
    }
}

