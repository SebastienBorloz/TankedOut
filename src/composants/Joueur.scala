package composants

import ch.hevs.gdx2d.components.physics.primitives.PhysicsBox
import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import ch.hevs.gdx2d.lib.interfaces.DrawableObject

import java.util


class Joueur(val ray: Float, val position: Vector2, val angle: Float) extends DrawableObject {
  val playerBox = new PhysicsCircle("playerCenter", position, ray, angle)
  playerBox.setCollisionGroup(-1)
  // Initialize canon
  //this.canon = new Canon(this, );
  private val stats = null
  private val position = null
  protected var playerBox: PhysicsCircle = null
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

  def getPos: Vector2 = position

  override def draw(g: GdxGraphics): Unit = {
    val pos = playerBox.getBodyPosition
    g.drawFilledCircle(pos.x, pos.y, 10, Color.BLUE)
  }

  def update(deltaTime: Float): Unit = { // update revolving wheels
    import scala.collection.JavaConversions._
    for (wheel <- this.getRevolvingWheels) {
      wheel.setAngle(this.wheelAngle)
    }
    // 3. APPLY FORCE TO WHEELS
    /**
     * Vector pointing in the force direction. Will be applied to
     * the wheel and is relative to the wheel
     */
    var baseVector = Vector2.Zero
    // if accelerator is pressed down and speed limit has not been reached,
    // go forwards
    if (accelerate && (this.getSpeedKMH < this.maxSpeed)) baseVector = new Vector2(0, -1)
    else if (brake) { // braking, but still moving forwards - increased force
      if (this.getLocalVelocity.y < 0) baseVector = new Vector2(0f, 1.3f)
      else { // going in reverse - less force
        // Limit maximal reverse speed
        if (getSpeedKMH < maxSpeed) baseVector = new Vector2(0f, 0.2f)
      }
    }
    else { // slow down if not accelerating
      baseVector = new Vector2(0, 0)
      // Stop the car when it is going slow
      if (this.getSpeedKMH < 4) this.setSpeed(0)
      else if (this.getLocalVelocity.y < 0) baseVector = new Vector2(0, slowingFactor)
      else if (this.getLocalVelocity.y > 0) baseVector = new Vector2(0, -slowingFactor)
    }
    // multiply by engine power, which gives us a force vector relative to
    // the wheel
    val forceVector = baseVector.scl(power)
    // Apply force to each wheel
    import scala.collection.JavaConversions._
    for (wheel <- this.getPoweredWheels) {
      val position = wheel.body.getWorldCenter
      wheel.body.applyForce(wheel.body.getWorldVector(new Vector2(forceVector.x, forceVector.y)), position, true)
    }
  }
}
