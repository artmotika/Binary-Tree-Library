import nodes.AVLNode
import trees.AVLTree
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*

class AVLTreeTest {
    private fun calculateHeight(node: AVLNode<Int, Int>?): Int {
        if (node == null)
            return 0

        return maxOf(calculateHeight(node.left), calculateHeight(node.right)) + 1
    }

    private fun getHeight(node: AVLNode<Int, Int>?): Int =
        node?.height ?: 0

    private fun checkHeight(node: AVLNode<Int, Int>?): Boolean =
        calculateHeight(node) == getHeight(node)

    private fun checkBalance(node: AVLNode<Int, Int>?): Boolean =
        node?.getBalance() ?: 0 in -1..1

    private var tree = AVLTree<Int, Int>()

    @BeforeEach
    fun makeTree() {
        tree = AVLTree()
    }

    @Nested
    inner class UnitOfAVLTreeIteratorMethodTests {
        @Test
        fun emptyIteratorTest() {
            val nodes = mutableListOf<AVLNode<Int, Int>>()

            for (node in tree) {
                nodes.add(node)
            }
            assertTrue(nodes.isEmpty())
        }

        @Test
        fun nextInEmptyTreeTest() {
            assertThrows(NoSuchElementException::class.java) {
                tree.iterator().next()
            }
        }

        @Test
        fun iteratorTest() {
            val keys = mutableListOf<Int>()

            for (i in -100..100) {
                keys.add(-i)
                tree.insert(-i, i)
            }

            for (node in tree) {
                assertTrue(node.key in keys)
                keys.remove(node.key)
            }

            assertTrue(keys.isEmpty())
        }
    }

    @Nested
    inner class UnitOfAVLTreeFindMethodTests {
        @Test
        fun findInEmptyTreeTest() {
            for (i in -100..100)
                assertNull(tree.find(i))
        }

        @Test
        fun findEachElementTest() {
            for (i in -100..100)
                tree.insert(i, -i)

            for (i in -100..100)
                assertEquals(tree.find(i), -i)
        }

        @Test
        fun findRemovedElementsTest() {
            for (i in -100..100)
                tree.insert(i, i)

            for (i in -10..10)
                tree.remove(i)

            for (i in -10..10)
                assertNull(tree.find(i))
        }

        @Test
        fun findNonexistentElementsTest() {
            for (i in -100..0)
                tree.insert(i, i)

            for (i in 1..100)
                assertNull(tree.find(i))
        }
    }

    @Nested
    inner class UnitOfAVLTreeInsertMethodTests {
        @Test
        fun insertSeveralTimesInTheSameKeyTest() {
            for (i in -100..100)
                tree.insert(1, i)

            assertEquals(tree.find(1), 100)
        }

        @Test
        fun insertInReversedOrderTest() {
            for (i in 100 downTo -100)
                tree.insert(i, -i)

            for (i in -100..100)
                assertEquals(tree.find(i), -i)
        }

        @Test
        fun insertInTheMiddleTest() {
            for (i in 100 downTo 0) {
                tree.insert(i, -i)
                tree.insert(-i, i)
            }

            for (i in -100..100)
                assertEquals(tree.find(i), -i)
        }

        @Test
        fun insertInEvenAndOddOrderTest() {
            for (i in -100..100) {
                if (i % 2 == 0)
                    tree.insert(i, -i)
            }

            for (i in -100..100) {
                if (i % 2 != 0)
                    tree.insert(i, -i)
            }

            for (i in -100..100)
                assertEquals(tree.find(i), -i)
        }

        @Test
        fun oneElementInvariantsCheckTest() {
            tree.insert(1, 1)

            for (node in tree) {
                assertTrue(checkHeight(node))
                assertTrue(checkBalance(node))
            }
        }

        @Test
        fun InsertInvariantsCheckTest() {
            for (i in 0..50)
                tree.insert(i, i)

            for (node in tree) {
                assertTrue(checkHeight(node))
                assertTrue(checkBalance(node))
            }
        }

        @Test
        fun reversedOrderInvariantsCheckTest() {
            for (i in 0..50)
                tree.insert(-i, i)

            for (node in tree) {
                assertTrue(checkHeight(node))
                assertTrue(checkBalance(node))
            }
        }


        @Test
        fun insertInTheMiddleInvariantsCheckTest() {
            for (i in 0..50) {
                tree.insert(i, -i)
                tree.insert(-i, i)
            }

            for (node in tree) {
                assertTrue(checkHeight(node))
                assertTrue(checkBalance(node))
            }
        }

        @Test
        fun evenAndOddOrderInvariantsCheckTest() {
            for (i in -50..50) {
                if (i % 2 == 0)
                    tree.insert(i, -i)
            }

            for (i in -50..50) {
                if (i % 2 != 0)
                    tree.insert(i, -i)
            }

            for (node in tree) {
                assertTrue(checkHeight(node))
                assertTrue(checkBalance(node))
            }
        }
    }

    @Nested
    inner class UnitOfAVLTreeRemoveMethodTests {
        @Test
        fun removeInEmptyTreeTest() {
            for (i in -100..100)
                assertFalse(tree.remove(i))
        }

        @Test
        fun removeElementsTest() {
            for (i in -100..100)
                tree.insert(i, i)

            for (i in -100..0)
                assertTrue(tree.remove(i))

            for (i in -100..0)
                assertNull(tree.find(i))
        }

        @Test
        fun removeNonexistentElementsTest() {
            for (i in -100..0)
                tree.insert(i, i)

            for (i in 1..100)
                assertFalse(tree.remove(i))
        }

        @Test
        fun removeLeafRootTest() {
            tree.insert(1, 1)
            assertTrue(tree.remove(1))
            assertNull(tree.find(1))
        }

        @Test
        fun removeRootWithRightSonTest() {
            tree.insert(1, 1)
            tree.insert(2, 2)
            assertTrue(tree.remove(1))
            assertNull(tree.find(1))
            assertEquals(tree.find(2), 2)
            for (node in tree) {
                assertTrue(checkHeight(node))
                assertTrue(checkBalance(node))
            }
        }

        @Test
        fun removeRootWithLeftSonTest() {
            tree.insert(2, 2)
            tree.insert(1, 1)
            assertTrue(tree.remove(2))
            assertNull(tree.find(2))
            assertEquals(tree.find(1), 1)
            for (node in tree) {
                assertTrue(checkHeight(node))
                assertTrue(checkBalance(node))
            }
        }

        @Test
        fun removeEachElementTest() {
            for (i in -100..100)
                tree.insert(i, i)

            for (i in -100..100)
                assertTrue(tree.remove(i))

            for (i in -100..100)
                assertNull(tree.find(i))
        }

        @Test
        fun removeMiddleElementAndCheckInvariantsTest() {
            for (i in -100..100)
                tree.insert(i, i)

            assertTrue(tree.remove(0))

            for (node in tree) {
                assertTrue(checkHeight(node))
                assertTrue(checkBalance(node))
            }
        }

        @Test
        fun removeMinElementAndCheckInvariantsTest() {
            for (i in -100..100)
                tree.insert(i, i)

            assertTrue(tree.remove(-100))

            for (node in tree) {
                assertTrue(checkHeight(node))
                assertTrue(checkBalance(node))
            }
        }

        @Test
        fun removeMaxElementAndCheckInvariantsTest() {
            for (i in -100..100)
                tree.insert(i, i)

            assertTrue(tree.remove(100))

            for (node in tree) {
                assertTrue(checkHeight(node))
                assertTrue(checkBalance(node))
            }
        }

        @Test
        fun removeEvenElementsAndCheckInvariantsTest() {
            for (i in -100..100)
                tree.insert(i, i)

            for (i in -100..100)
                if (i % 2 == 0)
                    assertTrue(tree.remove(i))

            for (node in tree) {
                assertTrue(checkHeight(node))
                assertTrue(checkBalance(node))
            }
        }

        @Test
        fun removeAllElementsExceptOnrAndCheckInvariantsTest() {
            for (i in -100..100)
                tree.insert(i, i)

            for (i in -100..100)
                if (i != 0)
                    assertTrue(tree.remove(i))

            for (node in tree) {
                assertTrue(checkHeight(node))
                assertTrue(checkBalance(node))
            }
        }
    }
}