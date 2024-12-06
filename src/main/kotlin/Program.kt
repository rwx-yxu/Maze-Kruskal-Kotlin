import org.openrndr.application
import org.openrndr.color.ColorRGBa
import kotlin.math.floor

fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        val scale = 40.0
        //Calculate columns and rows based on width and height of canvas
        val cols = floor(drawer.width / scale)
        val rows = floor(drawer.height / scale)

        val maze = Maze(cols = cols, rows = rows)



        extend {
            drawer.clear(ColorRGBa.WHITE)
            maze.show(drawer, scale)
        }
    }
}