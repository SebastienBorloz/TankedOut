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
    val stats: statSheet = new statSheet()
    var Boulettes: ArrayBuffer[Bullet] = new ArrayBuffer[Bullet]()
    var moveRight = false
    var moveLeft = false
    var moveUp = false
    var moveDown = false
    var shooting = false
    var shootingTemp = 0L
    var mouseAngle: Float = 0
    var horses: Int = 25
    var exp: Int = 0
    var rupteur: Int = 2

    def getPos: Vector2 = playerBox.getBodyPosition

    override def draw(g: GdxGraphics): Unit = {
        val pos = playerBox.getBodyPosition
        g.drawFilledCircle(pos.x, pos.y, 30, Color.FIREBRICK)
    }


    /** Fonction de gestion des déplacements du joueur et des tirs*/
    def update(deltaTime: Float): Unit = {
        playerBox.isBodyFixedRotation

        // Création de la direction
        var baseVector = new Vector2(0, 0)
        val position = playerBox.getBodyWorldCenter

        /** déplacement vertical haut */
        if (moveUp) {
            baseVector.y += 1
        }
        /** déplacement vertical bas */
        if (moveDown) {
            baseVector.y -= 1
        }
        /** déplacement horizontal gauche */
        if (moveLeft) {
            baseVector.x -= 1
        }
        /** déplacement horizontal droite */
        if (moveRight) {
            baseVector.x += 1
        }
        playerBox.applyBodyForce(baseVector.scl(horses), playerBox.getBodyWorldCenter, true)

        // Le joueur ralentit si aucune touche directionnelle n'est pressée
        if (!moveUp && !moveDown && !moveLeft && !moveRight) {
            playerBox.applyBodyForce(playerBox.getBodyLinearVelocity().scl(-10), playerBox.getBodyWorldCenter, true)
        }



        //limitation de vitesse
        val longActu: Float = playerBox.getBodyLinearVelocity.len()
        val vitesseLimite: Int = rupteur * stats.movementSpeed
        if (longActu > vitesseLimite) {
            playerBox.setBodyLinearVelocity(playerBox.getBodyLinearVelocity.scl(vitesseLimite / longActu))
        }

        // Gestion des tirs
        if(shooting && shootingTemp < System.currentTimeMillis() - (20 + 500/stats.reload)){
            shootingTemp = System.currentTimeMillis()
            val spawnPos: Vector2 = new Vector2(playerBox.getBodyPosition)
            spawnPos.x += 40 * math.sin(mouseAngle * math.Pi / 180).toFloat
            if(mouseAngle < 180) {
                spawnPos.y -= 60 * math.cos(mouseAngle * math.Pi / 180).toFloat
            }else{
                spawnPos.y += 60 * math.cos(mouseAngle * math.Pi / 180).toFloat
            }
            Boulettes.addOne(new Bullet(this.Boulettes,this, 3 + stats.bulletDamage * 5, 5 + stats.bulletSpeed * 3, mouseAngle, spawnPos))
        }
    }
    /** Fonction qui aligne le joueur en direction du curseur de la souris */
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
        } //Réparation au cas ou la souris est en dessous de la physicBox
        if (v1.x > v2.x) {
            math.acos(dessus / dessous) * 180.0 / math.Pi
        } else {
            360 - math.acos(dessus / dessous) * 180.0 / math.Pi
        }
    }

    def getLevel(): Int = {
        for(i <- setup.settings.LEVEL_UPS.indices){
            if(exp < setup.settings.LEVEL_UPS(i)){
                return i + 1
            }
        }
        return setup.settings.NIV_MAX
    }
}