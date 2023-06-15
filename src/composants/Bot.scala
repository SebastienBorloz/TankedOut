
package composants

import scala.io.AnsiColor._
import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import ch.hevs.gdx2d.lib.interfaces.DrawableObject
import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.physics.box2d.Body
import exp.PelletFactory
import setup.settings

import scala.reflect.internal.util.Position
import scala.util.Random.javaRandomToRandom


class Bot(val bouboules: PelletFactory,val ray: Float, val inPosition: Vector2, val angle: Float) extends DrawableObject {
  val hitBoxSize: Int = 100
  val hitBoxNumber: Int = 100
  var stats: statSheet = new statSheet(1, 1, 1, 1, 1, 1)
  var botBox = new PhysicsCircle("botBox", inPosition, ray, angle)
  botBox.setCollisionGroup(-1)
  def getInstance(): Bot = this

  def getPos: Vector2 = botBox.getBodyPosition

  override def draw(g: GdxGraphics): Unit = {
    g.drawFilledCircle(botBox.getBodyPosition.x, botBox.getBodyPosition.y, 30, Color.BLUE)
  }

  def createCircle(centerX: Double, centerY: Double, radius: Double, numPoints: Int): List[(Double, Double)] = {
    val angleIncrement = 2 * math.Pi / numPoints
    (0 until numPoints).toList.map { i =>
      val angle = i * angleIncrement
      val x = centerX + radius * math.cos(angle)
      val y = centerY + radius * math.sin(angle)
      (x, y)
    }
  }
  def getNearestObject(polygone: Body): Vector2 ={
    //mise en place d'une detectBoxe
    val circlePoints = createCircle(botBox.getBodyPosition.x, botBox.getBodyPosition.y, hitBoxSize, hitBoxNumber)
    circlePoints.foreach { case (x, y) =>
      if(polygone.getPosition == new Vector2((x,y)._1.toInt,(x,y)._2.toInt)){
        println("...  j'ai trouvé qqch :" + polygone)
        return new Vector2(x.toInt,y.toInt)
      }

    }
    return new Vector2(0,0)
  }
  def getDirection(but: Vector2, départ: Vector2): Double = {
    val dx = but.x - départ.x
    val dy = but.y - départ.y
    val radians = math.atan2(dy, dx)
    val degrees = math.toDegrees(radians)
    return degrees
  }

  def move(objectToMove: Bot, position: Vector2, destination: Vector2, angle: Double): Unit= {
    val horses = 0.25f
    val rupteur = 2
    if (destination.x != 0 && destination.y != 0) {
      println(s"${RED}j'ai une cible")
      println(s"$RESET destination : $destination")
      val offsetX = math.cos(angle)
      val offsetY = math.sin(angle)
      val positionUpdate = new Vector2((objectToMove.getPos.x + offsetX).toInt, (objectToMove.getPos.y + offsetY).toInt)

      if (botBox.getBodyLinearVelocity.len() > rupteur * stats.movementSpeed) {
        botBox.applyBodyForce(botBox.getBodyLinearVelocity().scl(-10),botBox.getBodyWorldCenter, true)
      }else{
        botBox.applyBodyForce(positionUpdate.scl(horses), botBox.getBodyWorldCenter,true)
      }
    }else {
      val x = random.between(-settings.BOX_WIDTH, settings.BOX_WIDTH)
      val y = random.between(-settings.BOX_HEIGHT, settings.BOX_HEIGHT)
      val positionUpdate = new Vector2((position.x + x).toInt, (position.y + y).toInt)

      if(botBox.getBodyLinearVelocity.len() > stats.movementSpeed) {
        botBox.applyBodyForce(botBox.getBodyLinearVelocity().scl(-50),botBox.getBodyWorldCenter, true)
        println("je freine")
      }else {
        botBox.applyBodyForce(positionUpdate.scl(horses), botBox.getBodyWorldCenter, true)
        println(s"${BLUE}je bouge random : $positionUpdate")
      }

    }
  }
}


