package org.mufuku.sudoku.solver.alpha.solver;

import org.junit.Test;
import org.mufuku.sudoku.solver.alpha.TestConstants;
import org.mufuku.sudoku.solver.alpha.reader.Reader;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Andreas Etzlstorfer (a.etzlstorfer@gmail.com)
 */
public class TableTest {

    private static final String EXPECTED_TO_STRING =
            "[1, x, x, x, x, x, x, x, 9]" + System.lineSeparator() +
                    "[x, 2, x, x, x, x, x, 8, x]" + System.lineSeparator() +
                    "[x, x, 3, x, x, x, 7, x, x]" + System.lineSeparator() +
                    "[x, x, x, 4, x, 6, x, x, x]" + System.lineSeparator() +
                    "[x, x, x, x, 5, x, x, x, x]" + System.lineSeparator() +
                    "[x, x, x, 4, x, 6, x, x, x]" + System.lineSeparator() +
                    "[x, x, 3, x, x, x, 7, x, x]" + System.lineSeparator() +
                    "[x, 2, x, x, x, x, x, 8, x]" + System.lineSeparator() +
                    "[1, x, x, x, x, x, x, x, 9]" + System.lineSeparator();

    private static final String CANDIDATES_TEST =
            "xxxxxxx8x" +
                    "8xx7x1x4x" +
                    "x4xx2xx3x" +
                    "374xxx9xx" +
                    "xxxx3xxxx" +
                    "xx5xxx321" +
                    "x1xx6xx5x" +
                    "x5x8x2xx6" +
                    "x8xxxxxxx";

    public static final String CANDIDATES_EXPECTED =
            "1 2 5 6 7 9|2 3 6 9|1 2 3 6 7 9||  3 4 5 6 9|    4 5 9|  3 4 5 6 9||1 2 5 6 7|     |    2 5 7 9" + System.lineSeparator() +
                    "           |2 3 6 9|    2 3 6 9||           |      5 9|           ||    2 5 6|     |      2 5 9" + System.lineSeparator() +
                    "  1 5 6 7 9|       |    1 6 7 9||      5 6 9|         |    5 6 8 9||  1 5 6 7|     |      5 7 9" + System.lineSeparator() +
                    "-----------+-------+-----------++-----------+---------+-----------++---------+-----+-----------" + System.lineSeparator() +
                    "           |       |           ||    1 2 5 6|    1 5 8|      5 6 8||         |    6|        5 8" + System.lineSeparator() +
                    "    1 2 6 9|  2 6 9|  1 2 6 8 9||1 2 4 5 6 9|         |4 5 6 7 8 9||4 5 6 7 8|  6 7|    4 5 7 8" + System.lineSeparator() +
                    "        6 9|    6 9|           ||      4 6 9|  4 7 8 9|  4 6 7 8 9||         |     |           " + System.lineSeparator() +
                    "-----------+-------+-----------++-----------+---------+-----------++---------+-----+-----------" + System.lineSeparator() +
                    "    2 4 7 9|       |    2 3 7 9||      3 4 9|         |    3 4 7 9||  2 4 7 8|     |2 3 4 7 8 9" + System.lineSeparator() +
                    "      4 7 9|       |      3 7 9||           |  1 4 7 9|           ||    1 4 7|1 7 9|           " + System.lineSeparator() +
                    "  2 4 6 7 9|       |  2 3 6 7 9||  1 3 4 5 9|1 4 5 7 9|  3 4 5 7 9||  1 2 4 7|1 7 9|  2 3 4 7 9" + System.lineSeparator();

    @Test
    public void test_toString() {
        Table table = Reader.STANDARD_9x9.getTable(TestConstants.EMPTY_TABLE);
        for (int i = 0; i < 9; i++) {
            table.getAt(i, i).setSymbol(i);
            table.getAt(i, table.getWidth() - i - 1).setSymbol(9 - i - 1);
        }
        assertThat(table.toString(), equalTo(EXPECTED_TO_STRING));
    }

    @Test
    public void test_candidatesString() {
        Table table = Reader.STANDARD_9x9.getTable(CANDIDATES_TEST);
        assertThat(table.getCandidateString(), equalTo(CANDIDATES_EXPECTED));
    }

    @Test
    public void test_validTable_true() {
        Table table1 = Reader.STANDARD_9x9.getTable(TestConstants.EMPTY_TABLE);
        assertThat(table1.candidatesValid(), is(true));

        Table table2 = Reader.STANDARD_9x9.getTable(CANDIDATES_TEST);
        assertThat(table2.candidatesValid(), is(true));
    }

    @Test
    public void test_invalidTable_false() {
        Table table1 = Reader.STANDARD_9x9.getTable(TestConstants.EMPTY_TABLE);
        table1.getAt(0, 0).setSymbol(1);
        table1.getAt(0, 0).getCandidates().set(0, true);
        assertThat(table1.candidatesValid(), is(false));

        Table table2 = Reader.STANDARD_9x9.getTable(TestConstants.EMPTY_TABLE);
        table2.getAt(0, 0).getCandidates().clear();
        assertThat(table2.candidatesValid(), is(false));
    }
}
