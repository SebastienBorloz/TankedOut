package composants

import ch.hevs.gdx2d.components.physics.primitives.{PhysicsCircle, PhysicsStaticBox}
import ch.hevs.gdx2d.lib.physics.AbstractPhysicsObject
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.{BodyDef, MassData}
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
        if(theOtherObject != joueur){
            destroy()
            tab.subtractOne(this)
        }

        if(theOtherObject.getBody.getType != BodyDef.BodyType.StaticBody && theOtherObject != joueur){
            val gravitere = theOtherObject.getBody.getGravityScale
            if(gravitere - damage < 0){
                theOtherObject.getBody.getMass.toInt match{
                    case 1 =>
                        joueur.exp += 10
                        var savedI: trianglePellet = null
                        for(i <- joueur.bouboules.triangleStash) {
                            if (i.triangleBox.getBody == theOtherObject.getBody) {
                                savedI = i
                            }
                        }
                        theOtherObject.destroy()
                        joueur.bouboules.triangleStash.subtractOne(savedI)

                    case 13 =>
                        var savedI: squarePellet = null
                        joueur.exp += 25
                        for (i <- joueur.bouboules.squareStash) {
                            if (i.squareBox.getBody == theOtherObject.getBody) {
                                savedI = i
                            }
                        }
                        theOtherObject.destroy()
                        joueur.bouboules.squareStash.subtractOne(savedI)

                    case 44 =>
                        var savedI: pentagonPellet = null
                        joueur.exp += 40
                        for (i <- joueur.bouboules.pentagonStash) {
                            if (i.pentagonBox.getBody == theOtherObject.getBody) {
                                savedI = i
                            }
                        }
                        theOtherObject.destroy()
                        joueur.bouboules.pentagonStash.subtractOne(savedI)

                    case 364 =>
                        var savedI: bigPentaPellet = null
                        joueur.exp += 150
                        for (i <- joueur.bouboules.bigPentaStash) {
                            if (i.bigPentaBox.getBody == theOtherObject.getBody) {
                                savedI = i
                            }
                        }
                        theOtherObject.destroy()
                        joueur.bouboules.bigPentaStash.subtractOne(savedI)
                    case _ =>

                }
            }else {
                theOtherObject.getBody.setGravityScale(gravitere - damage)
            }
        }
    }
}