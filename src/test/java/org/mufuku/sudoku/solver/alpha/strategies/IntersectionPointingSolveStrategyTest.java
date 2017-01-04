package org.mufuku.sudoku.solver.alpha.strategies;

import org.junit.Test;
import org.mufuku.sudoku.solver.alpha.TestConstants;
import org.mufuku.sudoku.solver.alpha.reader.Reader;
import org.mufuku.sudoku.solver.alpha.solver.Table;
import org.mufuku.sudoku.solver.alpha.strategies.IntersectionPointingSolveStrategy;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Andreas Etzlstorfer (a.etzlstorfer@gmail.com)
 */
public class IntersectionPointingSolveStrategyTest {

    private final IntersectionPointingSolveStrategy strategy = new IntersectionPointingSolveStrategy();

    @Test
    public void test_verticalBar_eliminateNeighbours() {
        Table table = Reader.STANDARD_9x9.getTable(TestConstants.EMPTY_TABLE);
        // remove everywhere 0
        table.forEach((row, col, subRow, subCol, cell) -> cell.getCandidates().set(0, false));

        // form bar
        table.getAt(3, 3).addCandidates(0);
        table.getAt(4, 3).addCandidates(0);

        // add dependent candidates to other cells
        table.getAt(0, 3).addCandidates(0);
        table.getAt(1, 4).addCandidates(0);
        table.getAt(8, 3).addCandidates(0);
        table.getAt(8, 2).addCandidates(0);

        boolean result1 = strategy.perform(table);
        assertThat(result1, is(true));
        assertThat(table.getAt(0, 3).getCandidates().get(0), is(false));
        assertThat(table.getAt(1, 4).getCandidates().get(0), is(true));
        assertThat(table.getAt(8, 3).getCandidates().get(0), is(false));
        assertThat(table.getAt(8, 2).getCandidates().get(0), is(true));

        boolean result2 = strategy.perform(table);
        assertThat(result2, is(false));
    }

    @Test
    public void test_horizontalBar_eliminateNeighbours() {
        Table table = Reader.STANDARD_9x9.getTable(TestConstants.EMPTY_TABLE);
        // remove everywhere 0
        table.forEach((row, col, subRow, subCol, cell) -> cell.getCandidates().set(2, false));

        // form bar
        table.getAt(1, 7).addCandidates(2);
        table.getAt(1, 8).addCandidates(2);

        // add dependent candidates to other cells
        table.getAt(1, 3).addCandidates(2);
        table.getAt(2, 2).addCandidates(2);

        boolean result1 = strategy.perform(table);
        assertThat(result1, is(true));
        assertThat(table.getAt(1, 3).getCandidates().get(2), is(false));
        assertThat(table.getAt(2, 2).getCandidates().get(2), is(true));

    }
}