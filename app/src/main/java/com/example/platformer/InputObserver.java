package com.example.platformer;

import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;

interface InputObserver {

    // This allows InputObservers to be called by GameEngine
    //to handle input
    void handleInput(MotionEvent event, GameState gs, ArrayList<Rect> buttons);
}
