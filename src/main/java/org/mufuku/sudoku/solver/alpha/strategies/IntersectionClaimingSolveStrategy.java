package org.mufuku.sudoku.solver.alpha.strategies;

import org.mufuku.sudoku.solver.alpha.solver.Cell;
import org.mufuku.sudoku.solver.alpha.solver.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andreas Etzlstorfer (a.etzlstorfer@gmail.com)
 */
public class IntersectionClaimingSolveStrategy implements ISolveStrategy {

    @Override
    public boolean perform(Table table) {
        boolean changed = false;
        for (int row = 0; row < table.getHeight(); row++) {
            Cell[] neighbours = table.getAt(row, 0).getRowNeighbours();
            changed = check(neighbours, table.getSymbolIndex().numberOfSymbols()) || changed;
        }
        for (int col = 0; col < table.getWidth(); col++) {
            Cell[] neighbours = table.getAt(0, col).getColNeighbours();
            changed = check(neighbours, table.getSymbolIndex().numberOfSymbols()) || changed;
        }
        return changed;
    }

    private boolean check(Cell[] neighbours, int numberOfSymbols) {
        boolean changed = false;
        @SuppressWarnings("unchecked")
        List<Cell>[] references = new List[numberOfSymbols];
        for (Cell neighbour : neighbours) {
            neighbour.getCandidates().stream().forEach(candidate -> {
                if (references[candidate] == null) {
                    references[candidate] = new ArrayList<>(numberOfSymbols);
                }
                references[candidate].add(neighbour);
            });
        }

        for (int candidate = 0; candidate < numberOfSymbols; candidate++) {
            List<Cell> cells = references[candidate];
            if (cells != null && cells.size() == 2) {
                if (cells.get(0).getSubNeighbours() == cells.get(1).getSubNeighbours()) {
                    changed = clearSubQuadrant(cells.get(0).getSubNeighbours(),
                            cells.get(0), cells.get(1), candidate) || changed;
                }
            }
        }
        return changed;
    }

    private boolean clearSubQuadrant(Cell[] subNeighbours, Cell cell1, Cell cell2, int candidate) {
        boolean changed = false;
        for (Cell subNeighbour : subNeighbours) {
            if (subNeighbour.getCandidates().get(candidate) && subNeighbour != cell1 && subNeighbour != cell2) {
                subNeighbour.clearCandidate(candidate);
                changed = true;
            }
        }
        return changed;
    }
}
