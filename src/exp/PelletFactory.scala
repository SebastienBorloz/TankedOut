package exp

import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.math.Vector2

import scala.collection.mutable.ArrayBuffer
import scala.util.Random.javaRandomToRandom

class PelletFactory {
    var triangleStash: ArrayBuffer[trianglePellet] = new ArrayBuffer[trianglePellet]()
    var squareStash: ArrayBuffer[squarePellet] = new ArrayBuffer[squarePellet]()
    var hexagonStash: ArrayBuffer[hexagonPellet] = new ArrayBuffer[hexagonPellet]()
    var bigHexaStash: ArrayBuffer[bigHexaPellet] = new ArrayBuffer[bigHexaPellet]()
    /** valeurs du nombres maximum de chaque pellets */
    val NBR_TRIANGLES = 1000
    val NBR_SQUARES = 1000
    val NBR_HEXAGONS = 1000
    val NBR_BIGHEXAS = 4

    var lastTriangle: Long = 0
    var lastSquare: Long = 0
    var lastHexagon: Long = 0
    var lastBigHexa: Long = 0

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
                spawnTriangle()
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
                spawnSquare()
            }
        }


        if(hexagonStash.length < NBR_HEXAGONS & gen >= NBR_HEXAGONS) {
            if (System.currentTimeMillis() > lastHexagon + 10000) {
                lastHexagon = System.currentTimeMillis()
                spawnHexagon()
            }
        }
        if (hexagonStash.length < NBR_HEXAGONS & gen < NBR_HEXAGONS) {
            if (System.currentTimeMillis() > lastHexagon) {
                lastHexagon = System.currentTimeMillis()
                spawnHexagon()
            }
        }

        if(bigHexaStash.length < NBR_BIGHEXAS & gen >= NBR_BIGHEXAS) {
            if (System.currentTimeMillis() > lastBigHexa + 30000) {
                lastBigHexa = System.currentTimeMillis()
                spawnBigHexa()
            }
        }
        else if(bigHexaStash.length < NBR_BIGHEXAS & gen < NBR_BIGHEXAS) {
            if (System.currentTimeMillis() > lastBigHexa) {
                lastBigHexa = System.currentTimeMillis()
                spawnBigHexa()
            }
        }
        gen += 1
    }

    /** fonctions de génération des coordonées du point d'apparition des pellets */
    def spawnCoorX(s: String): Int = {
        val innerRad: Int = 50
        var ausserRad: Int = 350
        val centerX: Int = 3000
        var coorX: Int = 0
        s match {
            case "H" => coorX = random.between(centerX - innerRad, centerX + innerRad)
            case "t" | "s" | "h" =>
                do {
                    coorX = random.between(0, centerX * 2)
                }
                while (coorX > centerX - 100 & coorX < centerX + 100)
        }
        return coorX
    }

    def spawnCoorY(s: String): Int = {
        val innerRad: Int = 50
        var ausserRad: Int = 350
        val centerY: Int = 500
        var coorY: Int = 0
        s match {
            case "H" => coorY = random.between(centerY - innerRad, centerY + innerRad)
            case "t"|"s"|"h" =>
                do {
                    coorY = random.between(0, centerY * 2)
                }
                while (coorY > centerY - 100 & coorY < centerY + 100)

        }
        return coorY
    }

    def spawnTriangle(): Unit ={
        println("triangle flag!")
        val positiongX: Int = spawnCoorX("t")
        val positiongY: Int = spawnCoorX("t")
        var spawnPoint: Vector2 = new Vector2(positiongX,positiongY)
        triangleStash.addOne(new trianglePellet(spawnPoint))
    }

    def spawnSquare(): Unit ={
        println("square flag!")
        val positiongX: Int = spawnCoorX("s")
        val positiongY: Int = spawnCoorX("s")
        var spawnPoint: Vector2 = new Vector2(positiongX, positiongY)
        squareStash.addOne(new squarePellet(spawnPoint))
    }

    def spawnHexagon(): Unit ={
        println("hexagon flag!")
        val positiongX: Int = spawnCoorX("h")
        val positiongY: Int = spawnCoorX("h")
        var spawnPoint: Vector2 = new Vector2(positiongX, positiongY)
        hexagonStash.addOne(new hexagonPellet(spawnPoint))
    }

    def spawnBigHexa(): Unit = {
        println("bigHexa flag!")
        val positiongX: Int = spawnCoorX("H")
        val positiongY: Int = spawnCoorY("H")
        var spawnPoint: Vector2 = new Vector2(positiongX, positiongY)
        bigHexaStash.addOne(new bigHexaPellet(spawnPoint))
    }
}
