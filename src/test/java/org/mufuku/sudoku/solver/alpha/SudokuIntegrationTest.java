package org.mufuku.sudoku.solver.alpha;

import org.junit.Test;
import org.mufuku.sudoku.solver.alpha.reader.IReader;
import org.mufuku.sudoku.solver.alpha.reader.Reader;
import org.mufuku.sudoku.solver.alpha.solver.Cell;
import org.mufuku.sudoku.solver.alpha.solver.Table;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Andreas Etzlstorfer (a.etzlstorfer@gmail.com)
 */
public class SudokuIntegrationTest {

    private static final String[] GOOD_ROWS = new String[]{
            "xx3x2x6xx",
            "9xx3x5xx1",
            "xx18x64xx",
            "xx81x29xx",
            "7xxxxxxx8",
            "xx67x82xx",
            "xx26x95xx",
            "8xx2x3xx9",
            "xx5x1x3xx"
    };

    private static final String[] GOOD_CANDIDATES = {
            "45", "4578", "", "49", "", "147", "", "5789", "57",
            "", "24678", "47", "", "47", "", "78", "278", "",
            "25", "257", "", "", "79", "", "", "23579", "2357",
            "345", "345", "", "", "3456", "", "", "34567", "34567",
            "", "123459", "49", "459", "34569", "4", "1", "13456", "",
            "1345", "13459", "", "", "3459", "", "", "1345", "345",
            "134", "1347", "", "", "478", "", "", "1478", "47",
            "", "1467", "47", "", "457", "", "17", "1467", "",
            "46", "4679", "", "4", "", "47", "", "24678", "2467"
    };

    private static final String[] GOOD_COLS = new String[]{
            Arrays.stream(GOOD_ROWS).map(s -> "" + s.charAt(0)).collect(Collectors.joining()),
            Arrays.stream(GOOD_ROWS).map(s -> "" + s.charAt(1)).collect(Collectors.joining()),
            Arrays.stream(GOOD_ROWS).map(s -> "" + s.charAt(2)).collect(Collectors.joining()),
            Arrays.stream(GOOD_ROWS).map(s -> "" + s.charAt(3)).collect(Collectors.joining()),
            Arrays.stream(GOOD_ROWS).map(s -> "" + s.charAt(4)).collect(Collectors.joining()),
            Arrays.stream(GOOD_ROWS).map(s -> "" + s.charAt(5)).collect(Collectors.joining()),
            Arrays.stream(GOOD_ROWS).map(s -> "" + s.charAt(6)).collect(Collectors.joining()),
            Arrays.stream(GOOD_ROWS).map(s -> "" + s.charAt(7)).collect(Collectors.joining()),
            Arrays.stream(GOOD_ROWS).map(s -> "" + s.charAt(8)).collect(Collectors.joining())
    };

    private static final String[][] SUB_QUADRANTS = new String[][]{{
            GOOD_ROWS[0].substring(0, 3) + GOOD_ROWS[1].substring(0, 3) + GOOD_ROWS[2].substring(0, 3),
            GOOD_ROWS[0].substring(3, 6) + GOOD_ROWS[1].substring(3, 6) + GOOD_ROWS[2].substring(3, 6),
            GOOD_ROWS[0].substring(6, 9) + GOOD_ROWS[1].substring(6, 9) + GOOD_ROWS[2].substring(6, 9),
    }, {
            GOOD_ROWS[3].substring(0, 3) + GOOD_ROWS[4].substring(0, 3) + GOOD_ROWS[5].substring(0, 3),
            GOOD_ROWS[3].substring(3, 6) + GOOD_ROWS[4].substring(3, 6) + GOOD_ROWS[5].substring(3, 6),
            GOOD_ROWS[3].substring(6, 9) + GOOD_ROWS[4].substring(6, 9) + GOOD_ROWS[5].substring(6, 9),
    }, {
            GOOD_ROWS[6].substring(0, 3) + GOOD_ROWS[7].substring(0, 3) + GOOD_ROWS[8].substring(0, 3),
            GOOD_ROWS[6].substring(3, 6) + GOOD_ROWS[7].substring(3, 6) + GOOD_ROWS[8].substring(3, 6),
            GOOD_ROWS[6].substring(6, 9) + GOOD_ROWS[7].substring(6, 9) + GOOD_ROWS[8].substring(6, 9),
    }};


    private static final String GOOD_FULL = Arrays.stream(GOOD_ROWS).collect(Collectors.joining());

    @Test
    public void test_valid_goodTable() {
        IReader reader = Reader.STANDARD_9x9;
        Table table = reader.getTable(GOOD_FULL);

        assertThat(table.valid(), equalTo(true));
        assertThat(table.solved(), equalTo(false));

        StringBuilder check = new StringBuilder();
        table.forEach((row, col, subRow, subCol, cell) -> check.append(cell.toString()));
        assertThat(check.toString(), equalTo(GOOD_FULL));

        assertThat(table.getHeight(), equalTo(9));
        assertThat(table.getWidth(), equalTo(9));
        assertThat(table.getSubHeight(), equalTo(3));
        assertThat(table.getSubWidth(), equalTo(3));

        assertThat(table.getAt(0, 0).getSymbol(), equalTo(-1));

        table.forEach((row, col, subRow, subCol, cell) -> {
            assertThat(Arrays.stream(table.getAt(row, col).getRowNeighbours()).
                            map(Cell::toString).collect(Collectors.joining()),
                    equalTo(GOOD_ROWS[row]));
            assertThat(cell.getRow(), equalTo(row));
            assertThat(cell.getCol(), equalTo(col));
        });

        table.forEach((row, col, subRow, subCol, cell) ->
                assertThat(Arrays.stream(table.getAt(row, col).getColNeighbours()).
                                map(Cell::toString).collect(Collectors.joining()),
                        equalTo(GOOD_COLS[col])));

        table.forEach((row, col, subRow, subCol, cell) ->
                assertThat(Arrays.stream(table.getAt(row, col).getSubNeighbours()).
                                map(Cell::toString).collect(Collectors.joining()),
                        equalTo(SUB_QUADRANTS[subRow][subCol])));

        table.forEach((row, col, subRow, subCol, cell) -> {
            if (cell.getSymbol() >= 0 && GOOD_ROWS[row].charAt(col) != 'x') {
                assertThat(cell.getCandidates().isEmpty(), equalTo(true));
            }
        });

        table.forEach((row, col, subRow, subCol, cell) -> {
            int index = row * 9 + col;
            if (index < GOOD_CANDIDATES.length) {
                assertThat(cell.getCandidateSymbols().stream().collect(Collectors.joining()), equalTo(GOOD_CANDIDATES[index]));
            }
        });
    }

    @Test
    public void test_valid_solve() {
        IReader reader = Reader.STANDARD_9x9;
        Table table = reader.getTable(GOOD_FULL);
        assertThat(table.solved(), equalTo(false));
        table.solve();
        assertThat(table.solved(), equalTo(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_null_exception() {
        Reader reader = Reader.STANDARD_9x9;
        reader.getTable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_empty_exception() {
        Reader reader = Reader.STANDARD_9x9;
        reader.getTable("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_invalidLength_exception() {
        Reader reader = Reader.STANDARD_9x9;
        reader.getTable("1234");
    }

    @Test(expected = IllegalStateException.class)
    public void test_invalidSymbol_exception() {
        Reader reader = Reader.STANDARD_9x9;
        String invalid = GOOD_FULL.replace('1', 'a');
        reader.getTable(invalid);
    }

    @Test
    public void test_duplicateInRow_validFalse() {
        Reader reader = Reader.STANDARD_9x9;
        String invalid = "3" + GOOD_FULL.substring(1);
        Table table = reader.getTable(invalid);
        assertThat(table.valid(), equalTo(false));
    }

    @Test
    public void test_duplicateInCol_validFalse() {
        Reader reader = Reader.STANDARD_9x9;
        String invalid = "9" + GOOD_FULL.substring(1);
        Table table = reader.getTable(invalid);
        assertThat(table.valid(), equalTo(false));
    }

    @Test
    public void test_duplicateInSubQuadrant_validFalse() {
        Reader reader = Reader.STANDARD_9x9;
        String invalid = "1" + GOOD_FULL.substring(1);
        Table table = reader.getTable(invalid);
        assertThat(table.valid(), equalTo(false));
    }
}
