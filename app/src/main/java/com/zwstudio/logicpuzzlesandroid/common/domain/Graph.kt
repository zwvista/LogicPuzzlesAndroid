package com.zwstudio.logicpuzzlesandroid.common.domain

import java.util.*

class Graph {
    var rootNode: Node? = null
    var nodes: ArrayList<*> = ArrayList<Any?>()
    var adjMatrix //Edges will be represented as adjacency Matrix
        : Array<IntArray>?
    var size = 0

    fun addNode(n: Node?) {
        nodes.add(n)
    }

    //This method will be called to make connect two nodes
    fun connectNode(start: Node?, end: Node?) {
        if (adjMatrix == null) {
            size = nodes.size
            adjMatrix = Array(size) { IntArray(size) }
        }
        val startIndex = nodes.indexOf(start)
        val endIndex = nodes.indexOf(end)
        adjMatrix!![startIndex][endIndex] = 1
        adjMatrix!![endIndex][startIndex] = 1
    }

    private fun getUnvisitedChildNode(n: Node): Node? {
        val index = nodes.indexOf(n)
        var j = 0
        while (j < size) {
            if (adjMatrix!![index][j] == 1 && (nodes[j] as Node).visited == false) {
                return nodes[j] as Node
            }
            j++
        }
        return null
    }

    //BFS traversal of a tree is performed by the bfs() function
    fun bfs(): List<Node?> {
        val nodeList: MutableList<Node?> = ArrayList()
        //BFS uses Queue data structure
        val q: Queue<*> = LinkedList<Any?>()
        q.add(rootNode)
        //printNode(this.rootNode);
        nodeList.add(rootNode)
        rootNode!!.visited = true
        while (!q.isEmpty()) {
            val n = q.remove() as Node
            var child: Node? = null
            while (getUnvisitedChildNode(n).also { child = it } != null) {
                child!!.visited = true
                //printNode(child);
                nodeList.add(child)
                q.add(child)
            }
        }
        //Clear visited property of nodes
        clearNodes()
        return nodeList
    }

    //DFS traversal of a tree is performed by the dfs() function
    fun dfs() {
        //DFS uses Stack data structure
        val s: Stack<*> = Stack<Any?>()
        s.push(rootNode)
        rootNode!!.visited = true
        printNode(rootNode)
        while (!s.isEmpty()) {
            val n = s.peek() as Node
            val child = getUnvisitedChildNode(n)
            if (child != null) {
                child.visited = true
                printNode(child)
                s.push(child)
            } else {
                s.pop()
            }
        }
        //Clear visited property of nodes
        clearNodes()
    }

    //Utility methods for clearing visited property of node
    private fun clearNodes() {
        var i = 0
        while (i < size) {
            val n = nodes[i] as Node
            n.visited = false
            i++
        }
    }

    //Utility methods for printing the node's label
    private fun printNode(n: Node?) {
        print(n!!.label + " ")
    }
}