package com.example.platformer;

import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;

class PhysicsEngine {

    void update(long fps, ArrayList<GameObject> objects, GameState gs){
        for(GameObject o : objects){
            o.update(fps, objects.get(LevelManager.PLAYER_INDEX).getTransform());
        }

        detectCollisions(gs, objects);
    }

    private void detectCollisions(GameState gs, ArrayList<GameObject> objects){
        // More code here soon
        boolean collisionOccured = false;

        // Something collides with some part of
        // the player most frames
        // so, let's make some handy references
        // Get a reference to the players position
        // as we will probably need to update it
        Transform playerTransform = objects.get(LevelManager.PLAYER_INDEX).getTransform();

        PlayerTransform playersPlayerTransform =
                (PlayerTransform)playerTransform;

        // Get the players extra colliders
        // from the cast object
        ArrayList<RectF> playerColliders = playersPlayerTransform.getColliders();

        PointF playerLocation = playerTransform.getLocation();

        for(GameObject o : objects){
            // Just need to check player collisions
            // with everything else
            if(o.checkActive()){
                // Object is active so check for collision
                // with player - anywhere at all
                if(RectF.intersects(o.getTransform().getCollider(),
                        playerTransform.getCollider())){
                    // A collision of some kind has occurred
                    // so let's dig a little deeper
                    collisionOccured = true;

                    // Get a reference to the current
                    // (being tested) object's
                    // transform and location
                    Transform testedTransform = o.getTransform();
                    PointF testedLocation = testedTransform.getLocation();

                    // Don't check the player against itself
                    if(objects.indexOf(o) != LevelManager.PLAYER_INDEX){
                        // Where was the player hit?
                        for(int i=0; i<playerColliders.size(); i++){

                            if(RectF.intersects(testedTransform.getCollider(),
                                    playerColliders.get(i))){
                                // React to the collision based on
                                // body part and object type

                                switch (o.getTag() + " with " + i){
                                    // Test feet first to avoid the
                                    // player sinking in to a tile
                                    // and unnecessarily triggering
                                    // right and left as well
                                    case "Movable Platform with 0":
                                        playersPlayerTransform.grounded();
                                        playerLocation.y = (testedTransform.getLocation().y) -
                                                        playerTransform.getSize().y;
                                        break;

                                    case "Death with 0":// Feet
                                        gs.death();
                                        break;

                                    case "Inert Tile with 0":// Feet
                                        playersPlayerTransform.grounded();
                                        playerLocation.y = testedTransform.getLocation().y -
                                                playerTransform.getSize().y;
                                        break;

                                    case "Inert Tile with 1":// Head
                                        // Just update the player to a suitable height
                                        // but allow any jumps to continue
                                        playerLocation.y = testedLocation.y +
                                                testedTransform.getSize().y;
                                        playersPlayerTransform.triggerBumpedHead();
                                        break;

                                    case "Collectible with 2":// Right
                                        SoundEngine.playCoinPickup();
                                        // Switch off coin
                                        o.setInactive();
                                        // Tell the game state a coin has been found
                                        gs.coinCollected();
                                        break;

                                    case "Inert Tile with 2":// Right
                                        // Stop the player moving right
                                        playerTransform.stopMovingRight();
                                        // Move the player to the left of the tile
                                        playerLocation.x = testedTransform.getLocation().x -
                                                playerTransform.getSize().x;
                                        break;

                                    case "Collectible with 3":// Left
                                        SoundEngine.playCoinPickup();
                                        // Switch off coin
                                        o.setInactive();
                                        // Tell the game state a coin has been found
                                        gs.coinCollected();
                                        break;

                                    case "Inert Tile with 3":// Left
                                        playerTransform.stopMovingLeft();
                                        // Move the player to the right of the tile
                                        playerLocation.x = testedLocation.x +
                                                testedTransform.getSize().x;
                                        break;

                                    // Handle the player's feet reaching
                                    // the objective tile
                                    case "Objective Tile with 0":
                                        SoundEngine.playReachObjective();
                                        gs.objectiveReached();
                                        break;

                                    default:
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }

        if(!collisionOccured){
            // No connection with main player
            // collider so not grounded
            playersPlayerTransform.notGrounded();
        }
    }
}
