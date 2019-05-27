package com.example.paddleball;
import android.graphics.RectF;

import java.util.Random;

public class Ball {


    // Create all variables required by game
        private RectF gameRect;
        private float xVel;
        private float yVel;
        private float ballWidth;
        private float ballHeight;

        public Ball(int sizeX, int sizeY){

            // Create ball and its size
            ballWidth = sizeX / 90;
            ballHeight = ballWidth;

           // Get the ball moving using velocity
            yVel = sizeY / 2;
            xVel = yVel;

            //Configure rectangle to ball and access it
            gameRect = new RectF();

        }

        public RectF getRect(){
            return gameRect;
        }

        /// Set up the frame and positions
        public void update(long fps){
            gameRect.left = gameRect.left + (xVel / fps);
            gameRect.top = gameRect.top + (yVel / fps);
            gameRect.right = gameRect.left + ballWidth;
            gameRect.bottom = gameRect.top - ballHeight;
        }

        // Reverse vertical and horizontal axis
        public void yReverseVel(){
            yVel = -yVel;
        }
        public void xReverseVel(){
            xVel = -xVel;
        }

        //Set velocity of the ball
        public void setRandomXVel(){
            Random generator = new Random();
            int answer = generator.nextInt(2);

            if(answer == 0){
                xReverseVel();
            }
        }

        // Increase the velocity
        public void increaseVelocity(){
            xVel = xVel + xVel / 10;
            yVel = yVel + yVel / 10;
        }

        // Clear intrusions from Y axis
        public void clearY(float y){
            gameRect.bottom = y;
            gameRect.top = y - ballHeight;
        }

         //Clear intrusions from X axis

        public void clearX(float x){
            gameRect.left = x;
            gameRect.right = x + ballWidth;
        }
         // Reset the frame again
        public void reset(int x, int y){
            gameRect.left = x / 2;
            gameRect.top = y - 20;
            gameRect.right = x / 2 + ballWidth;
            gameRect.bottom = y - 20 - ballHeight;
        }



    }