package exp

import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.math.Vector2

import scala.collection.mutable.ArrayBuffer
import scala.util.Random.javaRandomToRandom
import scala.io.AnsiColor._

class PelletFactory {
    var triangleStash: ArrayBuffer[trianglePellet] = new ArrayBuffer[trianglePellet]()
    var squareStash: ArrayBuffer[squarePellet] = new ArrayBuffer[squarePellet]()
    var pentagonStash: ArrayBuffer[pentagonPellet] = new ArrayBuffer[pentagonPellet]()
    var bigPentaStash: ArrayBuffer[bigPentaPellet] = new ArrayBuffer[bigPentaPellet]()
    /** valeurs du nombres maximum de chaque pellets */
    val NBR_TRIANGLES = 200
    val NBR_SQUARES = 100
    val NBR_HEXAGONS = 50
    val NBR_BIGPENTAS = 6

    var lastTriangle: Long = 0
    var lastSquare: Long = 0
    var lastPentagon: Long = 0
    var lastBigPenta: Long = 0

    val INTERNAL_RADIUS: Int = 50
    var EXTERNAL_RADIUS: Int = 800
    val BOX_WIDTH: Int = 6000
    val BOX_HEIGHT: Int = 3000
    val CENTER_X: Int = BOX_WIDTH / 2
    val CENTER_Y: Int = BOX_HEIGHT / 2

    var gen: Int = 0

    def pelletUpdate(): Unit ={ // fonction de
        //pellet triangulaire
        if(triangleStash.length < NBR_TRIANGLES & gen >= NBR_TRIANGLES) {
            if (System.currentTimeMillis() > lastTriangle + 1000) {
                lastTriangle = System.currentTimeMillis()
                spawnTriangle()
            }
        }
        if (triangleStash.length < NBR_TRIANGLES & gen < NBR_TRIANGLES) {
            if (System.currentTimeMillis() > lastTriangle) {
                lastTriangle = System.currentTimeMillis()
                for(i <- 0 until NBR_TRIANGLES){
                    spawnTriangle()
                }
            }
        }

        if(squareStash.length < NBR_SQUARES & gen >= NBR_SQUARES) {
            if (System.currentTimeMillis() > lastSquare + 5000) {
                lastSquare = System.currentTimeMillis()
                spawnSquare()

            }
        }
        if (squareStash.length < NBR_SQUARES & gen < NBR_SQUARES) {
            if (System.currentTimeMillis() > lastSquare) {
                lastSquare = System.currentTimeMillis()
                for (i <- 0 until NBR_SQUARES) {
                    spawnSquare()
                }
            }
        }


        if(pentagonStash.length < NBR_HEXAGONS & gen >= NBR_HEXAGONS) {
            if (System.currentTimeMillis() > lastPentagon + 10000) {
              lastPentagon = System.currentTimeMillis()
              spawnPentagon()
            }
        }
        if (pentagonStash.length < NBR_HEXAGONS & gen < NBR_HEXAGONS) {
            if (System.currentTimeMillis() > lastPentagon) {
              lastPentagon = System.currentTimeMillis()
                for (i <- 0 until NBR_HEXAGONS) {
                  spawnPentagon()
                }
            }
        }

        if(bigPentaStash.length < NBR_BIGPENTAS & gen >= NBR_BIGPENTAS) {
            if (System.currentTimeMillis() > lastBigPenta + 30000) {
              lastBigPenta = System.currentTimeMillis()
              spawnBigPenta()
            }
        }
        else if(bigPentaStash.length < NBR_BIGPENTAS & gen < NBR_BIGPENTAS) {
            if (System.currentTimeMillis() > lastBigPenta) {
                lastBigPenta = System.currentTimeMillis()
                for (i <- 0 until NBR_BIGPENTAS) {
                  spawnBigPenta()
                }
            }
        }
        gen += 1
    }

    /** fonctions de génération des coordonées du point d'apparition des pellets */
    def spawnCoor(s: String): (Int,Int) = {
        val angle: Double = random.between(0, 2*math.Pi)
        val smallRadius :Int = random.between(0,INTERNAL_RADIUS)
        var x = 0
        var y = 0

        s match {
            case "H" =>
                x = (smallRadius*math.cos(angle)).toInt + CENTER_X
                y = (smallRadius*math.sin(angle)).toInt + CENTER_Y

            case "t" | "s" | "h" =>
                x = random.between(0, BOX_WIDTH)
                y = random.between(0, BOX_HEIGHT)

                // vérifier qui soient pas dans le cercle
                val distanceToCenter = (math.sqrt(math.pow(CENTER_X - x, 2) + math.pow(CENTER_Y - y, 2))).toInt
                val min: Int = EXTERNAL_RADIUS - distanceToCenter
                if (distanceToCenter < EXTERNAL_RADIUS) {// séparation en fonction de l'endroit du point selon les 4 quarts d'un cercle :
                    if(x > CENTER_X-EXTERNAL_RADIUS & x < CENTER_X){
                        var toSubX = random.between(x - (CENTER_X - EXTERNAL_RADIUS), (x - (CENTER_X - EXTERNAL_RADIUS)) * 2)
                        x -= toSubX
                    }
                    else if(x < CENTER_X+EXTERNAL_RADIUS & x > CENTER_X){
                         var toAddX = random.between((CENTER_X+EXTERNAL_RADIUS)-x, ((CENTER_X+EXTERNAL_RADIUS)-x) * 2 )
                        x += toAddX
                    }
                    if (y > CENTER_Y - EXTERNAL_RADIUS & y < CENTER_Y) {
                       var toSubY = random.between(y - (CENTER_Y - EXTERNAL_RADIUS), (y -(CENTER_Y - EXTERNAL_RADIUS)) * 2)
                        y -= toSubY
                    }
                    else if (y < CENTER_Y + EXTERNAL_RADIUS & y > CENTER_Y) {
                         var toAddY = random.between((CENTER_Y+EXTERNAL_RADIUS)-y, ((CENTER_Y+EXTERNAL_RADIUS)-y)*2)
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
