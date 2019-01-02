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

    private final Map<Class, Long> hitMap;

    public StopwatchWrapperSolveStrategy(ISolveStrategy strategy, Map<Class, Long> stopwatchMap, Map<Class, Long> hitMap) {
        this.strategy = strategy;
        this.stopwatchMap = stopwatchMap;
        this.hitMap = hitMap;
    }

    @Override
    public boolean perform(Table table) {
        long start = System.currentTimeMillis();
        boolean result = strategy.perform(table);
        long end = System.currentTimeMillis();

        Class<? extends ISolveStrategy> strategyClass = strategy.getClass();

        Long timeSoFar = stopwatchMap.getOrDefault(strategyClass, 0L);
        timeSoFar += end - start;
        stopwatchMap.put(strategyClass, timeSoFar);

        if (result) {
            Long hits = hitMap.getOrDefault(strategyClass, 0L);
            hitMap.put(strategyClass, ++hits);
        }

        return result;
    }
}
