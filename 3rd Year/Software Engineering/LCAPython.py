import unittest


class Node:
    children = []
    value = None
    colour = 'white'
    count = -1

    def __init__(self, value):
        self.value = value
        self.children = []


class Graph:
    root = None

    def __init__(self, value):
        self.root = Node(value)

    def contains(self, value, node):
        if (node.value == value):
            return True

        for child in node.children:
            if (self.contains(value, child)):
                return True

        return False

    def get(self, value, node):
        if (node.value == value):
            return node

        for child in node.children:
            newNode = self.get(value, child)
            if (newNode != None):
                return newNode
        return None

    def addValue(self, value, parent):
        node = self.get(parent, self.root)
        if (node != None):
            newNode = self.get(value, self.root)
            if (newNode == None):
                newNode = Node(value)
            node.children.append(newNode)

    def colourBlue(self, value, node):
        if (node == None):
            return False

        if (node.value == value):
            node.colour = 'blue'
            return True
        result = False
        for child in node.children:
            if (self.colourBlue(value, child)):
                node.colour = 'blue'
                result = True

        return result

    def colourRed(self, value, node):
        if (node == None):
            return False

        if (node.value == value):
            if (node.colour == 'blue'):
                node.colour = 'red'
            return True
        result = False
        for child in node.children:
            if (self.colourRed(value, child)):
                if (node.colour == 'blue'):
                    node.colour = 'red'
                result = True

        return result

    def colourWhite(self, node):
        if (node == None):
            return
        node.colour = 'white'
        node.count = -1
        for child in node.children:
            self.colourWhite(child)

    def setCount(self, node):
        for child in node.children:
            value = self.setCount(child)
            if (node.count == -1):
                node.count = value
            if (value != -1):
                if (value < node.count):
                    node.count = value

        if (node.count == -1 and node.colour == 'red'):
            node.count = 0
            return node.count + 1
        elif (node.colour == 'red'):
            return node.count+1

        return -1

    def getZero(self, node):
        if (node.count == 0):
            return node.value
        for child in node.children:
            value = self.getZero(child)
            if (value != None):
                return value

        return None

    def lCA(self, value1, value2):
        self.colourBlue(value1, self.root)
        self.colourRed(value2, self.root)

        self.setCount(self.root)
        value = self.getZero(self.root)
        self.colourWhite(self.root)
        return value

    def print(self):
        return self.printNode(self.root, "")

    def printNode(self, node, prefix):
        if (node == None):
            return ""
        result = prefix + str(node.value) + " count: " + str(node.count) + "\n"
        for child in node.children:
            result += self.printNode(child, prefix + "+")
        return result


class TestBTMethods(unittest.TestCase):

    def testCreation(self):
        graph = Graph(1)
        self.assertEqual(graph.root.value, 1)
        self.assertListEqual(graph.root.children, [])

    def testLCA(self):
        graph = Graph(1)
        graph.addValue(2, 1)
        graph.addValue(3, 1)

        self.assertEqual(graph.lCA(2, 3), 1)
        self.assertEqual(graph.lCA(2, 1), 1)
        self.assertEqual(graph.lCA(1, 3), 1)

        graph.addValue(4, 2)
        graph.addValue(5, 2)
        graph.addValue(6, 3)
        graph.addValue(7, 6)

        self.assertEqual(graph.lCA(5, 4), 2)
        self.assertEqual(graph.lCA(5, 3), 1)
        self.assertEqual(graph.lCA(7, 4), 1)
        self.assertEqual(graph.lCA(7, 6), 6)

    def testLCADAGs(self):
        graph = Graph('a')
        graph.addValue('b', 'a')
        graph.addValue('c', 'a')
        graph.addValue('d', 'a')

        graph.addValue('d', 'b')
        graph.addValue('d', 'c')
        graph.addValue('e', 'd')
        graph.addValue('e', 'c')
        graph.addValue('e', 'a')

        self.assertEqual(graph.lCA('e', 'a'), 'a')
        self.assertEqual(graph.lCA('e', 'd'), 'd')
        self.assertEqual(graph.lCA('d', 'c'), 'c')
        self.assertEqual(graph.lCA('b', 'e'), 'b')


if __name__ == '__main__':
    unittest.main()
