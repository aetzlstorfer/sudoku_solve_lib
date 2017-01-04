package org.mufuku.sudoku.solver.alpha.strategies;

import org.mufuku.sudoku.solver.alpha.solver.Table;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Andreas Etzlstorfer (a.etzlstorfer@gmail.com)
 */
public class NakedSingleSolveStrategy implements ISolveStrategy {
    @Override
    public boolean perform(Table table) {
        AtomicBoolean changed = new AtomicBoolean(false);
        table.forEach((row, col, subRow, subCol, cell) -> {
            if (cell.getCandidates().cardinality() == 1) {
                cell.setSymbol(cell.getCandidates().nextSetBit(0));
                cell.refreshNeighbours();
                changed.set(true);
            }
        });
        return changed.get();
    }
}
