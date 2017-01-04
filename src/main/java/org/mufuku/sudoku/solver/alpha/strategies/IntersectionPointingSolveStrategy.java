package org.mufuku.sudoku.solver.alpha.strategies;

import org.mufuku.sudoku.solver.alpha.solver.Cell;
import org.mufuku.sudoku.solver.alpha.solver.Table;
import org.mufuku.sudoku.solver.alpha.strategies.ISolveStrategy;

/**
 * @author Andreas Etzlstorfer (a.etzlstorfer@gmail.com)
 */
public class IntersectionPointingSolveStrategy implements ISolveStrategy {
    @Override
    public boolean perform(Table table) {
        boolean changed = false;
        for (int subRow = 0; subRow < table.getSubHeight(); subRow++) {
            for (int subCol = 0; subCol < table.getSubWidth(); subCol++) {
                for (int candidate = 0; candidate < table.getSymbolIndex().numberOfSymbols(); candidate++) {
                    int minX = Integer.MAX_VALUE;
                    int minY = Integer.MAX_VALUE;
                    int maxX = Integer.MIN_VALUE;
                    int maxY = Integer.MIN_VALUE;

                    Cell[] subNeighbours = table.getAt(subRow * table.getSubHeight(), subCol * table.getSubWidth()).getSubNeighbours();
                    for (Cell cell : subNeighbours) {
                        if (cell.getCandidates().get(candidate)) {
                            minX = Math.min(minX, cell.getCol());
                            minY = Math.min(minY, cell.getRow());
                            maxX = Math.max(maxX, cell.getCol());
                            maxY = Math.max(maxY, cell.getRow());
                        }
                    }

                    if (maxX - minX == 0) {
                        for (Cell cell : table.getAt(minY, minX).getColNeighbours()) {
                            if (cell.getCandidates().get(candidate) && cell.getSubNeighbours() != subNeighbours) {
                                changed = true;
                                cell.getCandidates().set(candidate, false);
                            }
                        }
                    } else if (maxY - minY == 0) {
                        for (Cell cell : table.getAt(minY, minX).getRowNeighbours()) {
                            if (cell.getCandidates().get(candidate) && cell.getSubNeighbours() != subNeighbours) {
                                changed = true;
                                cell.getCandidates().set(candidate, false);
                            }
                        }
                    }
                }
            }
        }
        return changed;
    }
}
