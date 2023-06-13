package composants

import ch.hevs.gdx2d.components.physics.primitives.{PhysicsCircle, PhysicsStaticBox}
import ch.hevs.gdx2d.lib.physics.AbstractPhysicsObject
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.{BodyDef, MassData}

import scala.collection.mutable.ArrayBuffer

class Bullet(tab: ArrayBuffer[Bullet],joueur: Joueur, pvIn: Int, speedIn: Int, angleIn: Float, position: Vector2) extends PhysicsCircle("Boulette", position, 10, 1, 1, 0) {
    val trueAngle: Float = (angleIn * math.Pi / 180).toFloat

    if(angleIn > 180) {
        setBodyLinearVelocity(speedIn * math.sin(trueAngle).toFloat, speedIn * math.cos(trueAngle).toFloat)
    }else{
        setBodyLinearVelocity(speedIn * math.sin(trueAngle).toFloat, -1 * speedIn * math.cos(trueAngle).toFloat)
    }
    enableCollisionListener()

    override def collision(theOtherObject: AbstractPhysicsObject, energy: Float): Unit = {
        println(s"collision avec ${theOtherObject.toString} de type ${theOtherObject.getClass} (${})")
        if(theOtherObject != joueur.playerBox){
            destroy()
            tab.subtractOne(this)
        }



        if(theOtherObject.getBody.getType != BodyDef.BodyType.StaticBody && theOtherObject != joueur.playerBox){
            val gravitere = theOtherObject.getBody.getGravityScale
            if(gravitere - pvIn < 0){
                theOtherObject.destroy()
            }else {
                theOtherObject.getBody.setGravityScale(gravitere - pvIn)
            }
        }
    }
}