import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import trees.BSTree
import nodes.BSNode

class BSTreeTest {
    private var tree = BSTree<Int, Int>()

    @BeforeEach
    fun makeTree() {
        tree = BSTree()
    }

    //iterator function tests
    @Test
    fun `iterate empty tree`() {
        val nodes = mutableListOf<BSNode<Int, Int>>()

        for (node in tree) {
            nodes.add(node)
        }
        assertTrue(nodes.isEmpty())
    }

    @Test
    fun `iterator test`() {
        var countOfNodes = 0
        tree.insert(1, 2)
        tree.insert(2, 3)

        for (node in tree) {
            countOfNodes++
        }
        assertEquals(countOfNodes, 2)
    }

    //find function tests
    @Test
    fun `find in empty tree test`() {
        assertNull(tree.find(1))
    }

    @Test
    fun `find properly test`() {
        tree.insert(1, 2)
        assertEquals(tree.find(1), 2)
    }

    @Test
    fun `find nonexistent key test`() {
        tree.insert(1, 2)
        assertNull(tree.find(2))
    }

    //insert function tests
    @Test
    fun `insert several times in the same key test`() {
        tree.insert(1, 2)
        tree.insert(1, 3)
        assertNotNull(tree.find(1))
        assertEquals(tree.find(1), 3)
    }

    @Test
    fun `insert 2 nodes with right son test`() {
        tree.insert(1, 2)
        tree.insert(2, 3)
        assertEquals(tree.find(1), 2)
        assertEquals(tree.find(2), 3)
    }

    @Test
    fun `insert 2 nodes with left son test`() {
        tree.insert(1, 2)
        tree.insert(-1, 3)
        assertEquals(tree.find(1), 2)
        assertEquals(tree.find(-1), 3)
    }

    //remove function tests
    @Test
    fun `remove from empty tree test`() {
        assertFalse(tree.remove(1))
    }

    @Test
    fun `remove nonexistent key test`() {
        tree.insert(1, 2)
        assertFalse(tree.remove(2))
    }

    @Test
    fun `add nodes and remove all nodes test`() {
        val nodes = mutableListOf<BSNode<Int, Int>>()

        tree.insert(1, 4)
        tree.insert(3, 4)
        tree.insert(2, 3)
        tree.insert(-3, 3)

        tree.remove(3)
        tree.remove(1)
        tree.remove(2)
        tree.remove(-3)

        for (node in tree) {
            nodes.add(node)
        }
        assertTrue(nodes.isEmpty())
    }

    @Test
    fun `remove root with right son test`() {
        tree.insert(1, 1)
        tree.insert(2, 2)
        assertTrue(tree.remove(1))
        assertNull(tree.find(1))
        assertEquals(tree.find(2), 2)
    }

    @Test
    fun `remove root with left son test`() {
        tree.insert(2, 2)
        tree.insert(1, 1)
        assertTrue(tree.remove(2))
        assertNull(tree.find(2))
        assertEquals(tree.find(1), 1)
    }

    @Test
    fun `remove node with 2 kids test`() {
        var countOfNodes = 0
        tree.insert(7, 15)
        tree.insert(3, 10)
        tree.insert(2, 9)
        tree.insert(4, 11)
        tree.remove(3)
        for (node in tree) {
            countOfNodes++
        }

        assertEquals(countOfNodes, 3)
    }

    @Test
    fun `remove node with 2 kids which have 2 kids too test`() {
        var countOfNodes = 0
        tree.insert(20, 1)
        tree.insert(15, 2)
        tree.insert(10, 3)
        tree.insert(17, 4)
        tree.insert(5, 5)
        tree.insert(12, 6)
        tree.insert(16, 7)
        tree.insert(18, 8)
        tree.remove(15)
        for (node in tree) {
            countOfNodes++
        }

        assertEquals(countOfNodes, 7)
    }
}

