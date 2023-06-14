package composants

import ch.hevs.gdx2d.components.physics.primitives.PhysicsBox
import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle
import ch.hevs.gdx2d.components.physics.utils.PhysicsConstants
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import ch.hevs.gdx2d.lib.interfaces.DrawableObject
import com.badlogic.gdx.{Gdx, Input}
import exp.PelletFactory

import java.awt.MouseInfo.getPointerInfo
import java.util
import scala.collection.mutable.ArrayBuffer


class Joueur(val bouboules: PelletFactory,val ray: Float, val inPosition: Vector2, val angle: Float) extends DrawableObject {
    val playerBox = new PhysicsCircle("playerCenter", inPosition, ray, angle)
    playerBox.setCollisionGroup(-1)
    // Initialize canon
    //this.canon = new Canon(this, playerBox.getBodyPosition, 50, 20)
    private val stats: statSheet = new statSheet(1, 1, 1, 1 , 8, 1)
    var Boulettes: ArrayBuffer[Bullet] = new ArrayBuffer[Bullet]()
    //protected var canon: Canon = null
    var moveRight = false
    var moveLeft = false
    var moveUp = false
    var moveDown = false
    var shooting = false
    var shootingTemp = 0L
    var mouseAngle: Float = 0
    var exp: Int = 0

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
        g.drawFilledCircle(pos.x, pos.y, 30, Color.FIREBRICK)
    }

    def getSpeedKMH: Float = {
        val velocity = playerBox.getBodyLinearVelocity
        val len = velocity.len
        (len / 1000) * 3600
    }

    def getLocalVelocity: Vector2 = {
        playerBox.getBody.getLocalVector(playerBox.getBody.getLinearVelocityFromLocalPoint(new Vector2(0, 0)))
    }

    def update(deltaTime: Float): Unit = { // update revolving wheels
        //creation de la direction
        var baseVector = new Vector2(0, 0)
        if (moveUp) {
            baseVector.y += 1
        }
        if (moveDown) {
            baseVector.y -= 1
        }
        if (moveLeft) {
            baseVector.x -= 1
        }
        if (moveRight) {
            baseVector.x += 1
        }

        //ralenti si aucun bouton est enclenché
        if (!moveUp && !moveDown && !moveLeft && !moveRight) {
            baseVector = new Vector2(0, 0)
            baseVector = playerBox.getBodyLinearVelocity().scl(-0.75f)
        }
        val forceVector = baseVector.scl(25) //multiplicateur vitesse

        //application de la force
        val position = playerBox.getBodyWorldCenter
        playerBox.applyBodyForce(forceVector, position, true)


        //limitation de vitesse
        val longActu: Float = playerBox.getBodyLinearVelocity.len()
        val vitesseLimite: Int = 15 * stats.movementSpeed
        if (longActu > vitesseLimite) {
            playerBox.setBodyLinearVelocity(playerBox.getBodyLinearVelocity.scl(vitesseLimite / longActu))
        }


        //gestion des tirs
        if(shooting == true && shootingTemp < System.currentTimeMillis() - 1000/stats.reload){
            shootingTemp = System.currentTimeMillis()
            val spawnPos: Vector2 = new Vector2(playerBox.getBodyPosition)
            spawnPos.x += 40 * math.sin(mouseAngle * math.Pi / 180).toFloat
            if(mouseAngle < 180) {
                spawnPos.y -= 50 * math.cos(mouseAngle * math.Pi / 180).toFloat
            }else{
                spawnPos.y += 50 * math.cos(mouseAngle * math.Pi / 180).toFloat
            }
            Boulettes.addOne(new Bullet(this.Boulettes,this,10, 10, mouseAngle, spawnPos))
        }
    }

    def getAngle(v1: Vector2, v2: Vector2): Double = {
        val vInt1: Vector2 = if (v1.x > v2.x) {
            new Vector2(v2.x - v1.x, v2.y - v1.y)
        } else {
            new Vector2(v1.x - v2.x, v1.y - v2.y)
        }
        val vInt2: Vector2 = new Vector2(0, v1.y)
        var dessus: Double = vInt1.y.toDouble * vInt2.y.toDouble
        val dessous: Double = math.sqrt(math.pow(vInt1.x, 2) + math.pow(vInt1.y, 2)) * math.sqrt(math.pow(vInt2.x, 2) + math.pow(vInt2.y, 2))
        if (v1.y < 0) {
            dessus = dessus * -1
        } //reparation au cas ou la souris est en dessous de la physicBox
        if (v1.x > v2.x) {
            math.acos(dessus / dessous) * 180.0 / math.Pi
        } else {
            360 - math.acos(dessus / dessous) * 180.0 / math.Pi
        }
    }
}