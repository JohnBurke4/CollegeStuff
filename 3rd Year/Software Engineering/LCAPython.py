import unittest


class Node:
    children = []
    value = None

    def __init__(self, value):
        self.value = value


class Graph:
    root = None

    def __init__(self, value):
        self.root = new Node(value)

    def contains(self, value, node):
        if (node.value == value):
            return true

        for child in node.children:
            if (self.contains(value, child)):
                return true

        return false

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
            array = self.ancestors(value, node.left, array)
            if (array == []):
                array = self.ancestors(value, node.right, array)
            if (array != []):
                array.append(node.value)
            return array
        else:
            return array

    def lCA(self, valueA, valueB):
        ancestorsA = self.ancestors(value=valueA, node=self.root, array=[])
        ancestorsB = self.ancestors(valueB, self.root, [])
        for value1 in ancestorsA:
            for value2 in ancestorsB:
                if (value1 == value2):
                    return value1

    def prettyPrintValues(self):
        return self.prettyPrint(root, "")

    def prettyPrint(self, x, prefix):
        if (x == None):
            return prefix + "-null\n"
        else:
            return prefix + "-" + str(x.value) + "\n" + self.prettyPrint(x.left, prefix + " |") + self.prettyPrint(x.right, prefix + "  ")


class TestBTMethods(unittest.TestCase):

    def testCreation(self):
        root = Node(10)
        bt = BT(root)
        self.assertEqual(bt.root.value, 10)
        self.assertIsNone(bt.root.left)
        self.assertIsNone(bt.root.right)

    def testLCA(self):
        root = Node(10)
        bt = BT(root)
        bt.addValue(5)
        bt.addValue(15)
        bt.addValue(4)
        bt.addValue(6)
        bt.addValue(12)
        bt.addValue(11)
        bt.addValue(14)
        bt.addValue(13)

        self.assertEqual(bt.lCA(5, 15), 10)
        self.assertEqual(bt.lCA(4, 13), 10)
        self.assertEqual(bt.lCA(4, 6), 5)
        self.assertEqual(bt.lCA(5, 4), 5)
        self.assertEqual(bt.lCA(13, 11), 12)
        self.assertEqual(bt.lCA(14, 15), 15)


if __name__ == '__main__':
    unittest.main()
