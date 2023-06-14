package exp

import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.math.Vector2

import scala.collection.mutable.ArrayBuffer
import scala.util.Random.javaRandomToRandom
import scala.io.AnsiColor._
import setup.settings

class PelletFactory {
    var triangleStash: ArrayBuffer[trianglePellet] = new ArrayBuffer[trianglePellet]()
    var squareStash: ArrayBuffer[squarePellet] = new ArrayBuffer[squarePellet]()
    var pentagonStash: ArrayBuffer[pentagonPellet] = new ArrayBuffer[pentagonPellet]()
    var bigPentaStash: ArrayBuffer[bigPentaPellet] = new ArrayBuffer[bigPentaPellet]()

    var lastTriangle: Long = 0
    var lastSquare: Long = 0
    var lastPentagon: Long = 0
    var lastBigPenta: Long = 0

    def pelletUpdate(): Unit ={ // fonction de
        //pellet triangulaire
        if(triangleStash.length < settings.NBR_TRIANGLES) {
            if (System.currentTimeMillis() > lastTriangle + 1000) {
                lastTriangle = System.currentTimeMillis()
                spawnTriangle()
            }
        }

        if(squareStash.length < settings.NBR_SQUARES) {
            if (System.currentTimeMillis() > lastSquare + 5000) {
                lastSquare = System.currentTimeMillis()
                spawnSquare()
            }
        }

        if(pentagonStash.length < settings.NBR_PENTAGONS ) {
            if (System.currentTimeMillis() > lastPentagon + 10000) {
              lastPentagon = System.currentTimeMillis()
              spawnPentagon()
            }
        }

        if(bigPentaStash.length < settings.NBR_BIGPENTAS) {
            if (System.currentTimeMillis() > lastBigPenta + 30000) {
              lastBigPenta = System.currentTimeMillis()
              spawnBigPenta()
            }
        }
    }

    def pelletInit(): Unit ={
        for(i <- 0 until settings.NBR_TRIANGLES){spawnTriangle()}
        for(i <- 0 until settings.NBR_SQUARES){spawnSquare()}
        for(i <- 0 until settings.NBR_PENTAGONS){spawnPentagon()}
        for(i <- 0 until settings.NBR_BIGPENTAS){spawnBigPenta()}
    }

    /** fonctions de génération des coordonées du point d'apparition des pellets */
    def spawnCoor(s: String): (Int,Int) = {
        val angle: Double = random.between(0, 2*math.Pi)
        val smallRadius :Int = random.between(0,settings.INTERNAL_RADIUS)
        var x = 0
        var y = 0

        s match {
            case "H" =>
                x = (smallRadius*math.cos(angle)).toInt + settings.CENTER_X
                y = (smallRadius*math.sin(angle)).toInt + settings.CENTER_Y

            case "t" | "s" | "h" =>
                x = random.between(0, settings.BOX_WIDTH)
                y = random.between(0, settings.BOX_HEIGHT-1)

                // vérifier qui soient pas dans le cercle
                val distanceToCenter = (math.sqrt(math.pow(settings.CENTER_X - x, 2) + math.pow(settings.CENTER_Y - y, 2))).toInt
                val min: Int = settings.EXTERNAL_RADIUS - distanceToCenter
                if (distanceToCenter < settings.EXTERNAL_RADIUS) {// séparation en fonction de l'endroit du point selon les 4 quarts d'un cercle :
                    if(x > settings.CENTER_X-settings.EXTERNAL_RADIUS & x < settings.CENTER_X){
                        var toSubX = random.between(x - (settings.CENTER_X - settings.EXTERNAL_RADIUS), (x - (settings.CENTER_X - settings.EXTERNAL_RADIUS)) * 2)
                        x -= toSubX
                    }
                    else if(x < settings.CENTER_X+settings.EXTERNAL_RADIUS & x > settings.CENTER_X){
                         var toAddX = random.between((settings.CENTER_X+settings.EXTERNAL_RADIUS)-x, ((settings.CENTER_X+settings.EXTERNAL_RADIUS)-x) * 2 )
                        x += toAddX
                    }
                    if (y > settings.CENTER_Y - settings.EXTERNAL_RADIUS & y < settings.CENTER_Y) {
                       var toSubY = random.between(y - (settings.CENTER_Y - settings.EXTERNAL_RADIUS), (y -(settings.CENTER_Y - settings.EXTERNAL_RADIUS)) * 2)
                        y -= toSubY
                    }
                    else if (y < settings.CENTER_Y + settings.EXTERNAL_RADIUS & y > settings.CENTER_Y) {
                         var toAddY = random.between((settings.CENTER_Y+settings.EXTERNAL_RADIUS)-y, ((settings.CENTER_Y+settings.EXTERNAL_RADIUS)-y)*2)
                        y += toAddY
                    }
                }
        }
        var point = (x,y)
        return point
    }

    def spawnTriangle(): Unit ={
        val newPoint: (Int, Int) = spawnCoor("t")
        var spawnPoint: Vector2 = new Vector2(newPoint._1, newPoint._2)
        triangleStash.addOne(new trianglePellet(spawnPoint))
    }

    def spawnSquare(): Unit ={
        val newPoint: (Int, Int) = spawnCoor("s")
        var spawnPoint: Vector2 = new Vector2(newPoint._1, newPoint._2)
        squareStash.addOne(new squarePellet(spawnPoint))
    }

    def spawnPentagon(): Unit ={
        val newPoint: (Int, Int) = spawnCoor("h")
        var spawnPoint: Vector2 = new Vector2(newPoint._1, newPoint._2)
        pentagonStash.addOne(new pentagonPellet(spawnPoint))
    }

    def spawnBigPenta(): Unit = {
        val newPoint: (Int, Int) = spawnCoor("H")
        var spawnPoint: Vector2 = new Vector2(newPoint._1, newPoint._2)
        bigPentaStash.addOne(new bigPentaPellet(spawnPoint))
    }
}
