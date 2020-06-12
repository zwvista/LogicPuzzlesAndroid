package com.zwstudio.logicpuzzlesandroid.common.domain

import java.util.*

class Node(var label: String) {
    var visited = false
}

class Graph {
    lateinit var rootNode: Node
    private var nodes = mutableListOf<Node>()
    private var adjMatrix //Edges will be represented as adjacency Matrix
        : Array<IntArray>? = null
    var size = 0

    fun addNode(n: Node) = nodes.add(n)

    //This method will be called to make connect two nodes
    fun connectNode(start: Node, end: Node) {
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
        for (j in 0 until size)
            if (adjMatrix!![index][j] == 1 && !nodes[j].visited)
                return nodes[j]
        return null
    }

    //BFS traversal of a tree is performed by the bfs() function
    fun bfs(): List<Node> {
        val nodeList = mutableListOf<Node>()
        //BFS uses Queue data structure
        val q = mutableListOf<Node>()
        q.add(rootNode)
        //printNode(this.rootNode);
        nodeList.add(rootNode)
        rootNode.visited = true
        while (q.isNotEmpty()) {
            val n = q.removeAt(0)
            while (true) {
                val child = getUnvisitedChildNode(n) ?: break
                child.visited = true
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
        val s = Stack<Node>()
        s.push(rootNode)
        rootNode.visited = true
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
        for (i in 0 until size)
            nodes[i].visited = false
    }

    //Utility methods for printing the node's label
    private fun printNode(n: Node) {
        print(n.label + " ")
    }
}