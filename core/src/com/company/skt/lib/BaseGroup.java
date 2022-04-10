package com.company.skt.lib;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.company.skt.controller.Utils;

public class BaseGroup extends Group implements Initialize_Update, Named {
    
    private boolean solid;
    private boolean stopped;
    private boolean initialized;
    private boolean ignoreOverlap;
    private BoundaryActor boundaryActor;

    
    public BaseGroup(String name) {
        setName(name);
        boundaryActor = new BoundaryActor("boundary", this);
        addActor(boundaryActor);
    }
    
    public void initialize(){
        initialized = true;
    }
    
    public boolean isInitialized() {
        return initialized;
    }
    
    @Override
    public void update(float delta) {
        Initialize_Update.super.update(delta);
        // TODO Change to std-for-loop -> can cause GdxRuntimeException: #iterator() cannot be used nested
        for(Actor child : getAllChildren()) {
            ((Initialize_Update)child).update(delta);
        }
    }
    
    @Override
    protected void positionChanged() {
        boundaryActor.updateBoundaryPosition();
    }
    
    @Override
    protected void rotationChanged() {
        boundaryActor.updateBoundaryRotation();
    }
    
    @Override
    protected void scaleChanged() {
        boundaryActor.updateBoundaryScale();
    }
    
    public BaseGroup overlaps(ActorBundle<? extends BaseGroup> actorBundle) {
        // TODO Change to std-for-loop -> can cause GdxRuntimeException: #iterator() cannot be used nested
        for(BaseGroup baseGroup : actorBundle.getActors()) {
            if(!(this == baseGroup)) {
                if(overlaps(baseGroup)) {
                    return baseGroup;
                }
            }
        }
        return null;
    }
    
    public boolean overlaps(BaseGroup other) {
        if(!ignoreOverlap) {
            Polygon poly1 = this.getBoundary();
            Polygon poly2 = other.getBoundary();
            // initial test to improve performance
            if(!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle())) {
                return false;
            }
            // precise test
            if(this instanceof MovedGroup && isSolid()) {
                Intersector.MinimumTranslationVector mtv = new Intersector.MinimumTranslationVector();
                boolean overlaps = Intersector.overlapConvexPolygons(poly1, poly2, mtv);
                moveBy(mtv.normal.x * 1.5f * mtv.depth, mtv.normal.y * 1.5f * mtv.depth);
                return overlaps;
            }
            return Intersector.overlapConvexPolygons(poly1, poly2);
        } else {
            return false;
        }
    }
    
    public void setBoundary(float[] vertices, float refWidth, float refHeight) {
        boundaryActor.setCustomBoundary(vertices, refWidth, refHeight);
    }
    
    public Polygon getBoundary() {
        return boundaryActor.getBoundary();
    }
    
    public boolean isIgnoreOverlap() {
        return ignoreOverlap;
    }
    
    public void setIgnoreOverlap(boolean ignoreOverlap) {
        this.ignoreOverlap = ignoreOverlap;
    }
    
    public Vector2 getCenter() {
        Vector2 center = new Vector2();
        center.x = getX() + (getWidth()-1) / 2f;
        center.y = getY() + (getHeight()-1) / 2f;
        return center;
    }
    
    public void setSolid(boolean solid) {
        this.solid = solid;
    }
    
    public boolean isSolid() {
        return solid;
    }
    
    public boolean isStopped() {
        return stopped;
    }
    
    public void setStopped(boolean b) {
        stopped = b;
    }
    
    @Override
    public void setRotation(float degrees) {
        super.setRotation(Utils.limitDegrees(degrees));
    }
    
    @Override
    public void addActor(Actor actor) {
        super.addActor(actor);
        Vector2 size = new Vector2();
        // TODO Change to std-for-loop -> can cause GdxRuntimeException: #iterator() cannot be used nested
        for(Actor a : getAllChildren()) {
            size.x = Math.max(a.getWidth(), size.x);
            size.y = Math.max(a.getHeight(), size.y);
        }
        setSize(size.x, size.y);
        setOrigin(Align.center);
        // TODO Change to std-for-loop -> can cause GdxRuntimeException: #iterator() cannot be used nested
        for(Actor a : getAllChildren()) {
            if (a.getWidth() < getWidth()) {
                a.setX((getWidth() - a.getWidth()) / 2f);
            }
            if (a.getHeight() < getHeight()) {
                a.setY((getHeight() - a.getHeight()) / 2f);
            }
        }
    }
    
    private Array<Actor> createActorsList(Actor actor, Array<Actor> list){
        if(actor instanceof Group && ((Group)actor).getChildren() != null) {
            // TODO Change to std-for-loop -> can cause GdxRuntimeException: #iterator() cannot be used nested
            for (Actor child : ((Group)actor).getChildren()){
                list = createActorsList(child, list);
            }
        }
        list.add(actor);
        return list;
    }
    
    public Array<Actor> getAllActors() {
        return createActorsList(this, new Array<Actor>());
    }
    
    public Array<Actor> getAllChildren() {
        Array<Actor> list = createActorsList(this, new Array<Actor>());
        list.removeValue(this, true);
        return list;
    }
    
}
