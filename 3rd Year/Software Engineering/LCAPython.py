import unittest


class Node:
    children = []
    value = None

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
        if (self.contains(value, self.root)):
            return
        else:
            node = self.get(parent, self.root)
            if (node != None):
                newNode = Node(value)
                node.children.append(newNode)

    def ancestors(self, value, node, array):
        if (node == None):
            return array

        elif (node.value == value):
            array.append(value)
            return array
        elif (array == []):

            for child in node.children:
                lst = self.ancestors(value, child, array)
                if (lst != []):
                    lst.append(node.value)
                    break
            return array
        else:
            return array

    def lCA(self, valueA, valueB):
        ancestorsA = self.ancestors(valueA, self.root, [])
        ancestorsB = self.ancestors(valueB, self.root, [])
        for value1 in ancestorsA:
            for value2 in ancestorsB:
                if (value1 == value2):
                    return value1

    def print(self):
        return self.printNode(self.root, "")

    def printNode(self, node, prefix):
        if (node == None):
            return ""
        result = prefix + str(node.value) + "\n"
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


if __name__ == '__main__':
    unittest.main()
