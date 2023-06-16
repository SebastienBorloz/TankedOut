
package composants

import ch.hevs.gdx2d.components.physics.primitives.{PhysicsCircle, PhysicsStaticBox}
import ch.hevs.gdx2d.components.physics.utils.PhysicsConstants
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.lib.interfaces.DrawableObject
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import exp.{PelletFactory, bigPentaPellet, pentagonPellet, squarePellet, trianglePellet}
import setup.settings

import scala.io.AnsiColor._

class Bot(val bouboules: PelletFactory,val ray: Float, val position: Vector2, val angle: Float)
  extends PhysicsCircle("this.", position, ray, angle) with DrawableObject {
  var stats: statSheet = new statSheet(1, 1, 1, 1, 1, 1)
  this.setCollisionGroup(-1)
  def getInstance(): Bot = this
  def getPos: Vector2 = this.getBodyPosition

  var v : Vector2 = Vector2.Zero
  override def draw(g: GdxGraphics): Unit = {
    g.drawFilledCircle(this.getBodyPosition.x, this.getBodyPosition.y, 30, Color.BLUE)
    g.drawLine(this.getBodyPosition.x, this.getBodyPosition.y, getBodyPosition.x + PhysicsConstants.M2P*v.x, getBodyPosition.y + PhysicsConstants.M2P*v.y)
  }

  def getNearestObject(array: com.badlogic.gdx.utils.Array[Body]): Vector2 ={ // fonction qui return la posistion de l'objet le plus proche du bot
    var objectVector: Vector2 = Vector2.Zero
    var min: Double = getDistance(array.first().getPosition, this.getBodyPosition)
    var nearestObject = Vector2.Zero

    for(i <- array.toArray){
      if(i.getUserData.isInstanceOf[trianglePellet]){
        objectVector = i.getUserData.asInstanceOf[trianglePellet].position
      }
      if (i.getUserData.isInstanceOf[squarePellet]) {
        objectVector = i.getUserData.asInstanceOf[squarePellet].position
      }
      if (i.getUserData.isInstanceOf[pentagonPellet]) {
        objectVector = i.getUserData.asInstanceOf[pentagonPellet].position
      }
      if (i.getUserData.isInstanceOf[bigPentaPellet]) {
        objectVector = i.getUserData.asInstanceOf[bigPentaPellet].position
      }
      if (i.getUserData.isInstanceOf[Joueur]) {
        objectVector = i.getUserData.asInstanceOf[Joueur].inPosition
      }
      if(i.getUserData.isInstanceOf[PhysicsStaticBox]){
        objectVector = new Vector2(settings.CENTER_X, settings.CENTER_Y)
      }
      val dist : Double = getDistance(objectVector, this.getBodyPosition)
      if(dist < min){
        nearestObject = i.getPosition
        min = dist
      }
    }
    if(nearestObject == Vector2.Zero){
      nearestObject = new Vector2(settings.CENTER_X, settings.CENTER_Y)
    }
    return nearestObject
  }

  def getDistance(p1: Vector2, p2: Vector2): Double = { // donne la distance entre deux points
    val dx = p2.x - p1.x
    val dy = p2.y - p1.y
    return math.sqrt(dx * dx + dy * dy)
  }

  def move(destination: Vector2): Unit= { // gestion des mouvements du bots
    destination.x = 500 * PhysicsConstants.P2M
    destination.y = 400 * PhysicsConstants.P2M
    println(s"${RED}j'ai une cible$RESET à cette destination : $destination")
    //if (this.getBodyLinearVelocity.len() > settings.VITESSEMAX/2 * stats.movementSpeed) {
      //this.applyBodyForce(this.getBodyLinearVelocity().scl(-20), this.getBodyWorldCenter, true)
      //println(s"$GREEN je freine")
    //}else{
      //v = new Vector2(this.getBodyPosition.x - destination.x, this.getBodyPosition.y - destination.y).scl(settings.BOTACCELERATION).scl(1/100f)
      v = destination.scl(-1).add(getBodyPosition).scl(settings.BOTACCELERATION).scl(1 / 100f)

    this.applyBodyForce(v, this.getBodyPosition,true)
      println(v)
    //}
  }

}


