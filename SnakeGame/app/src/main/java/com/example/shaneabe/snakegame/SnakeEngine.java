package com.example.shaneabe.snakegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

/**
 * Snake Engine is a game engine that operates the classic Snake game. It can be hosted within a Fragment,
 * and possibly an activity. It is entirely standalone and only requires basic wiring to host within a fragment or activity.
 * Simply inflate the view, either from XML or code, and wire up the proper methods to start the Snake Engine execution.
 *
 * Hosting within a Fragment:
 *  1. Call init() method within the fragments onActivityCreated(Bundle) method to initialize the Snake Engine.
 *  2. Call resume() method within fragments onResume() method to start execution of the Snake Engine.
 *  3. Call pause() method within fragments onPause() method to pause execution of the Snake Engine when the fragment leaves the foreground view.
 *  4. Call tearDown() method within fragments onDestroyView() method to properly tear down the Snake Engine.
 *
 *  Hosting within an Activity: Not tested yet.
 *
 * Optional: Pause and Resume buttons can also be used to allow the player to pause/resume execution of the game. Use the pause() and resume() methods
 * from the buttons from the hosting fragment.
 */
public class SnakeEngine extends SurfaceView implements Runnable
{
    private Thread thread = null;                    // Main game loop thread

    public enum Direction {UP, RIGHT, DOWN, LEFT}    // enum for tracking movement direction

    private Direction direction = Direction.RIGHT;   // start off heading to the right

    private int screenX;                             // The screen dimensions, in pixels
    private int screenY;

    private int snakeLength;

    private int appleX;
    private int appleY;

    private int blockSize;                           // size in pixels of a snake segment

    private final int NUM_BLOCKS_WIDE = 40;          // size, in segments, of the playable area; i.e. the game is 40 blocks wide.
    private int numBlocksHigh;                       // height of the playable area, in block segments.

    private long nextFrameTime;                      // Control time between updates
    private final long FPS = 10;                     // update game 10 times per second (10 frames per second)
    private final long MILLIS_PER_SECOND = 3000;     // 1 second = 1000 milliseconds; decrease for faster game, increase for slower

    private int score;

    private int[] snakeXs;                           // location in the grid of all snake segments
    private int[] snakeYs;

    private volatile boolean isPlaying;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;             // Required for the canvas
    private Paint paint;
    private Random random;
    private boolean paused;
    private boolean gameThreadStarted;

    /**
     * Constructor for creating the Snake Engine through code.
     *
     * @param context The context of this application.
     */
    public SnakeEngine(Context context) {
        super(context, null);

        random = new Random();

        surfaceHolder = getHolder();    // Initialize drawing objects
        paint = new Paint();

        snakeXs = new int[100];         // snake limit is 100. Player wins if reaches 100 score
        snakeYs = new int[100];
    }

    /**
     * Constructor for creating the Snake Engine through an XML layout file.
     *
     * @param context The context of this application.
     * @param attrs The AttributeSet.
     */
    public SnakeEngine(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        random = new Random();

        surfaceHolder = getHolder();    // Initialize drawing objects
        paint = new Paint();

        snakeXs = new int[100];         // snake limit is 100. Player wins if reaches 100 score.
        snakeYs = new int[100];         // NOTE: Game will probably crash when 100 reached. TODO: fix this.

        gameThreadStarted = false;
        paused = true;
    }

    /**
     * The main game loop.
     */
    @Override
    public void run() {
        // Wait until the View is fully inflated. Calling newGame() before the View is ready will cause a divide by zero exception.
        while(getHeight() == 0 && getWidth() == 0)
        {
            try {
                thread.sleep(500);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        newGame();

        while (isPlaying)
        {
            // while the game is paused, halt execution.
            while(paused)
            {
                try
                {
                    thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            // update 10 times a second
            if (updateRequired())
            {
                update();
                draw();
            }
        }
    }

    /**
     * Pause execution of the Snake Engine.
     */
    public void pause() {
        paused = true;
    }

    /**
     * Tear down the Snake Engine.
     *
     * Stops execution and destroys the thread.
     */
    public void tearDown()
    {
        isPlaying = false;
        paused = false;
        try
        {
            thread.join();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Resume execution of the Snake Engine.
     */
    public void resume() {
        paused = false;

        if(!gameThreadStarted)
            gameThreadStarted = startGameThread();
    }

    /*
        Start the game thread. This method is only called once to start the game thread.
        Returns true after called to indicate that the thread was started.
     */
    private boolean startGameThread()
    {
        isPlaying = true;
        if(!thread.isAlive())
        {
            thread.start();
        }

        return true;
    }

    /**
     * Initialize the Snake Engine.
     *
     * This method creates a new Thread referencing this SnakeEngine instance,
     * and should only be called once when the game is first being created.
     * (e.g. in the onActivityCreated(Bundle) method within a hosting fragment)
     */
    public void init()
    {
        thread = new Thread(this);
    }

    /**
     * Start a new game.
     *
     * This method initializes or resets the snake back to its starting length to the center of the screen,
     * resets the score to 0, and respawns an apple.
     */
    public void newGame() {
        screenX = getWidth();   // Get the dimensions of this View
        screenY = getHeight();

        blockSize = screenX / NUM_BLOCKS_WIDE;
        numBlocksHigh = screenY / blockSize;

        snakeLength = 1;                   // start with one snake segment
        snakeXs[0] = NUM_BLOCKS_WIDE / 2;  // start in the middle of the screen
        snakeYs[0] = numBlocksHigh / 2;

        spawnApple();
        score = 0;
        nextFrameTime = System.currentTimeMillis();
    }

    /*
        Spawn an apple at a random location on the screen.
     */
    private void spawnApple() {

        appleX = random.nextInt(NUM_BLOCKS_WIDE - 1) + 1;
        appleY = random.nextInt(numBlocksHigh - 1) + 1;

        // check if the apple coords spawned inside the snake
        for(int i = 0; i < snakeLength; i++)
        {
            if(appleX == snakeXs[i] || appleY == snakeYs[i])
            {
                appleX = random.nextInt(NUM_BLOCKS_WIDE - 1) + 1;
                appleY = random.nextInt(numBlocksHigh - 1) + 1;
            }
        }
    }

    /*
        Called when the snake eats an apple.
     */
    private void eatApple() {
        snakeLength++;     // Increase length of snake
        spawnApple();      // spawn another apple
        score++;
    }

    /*
        Moves the snake by one block segment and updates its direction (if changed).
     */
    private void moveSnake() {
        for (int i = snakeLength; i > 0; i--) {
            snakeXs[i] = snakeXs[i - 1];   // move each segment to the position of the segment infront of it.
            snakeYs[i] = snakeYs[i - 1];   // excluding the head, because it has nothing infront of it.
        }

        switch (direction) {
            case UP:
                snakeYs[0]--;
                break;

            case RIGHT:
                snakeXs[0]++;
                break;

            case DOWN:
                snakeYs[0]++;
                break;

            case LEFT:
                snakeXs[0]--;
                break;
        }
    }

    /*
        Detect if the snake has hit a wall or eaten itself.
     */
    private boolean detectDeath() {
        boolean dead = false;

        // Has it hit the screen edge?
        if (snakeXs[0] == -1) dead = true;
        if (snakeXs[0] >= NUM_BLOCKS_WIDE) dead = true;
        if (snakeYs[0] == -1) dead = true;
        if (snakeYs[0] == numBlocksHigh) dead = true;

        // Has it eaten itself?
        for (int i = snakeLength - 1; i > 0; i--) {
            if ((i > 4) && (snakeXs[0] == snakeXs[i]) && (snakeYs[0] == snakeYs[i])) {
                dead = true;
            }
        }

        return dead;
    }

    /*
        Run an update of the game. Check if the snake has eaten an apple, where it has moved to,
        and if it has died.
     */
    private void update() {
        if (snakeXs[0] == appleX && snakeYs[0] == appleY)   // Did the snake eat an apple?
        {
            eatApple();
        }

        moveSnake();

        if (detectDeath())
        {
            newGame();
        }
    }

    /*
        Draw all game components (score, snake, apple) to the screen.
     */
    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();                                   // Lock the canvas for drawing
            canvas.drawColor(0xff000000);                                          // Color of the background

            paint.setColor(0xffffffff);                                            // Color of the score HUD
            paint.setTextSize(50);
            canvas.drawText("Score: " + score, screenX / 2, 70, paint);

            paint.setColor(0xff0ad30a);                                            // Color of the snake
            for (int i = 0; i < snakeLength; i++)                                  // Draw the snake one block at a time
            {
                canvas.drawRect(snakeXs[i] * blockSize,
                        (snakeYs[i] * blockSize),
                        (snakeXs[i] * blockSize) + blockSize,
                        (snakeYs[i] * blockSize) + blockSize,
                        paint);
            }

            paint.setColor(0xffd30a0a);                                            // Color of the apple
            canvas.drawRect(appleX * blockSize,                                // Draw an apple
                    (appleY * blockSize),
                    (appleX * blockSize) + blockSize,
                    (appleY * blockSize) + blockSize,
                    paint);

            surfaceHolder.unlockCanvasAndPost(canvas);                            // unlock the canvas post the results
        }
    }

    /*
        Check if an update is required (has enough time gone by that the screen needs to be redrawn?)
     */
    private boolean updateRequired() {
        if (nextFrameTime <= System.currentTimeMillis()) {
            nextFrameTime = System.currentTimeMillis() + MILLIS_PER_SECOND / FPS;  // Set when next update will be triggered.
            return true;                                                           // return true so update and draw functions are executed.
        }

        return false;
    }

    /**
     * Called when the user touches the screen and updates the direction of the snake.
     *
     * Controls are:
     *  Upper left corner - UP
     *  Lower left corner - DOWN
     *  Upper right corner - LEFT
     *  Lower right corner - RIGHT
     *
     * @param motionEvent The event.
     * @return True if the direction was updated, false otherwise.
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        // If the game is paused, disregard any touch events.
        if(paused)
            return false;


        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if (motionEvent.getX() <= screenX / 2)     // On the left side, so UP or DOWN
            {
                // if already going up or down, do nothing
                if (direction == Direction.UP || direction == Direction.DOWN)
                    return true;

                if (motionEvent.getY() <= screenY / 2) // Top left --> UP
                {
                    direction = Direction.UP;
                }
                else                                  // Bottom left --> DOWN
                {
                    direction = Direction.DOWN;
                }
            }
            else                                      // On the right side; LEFT or RIGHT
            {
                // if already going left or right, do nothing
                if(direction == Direction.LEFT || direction == Direction.RIGHT)
                    return true;

                if (motionEvent.getY() <= screenY / 2) // Top right --> LEFT
                {
                    direction = Direction.LEFT;
                }
                else                                  // Bottom right --> RIGHT
                {
                    direction = Direction.RIGHT;
                }
            }
        }
        return true;
    }
}


