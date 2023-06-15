package composants

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.{Vector2, Vector3}

import scala.collection.mutable.ArrayBuffer

class gestionDeClasses extends Enumeration{
    val Base, Sniper, MG, Doduble, Canon = Value
    var playerClass: gestionDeClasses#Value = Base


    def Shooting(mouseAngle: Float, joueur: Joueur): Unit ={
        println(playerClass.toString)
        playerClass match{
            case Base =>
                val spawnPos: Vector2 = new Vector2(joueur.playerBox.getBodyPosition)
                spawnPos.x += 40 * math.sin(mouseAngle * math.Pi / 180).toFloat
                if (mouseAngle < 180) {
                  spawnPos.y -= 60 * math.cos(mouseAngle * math.Pi / 180).toFloat
                } else {
                  spawnPos.y += 60 * math.cos(mouseAngle * math.Pi / 180).toFloat
                }
                joueur.Boulettes.addOne(new Bullet(joueur.Boulettes, joueur, 3 + joueur.stats.bulletDamage * 5, 5 + joueur.stats.bulletSpeed * 3, mouseAngle, spawnPos))

            case Sniper =>
            case MG =>
            case Doduble =>
            case Canon =>
        }
    }


    def Render(p1: Joueur, playerPosition:Vector2, g: GdxGraphics): Unit ={
        playerClass match{
            case Base =>
                val mouseX = Gdx.input.getX()
                val mouseY = Gdx.input.getY()
                val posMouse: Vector2 = new Vector2(mouseX.toFloat + 1, mouseY.toFloat + 1)
                val truePosMouse = g.getCamera.unproject(new Vector3(posMouse.x, posMouse.y, 0))
                val angle: Float = p1.getAngle(new Vector2(truePosMouse.x, truePosMouse.y), playerPosition).toFloat
                p1.mouseAngle = angle
                if (angle < 180) {
                    g.drawFilledRectangle(playerPosition.x + 30 * math.sin(angle * math.Pi / 180.0).toFloat, playerPosition.y - 30 * math.cos(angle * math.Pi / 180.0).toFloat, 21, 33, angle, Color.DARK_GRAY)
                } else {
                    g.drawFilledRectangle(playerPosition.x + 30 * math.sin(angle * math.Pi / 180.0).toFloat, playerPosition.y + 30 * math.cos(angle * math.Pi / 180.0).toFloat, 21, 33, 180 - angle, Color.DARK_GRAY)
                }

            case Sniper =>
            case MG =>
            case Doduble =>
            case Canon =>
        }
    }
}
