package com.example.platformer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.example.platformer.GOSpec.GameObjectSpec;

interface UpdateComponent {

    void update(long fps, Transform t, Transform playerTransform);
}
