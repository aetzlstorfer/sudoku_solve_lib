package org.mufuku.sudoku.solver.alpha.strategies;

import org.junit.Test;
import org.mufuku.sudoku.solver.alpha.TestConstants;
import org.mufuku.sudoku.solver.alpha.reader.Reader;
import org.mufuku.sudoku.solver.alpha.solver.Cell;
import org.mufuku.sudoku.solver.alpha.solver.Table;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Andreas Etzlstorfer (a.etzlstorfer@gmail.com)
 */
public class IntersectionClaimingSolveStrategyTest {

    private IntersectionClaimingSolveStrategy strategy = new IntersectionClaimingSolveStrategy();

    @Test
    public void test_horizontalBar_success() {
        Table table = Reader.STANDARD_9x9.getTable(TestConstants.EMPTY_TABLE);

        // clear 1st line
        for (int col = 0; col < 9; col++) {
            table.getAt(0, col).getCandidates().clear();
        }
        // clear last sub quadrant on first line
        for (Cell cell : table.getAt(0, 6).getSubNeighbours()) {
            cell.getCandidates().clear();
        }

        // set bar
        table.getAt(0, 7).addCandidates(1, 3);
        table.getAt(0, 8).addCandidates(1, 3);

        // set potential candidates to be removed on last sub quadrant on first line
        table.getAt(1, 6).addCandidates(1, 3, 4);
        table.getAt(1, 7).addCandidates(1, 3, 5, 6);
        table.getAt(2, 8).addCandidates(1, 3, 7, 8);

        boolean result1 = strategy.perform(table);
        assertThat(result1, is(true));
        assertThat(table.getAt(1, 6).getCandidatesAsArray(), equalTo(new int[]{4}));
        assertThat(table.getAt(1, 7).getCandidatesAsArray(), equalTo(new int[]{5, 6}));
        assertThat(table.getAt(2, 8).getCandidatesAsArray(), equalTo(new int[]{7, 8}));
    }

}