package org.mp.model;

import java.io.Serializable;

/**
 * Created by MP on 24/3/16.
 */
public class Region implements Serializable {
    private long id;
    private int coordinateX;
    private int coordinateY;
    private long timestamp;

    public Region(long id, int coordinateX, int coordinateY, long timestamp) {
        this.id = id;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(int coordinateX) {
        this.coordinateX = coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(int coordinateY) {
        this.coordinateY = coordinateY;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Region{" +
                "id=" + id +
                ", coordinateX=" + coordinateX +
                ", coordinateY=" + coordinateY +
                ", timestamp=" + timestamp +
                '}';
    }
}
