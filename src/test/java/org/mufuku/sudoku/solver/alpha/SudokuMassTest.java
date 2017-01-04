package org.mufuku.sudoku.solver.alpha;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mufuku.sudoku.solver.alpha.reader.Reader;
import org.mufuku.sudoku.solver.alpha.reader.SymbolIndex;
import org.mufuku.sudoku.solver.alpha.solver.Table;
import org.mufuku.sudoku.solver.alpha.strategies.ISolveStrategy;
import org.mufuku.sudoku.solver.alpha.utils.StopwatchWrapperSolveStrategy;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Andreas Etzlstorfer (a.etzlstorfer@gmail.com)
 */
public class SudokuMassTest {

    private Reader reader;

    private static int total = 0;

    private static int solved = 0;

    private static Map<Class, Long> stopwatchMap = new HashMap<>();

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        Reader base = Reader.STANDARD_9x9;

        Field symbolIndexField = base.getClass().getDeclaredField("symbolIndex");
        symbolIndexField.setAccessible(true);
        Field solveStrategiesField = base.getClass().getDeclaredField("solveStrategies");
        solveStrategiesField.setAccessible(true);

        SymbolIndex symbolIndex = (SymbolIndex) symbolIndexField.get(base);
        @SuppressWarnings("unchecked")
        List<ISolveStrategy> solveStrategies = (List<ISolveStrategy>) solveStrategiesField.get(base);

        List<ISolveStrategy> wrappedStrategies =
                solveStrategies.stream().map(s -> new StopwatchWrapperSolveStrategy(s, stopwatchMap)).collect(Collectors.toList());

        this.reader = new Reader(9, 9, 3, 3, symbolIndex, wrappedStrategies);
    }

    @Test
    public void test_projectEuler() throws Exception {
        testFile("p096_sudoku.txt", '0');
    }

    @Test
    public void test_qqwing() throws Exception {
        testFile("qqwing_1000_random.txt", '.');
    }

    @Test
    public void test_top1465() throws Exception {
        testFile("top1465.txt", '.');
    }

    @Test
    public void test_andoku() throws Exception {
        testFile("andoku3.txt", '0');
    }

    @AfterClass
    public static void after() {
        System.out.println("total      : " + total);
        System.out.println("solved     : " + solved);
        System.out.println("unresolved : " + (total - solved));
        System.out.println("---");

        stopwatchMap.forEach((clz, runtime) ->
                System.out.println(clz.getSimpleName() + " -> " + runtime / 1000.0 + "ms"));
    }

    private void testFile(String fileName, char noCandidateChar) throws Exception {

        try (Stream<String> lineStream = Files.lines(Paths.get(ClassLoader.getSystemResource(fileName).toURI()))) {
            lineStream.forEach(line -> {
                if (line.startsWith("#")) {
                    return;
                }
                Table table = reader.getTable(line.replace(noCandidateChar, 'x'));
                table.solve();
                assertThat(table.valid(), equalTo(true));
                assertThat(table.candidatesValid(), equalTo(true));
                total++;
                if (table.solved()) {
                    solved++;
                } else {
                    assertThat(table.candidatesValid(), is(true));
//                    System.out.println(table.toString());
//                    System.out.println(table.getCandidateString());
//                    System.out.println("---");
                }
            });
        }
    }
}