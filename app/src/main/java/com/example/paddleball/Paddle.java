package com.example.paddleball;

import android.graphics.RectF;

public class Paddle {

    // Create rectangle to fit sides
    private RectF gameRect;

    // Customize the Paddle
    private float gameLength;
    private float gameHeight;

    // X and Y co-ordinates of Paddle
    private float xAxis;
    private float yAxis;
    private float PaddleSpeed;

    // A user can move paddle left or right with one tap on either side of the screen
    public final int STOP = 0;
    public final int leftSide = 1;
    public final int rightSide = 2;

   // This is for initial stop condition
    private int PaddleMoving = STOP;

    // Customize the screen
    private int sizeX;
    private int sizeY;


    // Create main constructor method which we will call and use from the main activity
    public Paddle(int x, int y){


        // Once again , customize the screen
        sizeX = x;
        sizeY = y;
        gameLength = sizeX / 8;
        gameHeight = sizeY / 25;

       // Inistial position for paddle
        xAxis = sizeX / 2;
        yAxis = sizeY - 20;

        // Fix all the positions and speed
        gameRect = new RectF(xAxis, yAxis, xAxis + gameLength, yAxis + gameHeight);
        PaddleSpeed = sizeX;
    }

        // Method from BounceThread to make use of all available functions and objects
    public RectF getRect(){
        return gameRect;
    }

       // Set the paddle direction
    public void setMovementState(int state){
        PaddleMoving = state;
    }

       // Get Method from BounceThread which makes the Paddle move and change axis accordingslly
    public void update(long fps){
        if(PaddleMoving == leftSide){
            xAxis = xAxis - PaddleSpeed / fps;
        }

        if(PaddleMoving == rightSide){
            xAxis = xAxis + PaddleSpeed / fps;
        }

        // Prevent Paddle from disapparing outside of screen
        if(gameRect.left < 0){
            xAxis = 0;
        }

        if(gameRect.right > sizeX){
            xAxis = sizeX - (gameRect.right - gameRect.left);
        }

        // Keep updating the paddle position until required
        gameRect.left = xAxis;
        gameRect.right = xAxis + gameLength;
    }

}
