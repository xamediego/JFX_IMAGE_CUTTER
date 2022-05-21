package com;

public class Vector2D {

    float x;
    float y;

    public Vector2D() {
        x = 0;
        y = 0;
    }

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void add(Vector2D vector){
        x += vector.x;
        y += vector.y;
    }

}
