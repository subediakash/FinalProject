package com.example.paddleball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.RectF;

// Use control thread to run the game
class BounceThread extends SurfaceView implements Runnable {

    Thread gameThread = null;

    //Create all objects and variables
    SurfaceHolder sHolder;
    volatile boolean gamePlaying;
    boolean mPaused = true;

    // Import canvas and paints, we need to draw later
    Canvas gameCanvas;
    Paint gamePaint;
    // Tracking FPS is also useful
    long gameFPS;

    //Declare sizes once again
    int sizeX;
    int sizeY;

    // The players paddle
    Paddle paddle;

    // The game's ball
    Ball ball;



    // The userScore and lives remaining to show on the screen
    int userScore = 0;
    int userLives = 3;

    // Start running the thread now
    public BounceThread(Context context, int x, int y) {
        super(context);

        // Set the screen width and height
        sizeX = x;
        sizeY = y;

        //Assemble everything ready for gameplay now including ball bat and screen
        sHolder = getHolder();
        gamePaint = new Paint();
        paddle = new Paddle(sizeX, sizeY);
        ball = new Ball(sizeX, sizeY);
        setupAndRestart();

    }

    public void setupAndRestart(){

        // Innitial position of ball
        ball.reset(sizeX, sizeY);

        // Information to track for the user
        if(userLives == 0) {
            userScore = 0;
            userLives = 3;
        }

    }

    @Override
    // This is why we implemented runnable
    public void run() {
        while (gamePlaying) {

            // Capture the current time and kepp updating the frame
            long startFrame = System.currentTimeMillis();
                if(!mPaused){
                    update();
            }

            // Draw the frame
            draw();

            // Record FPS
            long thisFrame = System.currentTimeMillis() - startFrame;
            if (thisFrame >= 1) {
                gameFPS = 1000 / thisFrame;
            }

        }

    }

     /* The gameplay has begun. check for new positions and if the user has succedded
        in blocking the ball with the paddle ,which cause collision */
    public void update() {
        paddle.update(gameFPS);
        ball.update(gameFPS);
        // If colision then throw back ball to random positions again
        if(RectF.intersects(paddle.getRect(), ball.getRect())) {
            ball.setRandomXVel();
            ball.yReverseVel();
            ball.clearY(paddle.getRect().top - 2);
         //Every time user blocks, score increases and increase ball speed as well
            userScore++;
            ball.increaseVelocity();

        }

      // If user fails bounce the ball back again randomly and decrease the life
        if(ball.getRect().bottom > sizeY){
            ball.yReverseVel();
            ball.clearY(sizeY - 2);
            userLives--;

         // game end and restart
            if(userLives == 0){
                mPaused = true;
                gameCanvas.drawText("Game Over", 30, 50, gamePaint);
                setupAndRestart();
            }
        }

        // Checking the conditions when the ball hits various places of our screen

         //Top
        if(ball.getRect().top < 0){
            ball.yReverseVel();
            ball.clearY(12);
        }

        // Left
        if(ball.getRect().left < 0){
            ball.xReverseVel();
            ball.clearX(2);

        }

         // Right
        if(ball.getRect().right > sizeX){
            ball.xReverseVel();
            ball.clearX(sizeX - 22);

        }

    }

     // Keep updating scene and draw
    public void draw() {

        // Fix the canvas positon
        if (sHolder.getSurface().isValid()) {
            gameCanvas= sHolder.lockCanvas();

            // Assign colors , update ball and paddle
            gameCanvas.drawColor(Color.argb(255, 26, 128, 182));
            gamePaint.setColor(Color.argb(255, 255, 255, 255));
            gameCanvas.drawRect(paddle.getRect(), gamePaint);
            gameCanvas.drawRect(ball.getRect(), gamePaint);

            gamePaint.setColor(Color.argb(255, 249, 129, 0));
            gamePaint.setColor(Color.argb(255, 255, 255, 255));

            // Show the Score
            gamePaint.setTextSize(40);
            gameCanvas.drawText("Score: " + userScore + "Lives: " + userLives, 10, 50, gamePaint);

            // Update everythign again
            sHolder.unlockCanvasAndPost(gameCanvas);
        }

    }

    // Implement thread for wakeup and resume actions
    public void pause() {
        gamePlaying = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

      // If user plays
    public void resume() {
        gamePlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Now finally implement the TouchScreenEvent
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // When player has touched the screen
            case MotionEvent.ACTION_DOWN:
                mPaused = false;

               /* Player can tap either side of the screen to move the paddle  and keep touching
               if he wants the paddle to move faster !
                */
                if(motionEvent.getX() > sizeX / 2){
                    paddle.setMovementState(paddle.rightSide);
                }
                else{
                    paddle.setMovementState(paddle.leftSide);
                }
                break;

            // As per user stops
            case MotionEvent.ACTION_UP:
                paddle.setMovementState(paddle.STOP);
                break;
        }
        return true;
    }

}

