package org.mufuku.sudoku.solver.alpha.strategies;

import org.junit.Test;
import org.mufuku.sudoku.solver.alpha.TestConstants;
import org.mufuku.sudoku.solver.alpha.reader.Reader;
import org.mufuku.sudoku.solver.alpha.solver.Table;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Andreas Etzlstorfer (a.etzlstorfer@gmail.com)
 */
public class HiddenSingleSolveStrategyTest {

    private final HiddenSingleSolveStrategy strategy = new HiddenSingleSolveStrategy();

    private static final String TEST1 =
            "x257x31xx" +
                    "xxxxxx9x3" +
                    "x7x8xx4xx" +
                    "xxx92x63x" +
                    "2xxxxxxx1" +
                    "x98x17xxx" +
                    "xx2xx8x6x" +
                    "1x9xxxxxx" +
                    "xx65x981x";

    private static final String TEST2 =
            "x257x318x" +
                    "8xxxxx973" +
                    "x7x8xx42x" +
                    "xxx92x638" +
                    "2xxx8xx91" +
                    "x98x172xx" +
                    "xx21x8x69" +
                    "189xxxxx7" +
                    "xx65x9812";

    @Test
    public void test_columnCandidate_resolved() {
        for (int col = 0; col < 9; col++) {
            Table table = Reader.STANDARD_9x9.getTable(TestConstants.EMPTY_TABLE);
            for (int i = 0; i < 9; i++) {
                table.getAt(0, i).getCandidates().set(5, false);
            }
            table.getAt(0, col).getCandidates().clear();
            table.getAt(0, col).getCandidates().set(5);
            boolean result1 = strategy.perform(table);
            assertThat(result1, is(true));
            boolean result2 = strategy.perform(table);
            assertThat(result2, is(false));
        }
    }

    @Test
    public void test_rowCandidate_resolved() {
        for (int row = 0; row < 9; row++) {
            Table table = Reader.STANDARD_9x9.getTable(TestConstants.EMPTY_TABLE);
            for (int i = 0; i < 9; i++) {
                table.getAt(i, 0).getCandidates().set(6, false);
            }
            table.getAt(row, 0).getCandidates().clear();
            table.getAt(row, 0).getCandidates().set(6);
            boolean result1 = strategy.perform(table);
            assertThat(result1, is(true));
            boolean result2 = strategy.perform(table);
            assertThat(result2, is(false));
        }
    }

    @Test
    public void test_subQuadrantCandidate_resolved() {
        Table table = Reader.STANDARD_9x9.getTable(TestConstants.EMPTY_TABLE);
        Arrays.stream(table.getAt(0, 0).getSubNeighbours()).forEach(n ->
                n.getCandidates().set(7, false));
        table.getAt(0, 0).getCandidates().set(7, true);
        boolean result1 = strategy.perform(table);
        assertThat(result1, is(true));
        boolean result2 = strategy.perform(table);
        assertThat(result2, is(false));
    }

    @Test
    public void test_colAndRow_eliminatePositiveSingle() {
        Table table = Reader.STANDARD_9x9.getTable(TEST1);

        // check pre conditions
        assertThat(table.getAt(1, 7).getCandidates().get(6), is(true));
        assertThat(table.getAt(4, 4).getCandidates().get(7), is(true));

        boolean result = strategy.perform(table);

        assertThat(result, is(true));
        assertThat(table.getAt(1, 7).getCandidates().get(6), is(false));
        assertThat(table.getAt(4, 4).getCandidates().get(7), is(false));
    }

    @Test
    public void test_subQuadrant_eliminatePositiveSingle() {
        Table table = Reader.STANDARD_9x9.getTable(TEST2);

        // check pre conditions
        assertThat(table.getAt(2, 8).getCandidates().get(4), is(true));

        boolean result = strategy.perform(table);

        assertThat(result, is(true));
        assertThat(table.getAt(2, 8).getCandidates().get(4), is(false));
    }
}
