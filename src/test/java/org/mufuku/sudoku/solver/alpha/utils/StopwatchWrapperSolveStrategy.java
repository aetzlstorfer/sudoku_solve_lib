package org.mufuku.sudoku.solver.alpha.utils;

import org.mufuku.sudoku.solver.alpha.solver.Table;
import org.mufuku.sudoku.solver.alpha.strategies.ISolveStrategy;

import java.util.Map;

/**
 * @author Andreas Etzlstorfer (a.etzlstorfer@gmail.com)
 */
public class StopwatchWrapperSolveStrategy implements ISolveStrategy {

    private final ISolveStrategy strategy;

    private final Map<Class, Long> stopwatchMap;

    public StopwatchWrapperSolveStrategy(ISolveStrategy strategy, Map<Class, Long> stopwatchMap) {
        this.strategy = strategy;
        this.stopwatchMap = stopwatchMap;
    }

    @Override
    public boolean perform(Table table) {
        long start = System.nanoTime();
        boolean result = strategy.perform(table);
        long end = System.nanoTime();
        stopwatchMap.put(strategy.getClass(), end - start);
        return result;
    }
}
