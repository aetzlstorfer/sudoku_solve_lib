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
public class NakedPairSolveStrategyTest {

    private NakedPairSolveStrategy strategy = new NakedPairSolveStrategy();

    @Test
    public void test_twoPairsInRow_eliminatePair() {
        Table table = Reader.STANDARD_9x9.getTable(TestConstants.EMPTY_TABLE);
        for (int col = 0; col < 9; col++) {
            table.getAt(0, col).getCandidates().clear();
        }
        table.getAt(0, 1).addCandidates(0, 1);
        table.getAt(0, 2).addCandidates(0, 1);
        table.getAt(0, 8).addCandidates(0, 1, 2);

        boolean result1 = strategy.perform(table);
        assertThat(result1, is(true));
        assertThat(table.getAt(0, 1).getCandidatesAsArray(), equalTo(new int[]{0, 1}));
        assertThat(table.getAt(0, 2).getCandidatesAsArray(), equalTo(new int[]{0, 1}));
        assertThat(table.getAt(0, 8).getCandidatesAsArray(), equalTo(new int[]{2}));
        boolean result2 = strategy.perform(table);
        assertThat(result2, is(false));
    }

    @Test
    public void test_twoPairsInCol_eliminatePair() {
        Table table = Reader.STANDARD_9x9.getTable(TestConstants.EMPTY_TABLE);
        for (int row = 0; row < 9; row++) {
            table.getAt(row, 1).getCandidates().clear();
        }
        table.getAt(1, 1).addCandidates(2, 7);
        table.getAt(5, 1).addCandidates(2, 7);
        table.getAt(6, 1).addCandidates(2, 7, 8, 5);

        boolean result1 = strategy.perform(table);
        assertThat(result1, is(true));
        assertThat(table.getAt(1, 1).getCandidatesAsArray(), equalTo(new int[]{2, 7}));
        assertThat(table.getAt(5, 1).getCandidatesAsArray(), equalTo(new int[]{2, 7}));
        assertThat(table.getAt(6, 1).getCandidatesAsArray(), equalTo(new int[]{5, 8}));
        boolean result2 = strategy.perform(table);
        assertThat(result2, is(false));
    }

    @Test
    public void test_twoPairsInSubQuadrant_eliminatePair() {
        Table table = Reader.STANDARD_9x9.getTable(TestConstants.EMPTY_TABLE);

        Cell[] cells = table.getAt(3, 3).getSubNeighbours();
        for (Cell cell : cells) {
            cell.getCandidates().clear();
        }

        table.getAt(3, 3).addCandidates(3, 8);
        table.getAt(4, 4).addCandidates(3, 8);
        table.getAt(5, 5).addCandidates(3, 8, 1, 2, 4);

        boolean result1 = strategy.perform(table);
        assertThat(result1, is(true));
        assertThat(table.getAt(3, 3).getCandidatesAsArray(), equalTo(new int[]{3, 8}));
        assertThat(table.getAt(4, 4).getCandidatesAsArray(), equalTo(new int[]{3, 8}));
        assertThat(table.getAt(5, 5).getCandidatesAsArray(), equalTo(new int[]{1, 2, 4}));
        boolean result2 = strategy.perform(table);
        assertThat(result2, is(false));
    }
}
