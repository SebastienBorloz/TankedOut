package composants

import ch.hevs.gdx2d.components.physics.primitives.PhysicsBox
import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import ch.hevs.gdx2d.lib.interfaces.DrawableObject

import java.util


class Joueur(val ray: Float, val inPosition: Vector2, val angle: Float) extends DrawableObject {
    val playerBox = new PhysicsCircle("playerCenter", inPosition, ray, angle)
    playerBox.setCollisionGroup(-1)
    // Initialize canon
    //this.canon = new Canon(this, );
    private val stats: statSheet = new statSheet(1,1,1,1,1,1,1,1)
    protected var canon: Canon = null
    var moveRight = false
    var moveLeft = false
    var moveUp = false
    var moveDown = false

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

    protected def getLocalVelocity: Vector2 = {
        playerBox.getBody.getLocalVector(playerBox.getBody.getLinearVelocityFromLocalPoint(new Vector2(0, 0)))
    }

    def update(deltaTime: Float): Unit = { // update revolving wheels
        var baseVector = Vector2.Zero
        // if accelerator is pressed down and speed limit has not been reached,
        // go forwards
        if(moveUp){baseVector.x += 1}
        if(moveDown){baseVector.x -= 1}
        if(moveLeft){baseVector.x -= 1}
        if(moveRight){baseVector.x += 1}
        if (this.getSpeedKMH < this.stats.movementSpeed * 15) {
            baseVector = new Vector2(0, -1)
        }
        if(!moveUp && !moveDown && !moveLeft && !moveRight) { // slow down if not accelerating
          baseVector = new Vector2(0, 0)
          // Stop the car when it is going slow
          if (this.getSpeedKMH < 4) this.setSpeed(0)
          else if (this.getLocalVelocity.x < 0) baseVector = new Vector2(0, 0.5f)
          else if (this.getLocalVelocity.y > 0) baseVector = new Vector2(0, -0.5f)
        }

        // multiply by engine power, which gives us a force vector relative to
        // the wheel
        val forceVector = baseVector.scl(10)

        val position = playerBox.getBodyWorldCenter
        playerBox.applyBodyForce(playerBox.getBodyWorldVector(new Vector2(forceVector.x, forceVector.y)), position, true)
    }
}
