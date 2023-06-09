package composants

import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle
import com.badlogic.gdx.math.Vector2

class Bullet(pvIn: Int, speedIn: Int, angleIn: Float, position: Vector2) {
    val pvs: Int = pvIn
    val trueAngle: Float = (angleIn * math.Pi / 180).toFloat

    val bulletBox = new PhysicsCircle("Boulette", position, 10, 1, 1, 0)
    if(angleIn > 180) {
        bulletBox.setBodyLinearVelocity(speedIn * math.sin(trueAngle).toFloat, speedIn * math.cos(trueAngle).toFloat)
    }else{
        bulletBox.setBodyLinearVelocity(speedIn * math.sin(trueAngle).toFloat, -1 * speedIn * math.cos(trueAngle).toFloat)
    }
}
