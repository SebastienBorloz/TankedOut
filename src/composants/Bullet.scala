package composants

import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle
import ch.hevs.gdx2d.lib.physics.AbstractPhysicsObject
import com.badlogic.gdx.math.Vector2

class Bullet(joueur: Joueur, pvIn: Int, speedIn: Int, angleIn: Float, position: Vector2) extends PhysicsCircle("Boulette", position, 10, 1, 1, 0) {
    val pvs: Int = pvIn
    val trueAngle: Float = (angleIn * math.Pi / 180).toFloat

    if(angleIn > 180) {
        setBodyLinearVelocity(speedIn * math.sin(trueAngle).toFloat, speedIn * math.cos(trueAngle).toFloat)
    }else{
        setBodyLinearVelocity(speedIn * math.sin(trueAngle).toFloat, -1 * speedIn * math.cos(trueAngle).toFloat)
    }
    enableCollisionListener()

    override def collision(theOtherObject: AbstractPhysicsObject, energy: Float): Unit = {
        println(s"collision avec ${theOtherObject.toString}")
        if(theOtherObject != joueur.playerBox){
            destroy()
        }
    }
}