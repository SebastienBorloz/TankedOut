package composants

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.{Vector2, Vector3}

import scala.collection.mutable.ArrayBuffer

class gestionDeClasses extends Enumeration{
    val Base, Sniper, MG, Doduble, Canon = Value
    var playerClass: gestionDeClasses#Value = Base
    val tonkBitmap = new BitmapImage("data/images/tonk.png")

    def Shooting(mouseAngle: Float, joueur: Joueur): Unit ={
        playerClass match{
            case Base =>
                val spawnPos: Vector2 = new Vector2(joueur.getBodyPosition)
                spawnPos.x += 60 * math.sin(mouseAngle * math.Pi / 180).toFloat
                if (mouseAngle < 180) {
                  spawnPos.y -= 60 * math.cos(mouseAngle * math.Pi / 180).toFloat
                } else {
                  spawnPos.y += 60 * math.cos(mouseAngle * math.Pi / 180).toFloat
                }
                joueur.Boulettes.addOne(new Bullet(joueur.Boulettes, joueur, 3 + joueur.stats.bulletDamage * 5, 5 + joueur.stats.bulletSpeed * 3, mouseAngle, spawnPos))

            case Sniper =>
                val spawnPos: Vector2 = new Vector2(joueur.getBodyPosition)
                spawnPos.x += 60 * math.sin(mouseAngle * math.Pi / 180).toFloat
                if (mouseAngle < 180) {
                    spawnPos.y -= 60 * math.cos(mouseAngle * math.Pi / 180).toFloat
                } else {
                    spawnPos.y += 60 * math.cos(mouseAngle * math.Pi / 180).toFloat
                }
                joueur.Boulettes.addOne(new Bullet(joueur.Boulettes, joueur, 23 + joueur.stats.bulletDamage * 5, 5 + joueur.stats.bulletSpeed * 3, mouseAngle, spawnPos))

            case MG =>
            case Doduble =>
                val spawnPos1: Vector2 = new Vector2(joueur.getBodyPosition)
                val spawnPos2: Vector2 = new Vector2(joueur.getBodyPosition)

                spawnPos1.x += 60 * math.sin((mouseAngle * math.Pi / 180) + 0.1).toFloat
                spawnPos1.x += 60 * math.sin((mouseAngle * math.Pi / 180) - 0.1).toFloat

                if (mouseAngle < 180) {spawnPos2.y -= 60 * math.cos((mouseAngle * math.Pi / 180) + 0.1).toFloat}
                else {spawnPos2.y += 60 * math.cos((mouseAngle * math.Pi / 180) + 0.1).toFloat}
                if (mouseAngle < 180) {spawnPos2.y -= 60 * math.cos((mouseAngle * math.Pi / 180) - 0.1).toFloat}
                else {spawnPos2.y += 60 * math.cos((mouseAngle * math.Pi / 180) - 0.1).toFloat}

                joueur.Boulettes.addOne(new Bullet(joueur.Boulettes, joueur, 3 + joueur.stats.bulletDamage * 5, 0, mouseAngle, spawnPos1))
                joueur.Boulettes.addOne(new Bullet(joueur.Boulettes, joueur, 3 + joueur.stats.bulletDamage * 5, 0, mouseAngle, spawnPos2))


            case Canon =>
                val spawnPos: Vector2 = new Vector2(joueur.getBodyPosition)
                spawnPos.x += 60 * math.sin(mouseAngle * math.Pi / 180).toFloat
                if (mouseAngle < 180) {
                    spawnPos.y -= 60 * math.cos(mouseAngle * math.Pi / 180).toFloat
                } else {
                    spawnPos.y += 60 * math.cos(mouseAngle * math.Pi / 180).toFloat
                }
                joueur.Boulettes.addOne(new Bullet(joueur.Boulettes, joueur, 10 + joueur.stats.bulletDamage * 5, 5 + joueur.stats.bulletSpeed * 2, mouseAngle, spawnPos, 18))
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
                val mouseX = Gdx.input.getX()
                val mouseY = Gdx.input.getY()
                val posMouse: Vector2 = new Vector2(mouseX.toFloat + 1, mouseY.toFloat + 1)
                val truePosMouse = g.getCamera.unproject(new Vector3(posMouse.x, posMouse.y, 0))
                val angle: Float = p1.getAngle(new Vector2(truePosMouse.x, truePosMouse.y), playerPosition).toFloat
                p1.mouseAngle = angle
                if (angle < 180) {
                    g.drawFilledRectangle(playerPosition.x + 55 * math.sin(angle * math.Pi / 180.0).toFloat, playerPosition.y - 55 * math.cos(angle * math.Pi / 180.0).toFloat, 21, 90, angle, Color.DARK_GRAY)
                } else {
                    g.drawFilledRectangle(playerPosition.x + 55 * math.sin(angle * math.Pi / 180.0).toFloat, playerPosition.y + 55 * math.cos(angle * math.Pi / 180.0).toFloat, 21, 90, 180 - angle, Color.DARK_GRAY)
                }

            case MG =>
                val mouseX = Gdx.input.getX()
                val mouseY = Gdx.input.getY()
                val posMouse: Vector2 = new Vector2(mouseX.toFloat + 1, mouseY.toFloat + 1)
                val truePosMouse = g.getCamera.unproject(new Vector3(posMouse.x, posMouse.y, 0))
                val angle: Float = p1.getAngle(new Vector2(truePosMouse.x, truePosMouse.y), playerPosition).toFloat
                p1.mouseAngle = angle

                if (angle < 180) {
                    g.drawPicture(playerPosition.x + 30 * math.sin(angle * math.Pi / 180.0).toFloat, playerPosition.y + 30 * math.cos(angle * math.Pi / 180.0).toFloat, tonkBitmap)
                } else {
                    g.drawPicture(playerPosition.x + 30 * math.sin(angle * math.Pi / 180.0).toFloat, playerPosition.y + 30 * math.cos(angle * math.Pi / 180.0).toFloat, tonkBitmap)
                }

            case Doduble =>
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

            case Canon =>
                val mouseX = Gdx.input.getX()
                val mouseY = Gdx.input.getY()
                val posMouse: Vector2 = new Vector2(mouseX.toFloat + 1, mouseY.toFloat + 1)
                val truePosMouse = g.getCamera.unproject(new Vector3(posMouse.x, posMouse.y, 0))
                val angle: Float = p1.getAngle(new Vector2(truePosMouse.x, truePosMouse.y), playerPosition).toFloat
                p1.mouseAngle = angle
                if (angle < 180) {
                    g.drawFilledRectangle(playerPosition.x + 40 * math.sin(angle * math.Pi / 180.0).toFloat, playerPosition.y - 40 * math.cos(angle * math.Pi / 180.0).toFloat, 35, 60, angle, Color.DARK_GRAY)
                } else {
                    g.drawFilledRectangle(playerPosition.x + 40 * math.sin(angle * math.Pi / 180.0).toFloat, playerPosition.y + 40 * math.cos(angle * math.Pi / 180.0).toFloat, 35, 60, 180 - angle, Color.DARK_GRAY)
                }
        }
    }
}
