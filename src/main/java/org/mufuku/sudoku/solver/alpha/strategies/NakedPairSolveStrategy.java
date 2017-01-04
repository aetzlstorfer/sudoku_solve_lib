package org.mufuku.sudoku.solver.alpha.strategies;

import org.mufuku.sudoku.solver.alpha.solver.Cell;
import org.mufuku.sudoku.solver.alpha.solver.Table;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andreas Etzlstorfer (a.etzlstorfer@gmail.com)
 */
public class NakedPairSolveStrategy implements ISolveStrategy {
    @Override
    public boolean perform(Table table) {
        boolean changed = false;
        for (int row = 0; row < table.getHeight(); row++) {
            Cell[] neighbours = table.getAt(row, 0).getRowNeighbours();
            changed = checkNeighbours(neighbours) || changed;
        }
        for (int col = 0; col < table.getWidth(); col++) {
            Cell[] neighbours = table.getAt(0, col).getColNeighbours();
            changed = checkNeighbours(neighbours) || changed;
        }
        for (int subRow = 0; subRow < table.getSubHeight(); subRow++) {
            for (int subCol = 0; subCol < table.getSubWidth(); subCol++) {
                Cell[] neighbours = table.getAt(subRow * table.getSubHeight(), subCol * table.getSubWidth()).getSubNeighbours();
                changed = checkNeighbours(neighbours) || changed;
            }
        }
        return changed;
    }

    private boolean checkNeighbours(Cell[] neighbours) {
        Map<BitSet, Integer> pairs = new HashMap<>();
        boolean changed = false;
        for (Cell cell : neighbours) {
            if (cell.getCandidates().cardinality() == 2) {
                Integer count = pairs.getOrDefault(cell.getCandidates(), 0);
                pairs.put(cell.getCandidates(), ++count);
                if (count == 2) {
                    changed = changed || clearPairs(neighbours, cell.getCandidates());
                }
            }
        }
        return changed;
    }

    private boolean clearPairs(Cell[] neighbours, BitSet candidates) {
        boolean changed = false;
        for (Cell neighbour : neighbours) {
            if (neighbour.getCandidates().cardinality() > 2) {
                changed = neighbour.strikeCandidates(candidates) || changed;
            }
        }
        return changed;
    }
}