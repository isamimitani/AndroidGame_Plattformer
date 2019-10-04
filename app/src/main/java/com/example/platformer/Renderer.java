package com.example.platformer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

class Renderer {

    private Canvas mCanvas;
    private SurfaceHolder mSurefaceHolder;
    private Paint mPaint;

    // Here is our new camera
    private Camera mCamera;

    Renderer(SurfaceView sh, Point screenSize){
        mSurefaceHolder = sh.getHolder();
        mPaint = new Paint();

        // Initialize the camera
        mCamera = new Camera(screenSize.x, screenSize.y);
    }

    int getPixelsPerMetre(){
        return mCamera.getPixelsPerMetre();
    }

    void draw(ArrayList<GameObject> objects, GameState gs, HUD hud){
        if(mSurefaceHolder.getSurface().isValid()){
            mCanvas = mSurefaceHolder.lockCanvas();
            mCanvas.drawColor(Color.argb(255,0,0,0));

            if(gs.getDrawing()){
                // Set the player as the center of the camera
                mCamera.setWorldCentre(objects.get(LevelManager.PLAYER_INDEX)
                                        .getTransform().getLocation());

                for(GameObject o : objects){
                    if(o.checkActive()){
                        o.draw(mCanvas, mPaint, mCamera);
                    }
                }
            }

            hud.draw(mCanvas, mPaint, gs);

            mSurefaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }
}
