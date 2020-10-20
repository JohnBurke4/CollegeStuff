import unittest


class Node:
    left = None
    right = None
    value = None
    height = None

    def __init__(self, value):
        self.value = value


class BT:
    root = None

    def __init__(self, root):
        self.root = root
        self.root.height = 1

    def addValue(self, value):
        newNode = Node(value)
        node = self.root
        while (node != None):
            if (value == node.value):
                node.value = value
                node = None
            elif (value < node.value):
                if (node.left == None):
                    newNode.height = node.height + 1
                    node.left = newNode
                    node = None
                else:
                    node = node.left
            else:
                if (node.right == None):
                    newNode.height = node.height + 1
                    node.right = newNode
                    node = None
                else:
                    node = node.right

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
        ancestorsA = self.ancestors(value=valueA, node=root, array=[])
        ancestorsB = self.ancestors(valueB, root, [])
        print(ancestorsA)
        print(ancestorsB)
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


if __name__ == '__main__':
    unittest.main()
