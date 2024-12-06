import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2

class Node(private val i: Int, private val j: Int, val nodeIndex: Int){
    private val walls = mutableListOf(true, true, true, true)
    var visited = false
    private var highlight = false
    var setKey: Int = nodeIndex
    fun show(drawer: Drawer, scale: Double){
        val x  = i * scale
        val y = j * scale
        //Check if the node should be highlighted
        if(highlight){
            drawer.fill = ColorRGBa.RED
            drawer.rectangle(x,y,scale,scale)
        }
        drawer.stroke = ColorRGBa.BLACK
        drawer.fill = null
        val lineSegmentVectors = mutableListOf<Vector2>()
        //north wall - draw wall when true
        if(this.walls[0]){
            lineSegmentVectors.add(Vector2(x, y))
            lineSegmentVectors.add(Vector2(x + scale, y))
        }
        //south wall
        if(this.walls[1]){
            lineSegmentVectors.add(Vector2(x + scale, y + scale))
            lineSegmentVectors.add(Vector2(x , y + scale))
        }
        //west wall
        if(this.walls[2]){
            lineSegmentVectors.add(Vector2(x , y + scale))
            lineSegmentVectors.add(Vector2(x , y))
        }
        //east wall
        if(this.walls[3]){
            lineSegmentVectors.add(Vector2(x + scale, y))
            lineSegmentVectors.add(Vector2(x + scale, y + scale))
        }
        drawer.lineSegments(lineSegmentVectors)

    }

    fun getNeighbours(cols:Int, rows: Int, nodes: List<Node>): List<Node>{
        //Return array of valid node neighbours
        //Array size will at maximum be 4

        //Return array of valid node neighbours
        //Array size will at maximum be 4
        val neighbours = mutableListOf<Node>()
        val top: Node
        val bottom: Node
        val left: Node
        val right: Node

        //these are the cell neighbours
        //Calculate node index for array position of the neighbour node
        // will return -1 if the node index is out of bounds
        //these are the cell neighbours
        //Calculate node index for array position of the neighbour node
        // will return -1 if the node index is out of bounds
        val topNodeIndexNum: Int = Maze.nodeIndex(i, j - 1, cols, rows)
        if (topNodeIndexNum != -1) {
            top = nodes[topNodeIndexNum]
            //Check that node has not been visited.
            //Add to the return array of valid neighbours
            if (!top.visited) {
                neighbours.add(top)
            }
        }

        val bottomNodeIndexNum = Maze.nodeIndex(i, j + 1, cols, rows)
        if (bottomNodeIndexNum != -1) {
            bottom = nodes[bottomNodeIndexNum]
            //Check that node has not been visited.
            //Add to the return array of valid neighbours
            if (!bottom.visited) {
                neighbours.add(bottom)
            }
        }

        val leftNodeIndexNum = Maze.nodeIndex(i - 1, j, cols, rows)
        if (leftNodeIndexNum != -1) {
            left = nodes[leftNodeIndexNum]
            //Check that node has not been visited.
            //Add to the return array of valid neighbours
            if (!left.visited) {
                neighbours.add(left)
            }
        }

        val rightNodeIndexNum = Maze.nodeIndex(i +1,  j, cols, rows)
        if (rightNodeIndexNum != -1) {
            right = nodes[rightNodeIndexNum]
            //Check that node has not been visited.
            //Add to the return array of valid neighbours
            if (!right.visited) {
                neighbours.add(right)
            }
        }

        return neighbours
    }

    fun removeWall(adjacentNode: Node){
        val x = i - adjacentNode.i

        if (x == 1) {
            walls[2] = false
            adjacentNode.walls[3] = false
        } else if (x == -1) {
            walls[3] = false
            adjacentNode.walls[2] = false

        }

        val y = j - adjacentNode.j

        if (y == 1) {
            walls[0] = false
            adjacentNode.walls[1] = false
        } else if (y == -1) {
            walls[1] = false
            adjacentNode.walls[0] = false
        }


    }
}