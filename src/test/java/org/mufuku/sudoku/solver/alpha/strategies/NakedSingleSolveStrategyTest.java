package org.mufuku.sudoku.solver.alpha.strategies;

import org.junit.Test;
import org.mufuku.sudoku.solver.alpha.TestConstants;
import org.mufuku.sudoku.solver.alpha.reader.Reader;
import org.mufuku.sudoku.solver.alpha.solver.Table;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Andreas Etzlstorfer (a.etzlstorfer@gmail.com)
 */
public class NakedSingleSolveStrategyTest {

    private NakedSingleSolveStrategy strategy = new NakedSingleSolveStrategy();

    private static final String TEST1 =
            "12345678x" +
                    "xxxxxxxxx" +
                    "xxxxxxxxx" +
                    "xxxxxxxxx" +
                    "xxxxxxxxx" +
                    "xxxxxxxxx" +
                    "xxxxxxxxx" +
                    "xxxxxxxxx" +
                    "xxxxxxxxx";

    @Test
    public void test_singlePresent_symbolSet() {
        Table table = Reader.STANDARD_9x9.getTable(TEST1);
        boolean result = strategy.perform(table);
        assertThat(result, is(true));
        assertThat(table.getAt(0, 8).getCandidates().isEmpty(), is(true));
        assertThat(table.getAt(0, 8).getSymbol(), equalTo(8));
    }

    @Test
    public void test_singleNotPresent_nothing() {
        Table table = Reader.STANDARD_9x9.getTable(TestConstants.EMPTY_TABLE);
        boolean result = strategy.perform(table);
        assertThat(result, is(false));
    }
}
