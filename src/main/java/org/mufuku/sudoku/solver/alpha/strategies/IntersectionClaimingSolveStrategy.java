package org.mufuku.sudoku.solver.alpha.strategies;

import org.mufuku.sudoku.solver.alpha.solver.Cell;
import org.mufuku.sudoku.solver.alpha.solver.Table;
import org.mufuku.sudoku.solver.alpha.strategies.ISolveStrategy;

import java.util.ArrayList;
import java.util.BitSet;
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
            changed = check(neighbours) || changed;
        }
        for (int col = 0; col < table.getWidth(); col++) {
            Cell[] neighbours = table.getAt(0, col).getColNeighbours();
            changed = check(neighbours) || changed;
        }
        return changed;
    }

    private boolean check(Cell[] neighbours) {
        List<Cell> cells = new ArrayList<>(2);
        for (Cell neighbour : neighbours) {
            int candidateCount = neighbour.getCandidates().cardinality();
            if (candidateCount > 2) {
                return false;
            } else if (candidateCount == 2) {
                if (cells.size() == 2) {
                    return false;
                }
                cells.add(neighbour);
            }
        }

        boolean changed = false;
        if (cells.size() == 2 && cells.get(0).getSubNeighbours() == cells.get(1).getSubNeighbours()) {
            BitSet candidatesToBeRemoved = cells.get(0).getCandidates();
            for (Cell subNeighbour : cells.get(0).getSubNeighbours()) {
                if (!cells.contains(subNeighbour)) {
                    boolean changedQuadrant = subNeighbour.strikeCandidates(candidatesToBeRemoved);
                    changed = changedQuadrant || changed;
                }
            }
        }
        return changed;
    }
}
