
package composants

import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.lib.interfaces.DrawableObject
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import exp.PelletFactory

import scala.io.AnsiColor._


class Bot(val bouboules: PelletFactory,val ray: Float, val inPosition: Vector2, val angle: Float) extends PhysicsCircle("this.", inPosition, ray, angle) with DrawableObject {
  var stats: statSheet = new statSheet(1, 1, 1, 1, 1, 1)
  this.setCollisionGroup(-1)
  def getInstance(): Bot = this

  override def draw(g: GdxGraphics): Unit = {
    g.drawFilledCircle(this.getBodyPosition.x, this.getBodyPosition.y, 30, Color.BLUE)
  }

  def getNearestObject(array: com.badlogic.gdx.utils.Array[Body]): Vector2 ={
    //mise en place d'une detectBoxe
    var min: Double = getDistance(array.first().getPosition, this.getBodyPosition)
    var nearestObject = new Vector2(0,0)
    for(i <- array.toArray){
      val dist : Double = getDistance(i.getPosition, this.getBodyPosition)
      if(dist < min){
        nearestObject = i.getPosition
        min = dist
      }
    }
    return nearestObject
  }

  def getDistance(p1: Vector2, p2: Vector2): Double = {
    val dx = p2.x - p1.x
    val dy = p2.y - p1.y
    return math.sqrt(dx * dx + dy * dy)
  }

  def getDirection(but: Vector2, départ: Vector2): Double = {
    val dx = but.x - départ.x
    val dy = but.y - départ.y
    val radians = math.atan2(dy, dx)
    val degrees = math.toDegrees(radians)
    return degrees
  }

  def move(objectToMove: Bot, destination: Vector2, angle: Double): Unit= {
    val horses = 0.25f
    val rupteur = 2
    println(s"${RED}j'ai une cible")
    println(s"$RESET destination : $destination")

    if (this.getBodyLinearVelocity.len() > rupteur * stats.movementSpeed) {
      this.applyBodyForce(this.getBodyLinearVelocity().scl(-50), this.getBodyWorldCenter, true)
      println(s"$GREEN je freine")
    }else{
      this.applyBodyForce(destination.scl(horses), this.getBodyWorldCenter,true)
    }
  }

  def getPos: Vector2 = this.getBodyPosition
}


