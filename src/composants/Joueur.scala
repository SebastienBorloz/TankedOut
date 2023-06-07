package composants

import ch.hevs.gdx2d.components.physics.primitives.PhysicsBox
import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import ch.hevs.gdx2d.lib.interfaces.DrawableObject
import com.badlogic.gdx.{Gdx, Input}

import java.util


class Joueur(val ray: Float, val inPosition: Vector2, val angle: Float) extends DrawableObject {
    val playerBox = new PhysicsCircle("playerCenter", inPosition, ray, angle)
    playerBox.setCollisionGroup(-1)
    // Initialize canon
    //this.canon = new Canon(this, playerBox.getBodyPosition, 20, 50)
    private val stats: statSheet = new statSheet(1,1,1,1,1,1,1,1)
    protected var canon: Canon = null
    var moveRight = false
    var moveLeft = false
    var moveUp = false
    var moveDown = false
    var angleSouris: Float = 0


    def setSpeed(speed: Float): Unit = {
        /*   speed - speed in kilometers per hour   */
        var velocity = playerBox.getBodyLinearVelocity
        velocity = velocity.nor
        velocity = new Vector2(velocity.x * ((speed * 1000.0f) / 3600.0f), velocity.y * ((speed * 1000.0f) / 3600.0f))
        playerBox.setBodyLinearVelocity(velocity)
    }

    def getPos: Vector2 = playerBox.getBodyPosition

    override def draw(g: GdxGraphics): Unit = {
        val pos = playerBox.getBodyPosition
        g.drawFilledCircle(pos.x, pos.y, 10, Color.BLUE)
    }

    def getSpeedKMH: Float = {
        val velocity = playerBox.getBodyLinearVelocity
        val len = velocity.len
        (len / 1000) * 3600
    }

    def getLocalVelocity: Vector2 = {
        playerBox.getBody.getLocalVector(playerBox.getBody.getLinearVelocityFromLocalPoint(new Vector2(0, 0)))
    }

    def update(deltaTime: Float): Unit = { // update revolving wheels
        var baseVector = new Vector2(0,0)
        //playerBox.setBodyLinearVelocity(baseVector)
        // if accelerator is pressed down and speed limit has not been reached,
        // go forwards
        if(moveUp){baseVector.y += 1}
        if(moveDown){baseVector.y -= 1}
        if(moveLeft){baseVector.x -= 1}
        if(moveRight){baseVector.x += 1}

        if(!moveUp && !moveDown && !moveLeft && !moveRight) { // slow down if not accelerating
            baseVector = new Vector2(0,0)
            baseVector = playerBox.getBodyLinearVelocity().scl(-0.75f)
        }

        val forceVector = baseVector.scl(25)
        val position = playerBox.getBodyWorldCenter
        val vTest: Vector2 = playerBox.getBodyWorldVector(new Vector2(baseVector.x, baseVector.y))
        //Gdx.app.log("[Info debug]",s"UP: $moveUp, DOWN: $moveDown, LEFT: $moveLeft, RIGHT: $moveRight, Vecteur: ${vTest.toString}")
        playerBox.applyBodyForce(forceVector, position, true)

        val longActu: Float = playerBox.getBodyLinearVelocity.len()
        val vitesseLimite: Int = 15 * stats.movementSpeed
        if (longActu > vitesseLimite){
            playerBox.setBodyLinearVelocity(playerBox.getBodyLinearVelocity.scl(vitesseLimite / longActu))
        }
    }
}
