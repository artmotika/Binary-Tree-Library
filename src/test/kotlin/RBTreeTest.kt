import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import trees.RBTree
import nodes.RBNode
import nodes.RBNode.Color

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RBTreeTest {

    private tailrec fun getHeight(leaf: RBNode<Int, Int>?, height: Int = 1): Int {
        return when (leaf?.color) {
            Color.Red -> getHeight(leaf.parent, height)
            Color.Black -> getHeight(leaf.parent, height + 1)
            else -> height
        }
    }

    private fun verifyTree(tree: RBTree<Int, Int>): Boolean {
        val root: RBNode<Int, Int> =
            when (tree.iterator().hasNext()) {
                true -> tree.iterator().next()
                false -> null
            } ?: return true

        val leaves = mutableListOf<RBNode<Int, Int>>()

        if (root.color != Color.Black)
            return false

        for (node in tree) {

            if (!(node.left == null || node.left!!.key <= node.key) ||
                !(node.right == null || node.right!!.key >= node.key)
            )
                return false

            if (node.color == Color.Red &&
                !((node.left == null || node.left?.color == Color.Black)
                        && (node.right == null || node.right?.color == Color.Black))
            )
                return false

            if (node.isLeaf())
                leaves.add(node)
        }

        val height = getHeight(leaves[0])

        for (leaf in leaves)
            if (getHeight(leaf) != height)
                return false

        return true
    }

    private lateinit var tree: RBTree<Int, Int>

    @BeforeEach
    fun initializeTree() {
        tree = RBTree()
    }

    @Nested
    inner class UnitOfRBTreeInsertMethodTests {

        @Test
        fun `Insert into empty tree`() {

            val key = 5

            tree.insert(key, key)

            val root = tree.iterator().next()

            assertNotNull(tree.find(key))

            assertEquals(root.key, key)
        }

        @Test
        fun `Inserting node is left child`() {

            val keys = listOf(4, 6, 55, 81, 5, 29, 13, 8, 19, 70)

            for (key in keys)
                tree.insert(key, key)

            tree.insert(12, 12)

            assertNotNull(tree.find(12))

            assertTrue(verifyTree(tree))
        }

        @Test
        fun `Inserting node is right child`() {

            val keys = listOf(4, 6, 55, 81, 5, 29, 13, 8, 19, 70)

            for (key in keys)
                tree.insert(key, key)

            tree.insert(56, 56)

            assertNotNull(tree.find(56))

            assertTrue(verifyTree(tree))
        }

        @Test
        fun `Inserting node's parent is black`() {

            val keys = listOf(4, 6, 55, 81, 5, 29, 13, 8, 19, 70)

            for (key in keys)
                tree.insert(key, key)

            tree.insert(82, 82)

            assertNotNull(tree.find(82))

            assertTrue(verifyTree(tree))
        }

        @Test
        fun `Inserting node's parent is red`() {

            val keys = listOf(4, 6, 55, 81, 5, 29, 13, 8, 19, 70)

            for (key in keys)
                tree.insert(key, key)

            tree.insert(3, 3)

            assertNotNull(tree.find(3))

            assertTrue(verifyTree(tree))
        }

        @Test
        fun `Inserting node with existing key`() {

            val keys = listOf(3, 4, 7, 9, 10, -4)

            for (key in keys)
                tree.insert(key, key)

            tree.insert(9, 18)

            assertEquals(tree.find(9), 18)
            assertTrue(verifyTree(tree))
        }

        @Test
        fun `Alternating insertion of the min and max node`() {
            
            val keys = listOf(10, 1, 9, 2, 8, 3, 7, 4, 6, 5)
            
            for (key in keys) {
                tree.insert(key, key)
                assertTrue(verifyTree(tree))
            }
            
            for (key in keys)
                assertNotNull(tree.find(key))
        }

        @Test
        fun `Inserting in increasing order`() {

            for (key in 1..15) {
                tree.insert(key, key)
                assertTrue(verifyTree(tree))
            }

            for (key in 1..15) {
                assertNotNull(tree.find(key))
            }
        }

        @Test
        fun `Inserting in decreasing order`() {

            for (key in 1..15) {
                tree.insert(16 - key, 16 - key)
                assertTrue(verifyTree(tree))
            }

            for (key in 1..15) {
                assertNotNull(tree.find(16 - key))
            }
        }
    }

    @Nested
    inner class UnitOfRBTreeIteratorMethodTests {

        @Test
        fun `Empty tree iterator`() {

            assertFalse(tree.iterator().hasNext())
        }

        @Test
        fun `Exception when calling next in empty tree`() {

            assertThrows(NoSuchElementException::class.java) {
                tree.iterator().next()
            }
        }

        @Test
        fun `Root is first element in iterator`() {

            val keys = listOf(3, 4, 7, 2, 10, 11)

            for (key in keys)
                tree.insert(key, key)

            val rootKey = tree.iterator().next().key

            assertEquals(rootKey, 4)
        }

        @Test
        fun `The iterator goes through all vertices`() {

            val keys = mutableListOf(1, 3, 10, 19, -5)

            for (key in keys)
                tree.insert(key, key)

            for (node in tree) {
                assertTrue(node.key in keys)
                keys.remove(node.key)
            }

            assertTrue(keys.isEmpty())
        }

        @Test
        fun `Traversal in the correct BFS order`() {

            val keys = listOf(2, 3, 10, 19, 1)

            val correctOrder = listOf(3, 2, 10, 1, 19)

            for (key in keys)
                tree.insert(key, key)

            for ((i, node) in tree.withIndex()) {
                assertEquals(node.key, correctOrder[i])
            }
        }
    }

    @Nested
    inner class UnitOfRBTreeFindMethodTests {

        @Test
        fun `Find in empty tree test`() {

            assertNull(tree.find(1))
        }

        @Test
        fun `Find nonexistent node test`() {

            val keys = listOf(2, 3, 10, 19, 1)

            for (key in keys)
                tree.insert(key, key)

            assertNull(tree.find(4))
        }

        @Test
        fun `Correct search of existing nodes`() {

            val keys = listOf(9, 1, 3, 6, 11)

            for (key in keys)
                tree.insert(key, 2 * key)

            for (key in keys)
                assertEquals(tree.find(key), 2 * key)
        }

        @Test
        fun `Tree invariants saves after search`() {

            val keys = listOf(9, 1, 3, 6, 11)

            for (key in keys)
                tree.insert(key, 2 * key)

            assertTrue(verifyTree(tree))
        }
    }

    @Nested
    inner class UnitOfRBTreeRemoveMethodTests {

        @Test
        fun `Remove from empty tree`() {

            assertFalse(tree.remove(1))
        }

        @Test
        fun `Remove root`() {

            for (key in 1..15)
                tree.insert(key, key)

            val rootKey = tree.iterator().next().key

            tree.remove(rootKey)

            assertNull(tree.find(rootKey))
            assertTrue(verifyTree(tree))
        }

        @Test
        fun `Removing node's brother is black`() {

            for (key in 1..15)
                tree.insert(key, key)

            tree.remove(8)

            assertNull(tree.find(8))

            assertTrue(verifyTree(tree))
        }

        @Test
        fun `Removing node's brother is red`() {

            for (key in 1..15)
                tree.insert(key, key)

            tree.remove(2)

            assertNull(tree.find(2))

            assertTrue(verifyTree(tree))
        }

        @Test
        fun `Removing node is left child`() {

            for (key in 1..15)
                tree.insert(key, key)

            tree.remove(6)

            assertNull(tree.find(6))

            assertTrue(verifyTree(tree))
        }

        @Test
        fun `Removing node is right child`() {

            for (key in 1..15)
                tree.insert(key, key)

            tree.remove(10)

            assertNull(tree.find(10))

            assertTrue(verifyTree(tree))
        }

        @Test
        fun `Alternating removing of the min and max node`() {

            val keys = mutableSetOf(10, 1, 9, 2, 8, 3, 7, 4, 6, 5)

            for (key in 1..10)
                tree.insert(key, key)

            for (key in keys) {
                tree.remove(key)
                assertTrue(verifyTree(tree))
            }
            
            assertFalse(tree.iterator().hasNext())
        }
        
        @Test
        fun `Removing nodes in increasing order`() {

            for (key in 1..15)
                tree.insert(key, key)

            for (key in 1..15) {
                tree.remove(key)
                assertTrue(verifyTree(tree))
            }

            assertFalse(tree.iterator().hasNext())
        }

        @Test
        fun `Removing nodes in decreasing order`() {

            for (key in 1..15)
                tree.insert(16 - key, 16 - key)

            for (key in 1..15) {
                tree.remove(16 - key)
                assertTrue(verifyTree(tree))
            }

            assertFalse(tree.iterator().hasNext())
        }


        @Test
        fun `Remove nonexistent key`() {

            val keys = listOf(3, 4, 8, 20, -10, 11)

            for (key in keys)
                tree.insert(key, key)

            assertFalse(tree.remove(5))
        }

        @Test
        fun `Removing in scattered order`() {

            for (key in 1..15)
                tree.insert(key, key)

            tree.remove(3)
            assertNull(tree.find(3))
            verifyTree(tree)

            tree.remove(14)
            assertNull(tree.find(14))
            verifyTree(tree)

            tree.remove(6)
            assertNull(tree.find(6))
            verifyTree(tree)
        }
    }
}