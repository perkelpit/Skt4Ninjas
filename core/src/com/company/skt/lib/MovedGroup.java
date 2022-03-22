package com.company.skt.lib;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class MovedGroup extends BaseGroup {
    private Vector2 velocityVector;
    private Vector2 accelerationVector;
    private float acceleration;
    private float maxSpeed;
    private float deceleration;
    
    {
        velocityVector = new Vector2(0, 0);
        accelerationVector = new Vector2(0, 0);
        acceleration = 0;
        maxSpeed = 1000;
        deceleration = 0;
    }
    
    public MovedGroup(String name) {
        super(name);
    }
    
    public void setSpeed(float speed) {
        if(velocityVector.len() == 0) {
            velocityVector.set(speed, 0);
        } else {
            velocityVector.setLength(speed);
        }
    }
    
    public float getSpeed() {
        return velocityVector.len();
    }
    
    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
    
    public float getMaxSpeed() {
        return maxSpeed;
    }
    
    public void setMotionAngle(float angle) {
        velocityVector.setAngleDeg(angle);
    }
    
    public float getMotionAngle() {
        return velocityVector.angleDeg();
    }
    
    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }
    
    public float getAcceleration() {
        return acceleration;
    }
    
    public void setDeceleration(float deceleration) {
        this.deceleration = deceleration;
    }
    
    public void accelerateAtAngle(float angle) {
        accelerationVector.add(new Vector2(acceleration, 0).setAngleDeg(angle));
        setRotation(angle);
    }
    
    public void accelerateForward() {
        accelerateAtAngle(getRotation());
    }
    
    public boolean isMoving() {
        return getSpeed() > 0;
    }
    
    public Vector2 getVelocityVector() {
        return velocityVector;
    }
    
    public void setAccelerationVector(Vector2 accelerationVector) {
        this.accelerationVector = accelerationVector;
    }
    
    public void setVelocityVector(Vector2 velocityVector) {
        this.velocityVector = velocityVector;
    }
    
    public Vector2 getAccelerationVector() {
        return accelerationVector;
    }
    
    public void applyMovementPhysics(float delta) {
        // update speed
        velocityVector.add(accelerationVector.x * delta, accelerationVector.y * delta); // accelerate
        float speed = getSpeed();
        if(accelerationVector.len() == 0) {
            speed -= deceleration * delta; // decelerate if not accelerating
        }
        speed = MathUtils.clamp(speed, 0, maxSpeed); // keep speed within bounds
        setSpeed(speed);
        // move according to updated speed
        moveBy(velocityVector.x * delta, velocityVector.y * delta);
        // reset acceleration
        accelerationVector.set(0, 0);
    }
    
    public Vector2 redirectVector(Vector2 toRedirectVector, Vector2 obstacleVector){
        float deltaAngle = Math.abs(toRedirectVector.angleDeg() - (obstacleVector.angleDeg() + 90))
                           - Math.abs(toRedirectVector.angleDeg() - (obstacleVector.angleDeg() - 90));
        Vector2 redirectedVector = new Vector2(toRedirectVector.len(), 0);
        redirectedVector.setAngleDeg(toRedirectVector.angleDeg() - deltaAngle);
        return redirectedVector;
    }
    
    @Override
    public void act(float delta) {
        super.act(delta);
        if(!isStopped()) {
            applyMovementPhysics(delta);
        }
    }
    
}
