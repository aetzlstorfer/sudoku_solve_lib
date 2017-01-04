package org.mufuku.sudoku.solver.alpha.strategies;

import org.mufuku.sudoku.solver.alpha.solver.Cell;
import org.mufuku.sudoku.solver.alpha.solver.Table;

import java.util.*;

/**
 * @author Andreas Etzlstorfer (a.etzlstorfer@gmail.com)
 */
public class HiddenPairSolveStrategy implements ISolveStrategy {
    @Override
    public boolean perform(Table table) {
        boolean changed = false;
        int numberOfSymbols = table.getSymbolIndex().numberOfSymbols();
        for (int row = 0; row < table.getHeight(); row++) {
            Cell[] neighbours = table.getAt(row, 0).getRowNeighbours();
            changed = findSingles(numberOfSymbols, neighbours) || changed;
        }
        for (int col = 0; col < table.getHeight(); col++) {
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
        for (Cell neighbour : neighbours) {
            neighbour.getCandidates().stream().forEach(candidate -> counts[candidate]++);
        }

        Set<Pair> pairs = new HashSet<>();
        for (int i = 0; i < numberOfSymbols; i++) {
            for (int j = 0; j < numberOfSymbols; j++) {
                if (i != j && counts[i] == 2 && counts[j] == 2) {
                    int x = Math.min(i, j);
                    int y = Math.max(i, j);
                    pairs.add(new Pair(x, y));
                }
            }
        }

        Map<Pair, List<Cell>> countMap = new HashMap<>();
        for (Cell neighbour : neighbours) {
            for (Pair pair : pairs) {
                if (neighbour.getCandidates().get(pair.getX()) &&
                        neighbour.getCandidates().get(pair.getY()) &&
                        neighbour.getCandidates().cardinality() > 2
                        ) {
                    List<Cell> countCells = countMap.get(pair);
                    if (countCells == null) {
                        countCells = new ArrayList<>(5);
                        countMap.put(pair, countCells);
                    }
                    countCells.add(neighbour);
                }
            }
        }

        for (Map.Entry<Pair, List<Cell>> entry : countMap.entrySet()) {
            if (entry.getValue().size() == 2) {
                for (Cell cell : entry.getValue()) {
                    cell.clearCandidatesExcept(entry.getKey().getX(), entry.getKey().getY());
                    changed = true;
                }
            }
        }

        return changed;
    }
}
