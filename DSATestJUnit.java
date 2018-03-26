import static org.junit.Assert.*;

import org.junit.Test;

public class DSATestJUnit {

	// Initializing maps
	private int size = 20;

	public String[][] getLargeMap() {
		String[][] ret = new String[size + 5][size + 5];
		for (int i = 0; i < size + 5; i++) {
			for (int j = 0; j < size + 5; j++) {
				ret[i][j] = "---";
			}
		}
		return ret;
	}

	public String[][] getSmallMap() {
		String[][] ret = new String[size - 10][size - 10];
		for (int i = 0; i < size - 10; i++) {
			for (int j = 0; j < size - 10; j++) {
				ret[i][j] = "---";
			}
		}
		return ret;
	}

	public String[][] getBlockedMap() {
		String[][] ret = new String[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (i == j || i == j + 1 || i == j - 1) {
					ret[i][j] = "-x-";
				} else {
					ret[i][j] = "---";
				}
			}
		}
		return ret;
	}

	public String[][] getOpenMap() {
		String[][] ret = new String[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				ret[i][j] = "---";
			}
		}
		return ret;
	}

	public String[][] getRectMap() {
		String[][] ret = new String[size][size - 1];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size - 1; j++) {
				ret[i][j] = "---";
			}
		}
		return ret;
	}

	@Test
	public void test_out_of_bounds_negative_value_for_destination() {
		DSA dsa = new DSA(size);
		int node;
		boolean value = true;
		try {
			node = dsa.findFirstMove(getOpenMap(), 0, 0, -1, -1);
		} catch (IndexOutOfBoundsException e1) {
			value = false;
		}
		assertEquals("Destination has negative value", value, true);
	}

	@Test
	public void test_out_of_bounds_negative_value_for_beginning() {
		DSA dsa = new DSA(size);
		int node;
		boolean value = true;
		node = dsa.findFirstMove(getOpenMap(), -1, -1, 0, 0);
		assertEquals("Starting Point has negative value", value, false);
	}

	@Test
	public void test_out_of_bounds_max_value_for_beginning() {
		DSA dsa = new DSA(size);
		int node;
		boolean value = true;
		node = dsa.findFirstMove(getOpenMap(), size, size, 0, 0);
		value = false;
		assertEquals("Out of bounds value for beginning", value, true);
	}

	@Test
	public void test_out_of_bounds_max_value_for_destination() {
		DSA dsa = new DSA(size);
		boolean value = true;
		int node;
		try {
			node = dsa.findFirstMove(getOpenMap(), 0, 0, size, size);
		} catch (IndexOutOfBoundsException e1) {
			value = false;
		}
		assertEquals("Out of bounds value for destination", value, false);
	}

	@Test
	public void test_no_path() {
		DSA dsa = new DSA(size);
		int node = dsa.findFirstMove(getBlockedMap(), size - 1, 0, 0, size - 1);
		assertEquals("No valid path exists", node, size-1);
	}

	@Test
	public void test_right_move() {
		DSA dsa = new DSA(size);
		assertEquals("The next move should've been to the right", dsa.findFirstMove(getOpenMap(), size - 3, size - 5, size - 1, size - 5),
				(size - 2) + ((size - 5) * size));
	}

	@Test
	public void test_down_move() {
		DSA dsa = new DSA(size);
		assertEquals("The next move should've been to the botton",dsa.findFirstMove(getOpenMap(), size - 3, size - 5, size - 3, size - 3),
				(size - 3) + ((size - 4) * size));
	}

	@Test
	public void test_left_move() {
		DSA dsa = new DSA(size);
		assertEquals("The next move should've been to the left",dsa.findFirstMove(getOpenMap(), size - 3, size - 5, size - 5, size - 5),
				(size - 4) + ((size - 5) * size));
	}

	@Test
	public void test_up_move() {
		DSA dsa = new DSA(size);
		assertEquals("The next move should've been to the top", dsa.findFirstMove(getOpenMap(), size - 3, size - 5, size - 3, size - 7),
				(size - 3) + ((size - 6) * size));
	}

	@Test
	public void test_top_right_diagonal_move() {
		DSA dsa = new DSA(size);
		assertEquals("The next move should've been diagonally to the top right",dsa.findFirstMove(getOpenMap(), size - 3, size - 5, size - 1, size - 7),
				(size - 2) + ((size - 6) * size));
	}

	@Test
	public void test_top_left_diagonal_move() {
		DSA dsa = new DSA(size);
		assertEquals("The next move should've been diagonally to the top left",dsa.findFirstMove(getOpenMap(), size - 3, size - 5, size - 5, size - 7),
				(size - 4) + ((size - 6) * size));
	}

	@Test
	public void test_bottom_right_diagonal_move() {
		DSA dsa = new DSA(size);
		assertEquals("The next move should've been diagonally to the bottom right",dsa.findFirstMove(getOpenMap(), size - 3, size - 5, size - 1, size - 3),
				(size - 2) + ((size - 4) * size));
	}

	@Test
	public void test_bottom_left_diagonal_move() {
		DSA dsa = new DSA(size);
		assertEquals("The next move should've been diagonally to the bottom left", dsa.findFirstMove(getOpenMap(), size - 3, size - 5, size - 5, size - 3),
				(size - 4) + ((size - 4) * size));
	}

	@Test
	public void test_small_map() {
		DSA dsa = new DSA(size);
		assertEquals("",dsa.findFirstMove(getSmallMap(),0,0,1,1),0);
	}

	@Test
	public void test_large_map() {
		DSA dsa = new DSA(size);
		assertEquals("",dsa.findFirstMove(getLargeMap(),0,0,size,size), 0);
	}

	@Test
	public void test_rectangular_map() {
		DSA dsa = new DSA(size);
		assertEquals("",dsa.findFirstMove(getRectMap(),0,0,1,1), 0);

	}

}
