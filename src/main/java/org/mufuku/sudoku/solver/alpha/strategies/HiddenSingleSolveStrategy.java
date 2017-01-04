package org.mufuku.sudoku.solver.alpha.strategies;

import org.mufuku.sudoku.solver.alpha.solver.Cell;
import org.mufuku.sudoku.solver.alpha.solver.Table;

/**
 * @author Andreas Etzlstorfer (a.etzlstorfer@gmail.com)
 *         <p>
 *         Hidden single
 */
public class HiddenSingleSolveStrategy implements ISolveStrategy {
    @Override
    public boolean perform(Table table) {
        boolean changed = false;
        int numberOfSymbols = table.getSymbolIndex().numberOfSymbols();
        for (int row = 0; row < table.getHeight(); row++) {
            Cell[] neighbours = table.getAt(row, 0).getRowNeighbours();
            changed = findSingles(numberOfSymbols, neighbours) || changed;
        }
        for (int col = 0; col < table.getWidth(); col++) {
            Cell[] neighbours = table.getAt(0, col).getColNeighbours();
            changed = findSingles(numberOfSymbols, neighbours) || changed;
        }
        for (int subRow = 0; subRow < table.getSubHeight(); subRow++) {
            for (int subCol = 0; subCol < table.getSubWidth(); subCol++) {
                Cell[] neighbours = table.getAt(subRow * table.getSubHeight(),
                        subCol * table.getSubWidth()).getSubNeighbours();
                changed = findSingles(numberOfSymbols, neighbours) || changed;
            }
        }
        return changed;
    }

    private boolean findSingles(int numberOfSymbols, Cell[] neighbours) {
        boolean changed = false;
        int[] counts = new int[numberOfSymbols];
        Cell[] lastIndex = new Cell[numberOfSymbols];
        for (Cell neighbour : neighbours) {
            neighbour.getCandidates().stream().forEach(candidate -> {
                counts[candidate]++;
                lastIndex[candidate] = neighbour;
            });
        }
        for (int candidate = 0; candidate < counts.length; candidate++) {
            int count = counts[candidate];
            if (count == 1) {
                lastIndex[candidate].setSymbol(candidate);
                lastIndex[candidate].refreshNeighbours();
                changed = true;
            }
        }
        return changed;
    }
}
