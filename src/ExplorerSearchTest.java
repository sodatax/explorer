import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class ExplorerSearchTest {
    @Test
    public void testReachableArea_someUnreachable() {
        int[][] island = {
            {1,1,1,3,1,1},
            {3,2,3,1,3,1},
            {1,1,1,1,3,3},
            {3,1,2,1,0,1},
            {1,1,1,2,1,1},
        };
        int actual = ExplorerSearch.reachableArea(island);
        assertEquals(14, actual);
    }

    // Add more tests here!
    // Come up with varied cases
    @Test
    public void testExplorerLocation_centerOfGrid() {
        int[][] island = {
            {1, 1, 1},
            {1, 0, 1},
            {1, 1, 1}
        };
        int[] expected = {1, 1};
        assertArrayEquals(expected, ExplorerSearch.explorerLocation(island));
    }

    @Test
    public void testExplorerLocation_topLeftCorner() {
        int[][] island = {
            {0, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
        };
        int[] expected = {0, 0};
        assertArrayEquals(expected, ExplorerSearch.explorerLocation(island));
    }

    @Test
    public void testSalamanderLocation_notFound_throwsException() {
        int[][] island = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
        };
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ExplorerSearch.explorerLocation(island);
        });
        assertEquals("No explorer present", exception.getMessage());
    }


    @Test
    public void testExplorerLocation_at_1_2() {
        int[][] island = {
            {1, 1, 1},
            {1, 1, 0},
            {1, 1, 1}
        };
        int[] expected = {1, 2};
        assertArrayEquals(expected, ExplorerSearch.explorerLocation(island));
    }

    @Test
    public void testPossibleMoves_allDirectionsOpen() {
        int[][] island = {
            {1, 1, 1},
            {1, 0, 1},
            {1, 1, 1}
        };
        int[] location = {1, 1};
        List<int[]> moves = ExplorerSearch.possibleMoves(island, location);
        Set<String> moveSet = toSet(moves);

        assertEquals(4, moves.size());
        assertTrue(moveSet.contains("0,1")); // up
        assertTrue(moveSet.contains("2,1")); // down
        assertTrue(moveSet.contains("1,0")); // left
        assertTrue(moveSet.contains("1,2")); // right
    }

    @Test
    public void testPossibleMoves_allDirectionsBlockedByWalls() {
        int[][] island = {
            {2, 2, 2},
            {3, 0, 3},
            {2, 2, 2}
        };
        int[] location = {1, 1};
        List<int[]> moves = ExplorerSearch.possibleMoves(island, location);
        assertTrue(moves.isEmpty());
    }

    @Test
    public void testPossibleMoves_partialEdge() {
        int[][] island = {
            {0, 1, 1}
        };
        int[] location = {0, 0};
        List<int[]> moves = ExplorerSearch.possibleMoves(island, location);
        Set<String> moveSet = toSet(moves);

        assertEquals(1, moves.size());
        assertTrue(moveSet.contains("0,1")); // only right
    }

    @Test
    public void testPossibleMoves_oneOpen_twoWalls_oneEdge() {
        // Salamander at (1, 1)
        // Up is W, down is wall, left is edge (0), right is open
        int[][] island = {
            {2, 2, 2},
            {1, 0, 1},
            {3, 3, 3}
        };
        int[] location = {1, 1};
        List<int[]> moves = ExplorerSearch.possibleMoves(island, location);
        Set<String> moveSet = toSet(moves);

        assertEquals(2, moves.size());
        assertTrue(moveSet.contains("1,2"));
        assertTrue(moveSet.contains("1,0"));
    }

    @Test
    public void testPossibleMoves_blockedAboveByWall() {
        int[][] island = {
            {1, 3, 1},
            {1, 0, 1},
            {1, 1, 1}
        };
        int[] location = {1, 1};
        List<int[]> moves = ExplorerSearch.possibleMoves(island, location);
        Set<String> moveSet = toSet(moves);

        assertFalse(moveSet.contains("0,1")); // up blocked
        assertTrue(moveSet.contains("2,1"));  // down open
        assertTrue(moveSet.contains("1,0"));  // left open
        assertTrue(moveSet.contains("1,2"));  // right open
    }

    @Test
    public void testPossibleMoves_blockedBelowByWall() {
        int[][] island = {
            {1, 1, 1},
            {1, 0, 1},
            {1, 2, 1}
        };
        int[] location = {1, 1};
        Set<String> moveSet = toSet(ExplorerSearch.possibleMoves(island, location));

        assertTrue(moveSet.contains("0,1"));  // up open
        assertFalse(moveSet.contains("2,1")); // down blocked
        assertTrue(moveSet.contains("1,0"));  // left open
        assertTrue(moveSet.contains("1,2"));  // right open
    }

    @Test
    public void testPossibleMoves_blockedLeftByWall() {
        int[][] island = {
            {1, 1, 1},
            {3, 0, 1},
            {1, 1, 1}
        };
        int[] location = {1, 1};
        Set<String> moveSet = toSet(ExplorerSearch.possibleMoves(island, location));

        assertTrue(moveSet.contains("0,1"));  // up open
        assertTrue(moveSet.contains("2,1"));  // down open
        assertFalse(moveSet.contains("1,0")); // left blocked
        assertTrue(moveSet.contains("1,2"));  // right open
    }

    @Test
    public void testPossibleMoves_blockedRightByWall() {
        int[][] island = {
            {1, 1, 1},
            {1, 0, 2},
            {1, 1, 1}
        };
        int[] location = {1, 1};
        Set<String> moveSet = toSet(ExplorerSearch.possibleMoves(island, location));

        assertTrue(moveSet.contains("0,1"));  // up open
        assertTrue(moveSet.contains("2,1"));  // down open
        assertTrue(moveSet.contains("1,0"));  // left open
        assertFalse(moveSet.contains("1,2")); // right blocked
    }

    @Test
    public void testPossibleMoves_topLeftCornerWithOneOpen() {
        int[][] island = {
            {0, 2},
            {3, 1}
        };
        int[] location = {0, 0};
        List<int[]> moves = ExplorerSearch.possibleMoves(island, location);

        assertTrue(moves.isEmpty()); // surrounded by walls/edges
    }

    @Test
    public void testPossibleMoves_rightEdgeWithOpenLeft() {
        int[][] island = {
            {1, 1, 0}
        };
        int[] location = {0, 2};
        List<int[]> moves = ExplorerSearch.possibleMoves(island, location);
        Set<String> moveSet = toSet(moves);

        assertEquals(1, moves.size());
        assertTrue(moveSet.contains("0,1"));
    }

    private Set<String> toSet(List<int[]> list) {
        Set<String> set = new HashSet<>();
        for (int[] arr : list) {
            set.add(arr[0] + "," + arr[1]);
        }
        return set;
    }
}
