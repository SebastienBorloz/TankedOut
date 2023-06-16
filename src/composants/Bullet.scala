package composants

import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle
import ch.hevs.gdx2d.lib.physics.AbstractPhysicsObject
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import exp.{bigPentaPellet, pentagonPellet, squarePellet, trianglePellet}

import scala.collection.mutable.ArrayBuffer

class Bullet(tab: ArrayBuffer[Bullet], joueur: Joueur, damage: Int, speedIn: Int, angleIn: Float, position: Vector2, radIn : Int = 10) extends PhysicsCircle("Boulette", position, radIn, 1, 1, 0) {
    val trueAngle: Float = (angleIn * math.Pi / 180).toFloat

    if(angleIn > 180) {
        setBodyLinearVelocity(speedIn * math.sin(trueAngle).toFloat, speedIn * math.cos(trueAngle).toFloat)
    }else{
        setBodyLinearVelocity(speedIn * math.sin(trueAngle).toFloat, -1 * speedIn * math.cos(trueAngle).toFloat)
    }
    enableCollisionListener()

    override def collision(theOtherObject: AbstractPhysicsObject, energy: Float): Unit = {
        if(!theOtherObject.isInstanceOf[Joueur]){
            destroy()
            tab.subtractOne(this)
        }

        if(theOtherObject.isInstanceOf[bigPentaPellet] || theOtherObject.isInstanceOf[pentagonPellet] || theOtherObject.isInstanceOf[squarePellet]
           || theOtherObject.isInstanceOf[Bot] || theOtherObject.isInstanceOf[trianglePellet]) {
            if (theOtherObject.isInstanceOf[bigPentaPellet]) {
                if (theOtherObject.asInstanceOf[bigPentaPellet].valeur > 0) {
                    theOtherObject.asInstanceOf[bigPentaPellet].valeur - damage
                } else {
                    theOtherObject.destroy()
                    joueur.bouboules.bigPentaStash.subtractOne(theOtherObject.asInstanceOf[bigPentaPellet])
                }
            }
            if (theOtherObject.isInstanceOf[pentagonPellet]) {
                if (theOtherObject.asInstanceOf[pentagonPellet].valeur > 0) {
                    theOtherObject.asInstanceOf[pentagonPellet].valeur - damage
                } else {
                    theOtherObject.destroy()
                    joueur.bouboules.pentagonStash.subtractOne(theOtherObject.asInstanceOf[pentagonPellet])
                }
            }
            if (theOtherObject.isInstanceOf[squarePellet]) {
                if (theOtherObject.asInstanceOf[squarePellet].valeur > 0) {
                    theOtherObject.asInstanceOf[squarePellet].valeur - damage
                } else {
                    theOtherObject.destroy()
                    joueur.bouboules.squareStash.subtractOne(theOtherObject.asInstanceOf[squarePellet])
                }
            }
            if (theOtherObject.isInstanceOf[trianglePellet]) {
                if (theOtherObject.asInstanceOf[trianglePellet].valeur > 0) {
                    theOtherObject.asInstanceOf[trianglePellet].valeur - damage
                } else {
                    theOtherObject.destroy()
                    joueur.bouboules.triangleStash.subtractOne(theOtherObject.asInstanceOf[trianglePellet])
                }
            }
        }
    }
}