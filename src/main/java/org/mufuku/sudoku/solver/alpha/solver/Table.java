package org.mufuku.sudoku.solver.alpha.solver;

import org.mufuku.sudoku.solver.alpha.reader.SymbolIndex;
import org.mufuku.sudoku.solver.alpha.strategies.ISolveStrategy;

import java.util.*;

/**
 * @author Andreas Etzlstorfer (a.etzlstorfer@gmail.com)
 */
public class Table {

    private final Cell[][] cells;

    private final int height;

    private final int width;

    private final int subHeight;

    private final int subWidth;

    private final SymbolIndex symbolIndex;

    private final List<ISolveStrategy> solveStrategies;

    public Table(int height, int width, int subHeight, int subWidth, SymbolIndex symbolIndex, List<ISolveStrategy> solveStrategies) {
        this.height = height;
        this.width = width;
        this.subHeight = subHeight;
        this.subWidth = subWidth;
        this.solveStrategies = solveStrategies;
        this.symbolIndex = symbolIndex;
        this.cells = new Cell[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                this.cells[row][col] = new Cell(col, row, symbolIndex);
            }
        }
    }

    public void forEach(ITableIterator iterator) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int subCol = col / subWidth;
                int subRow = row / subHeight;
                iterator.iterate(row, col, subRow, subCol, getAt(row, col));
            }
        }
    }

    public Cell getAt(int row, int col) {
        return cells[row][col];
    }

    @SuppressWarnings("unchecked")
    public void initializeFieldDependencies() {
        // link row neighbours
        this.forEach((row, col, subRow, subCol, cell) -> cell.setRowNeighbours(cells[row]));

        // link col neighbours
        Cell[][] transposed = new Cell[this.width][this.height];
        this.forEach((row, col, subRow, subCol, cell) -> transposed[col][row] = getAt(row, col));
        this.forEach((row, col, subRow, subCol, cell) -> cell.setColNeighbours(transposed[col]));

        // link sub quadrant neighbours
        List[][] subQuadrants = new List[this.height / this.subHeight][this.width / this.subWidth];
        this.forEach((row, col, subRow, subCol, cell) -> {
            if (subQuadrants[subRow][subCol] == null) {
                subQuadrants[subRow][subCol] = new ArrayList<Cell>();
            }
            subQuadrants[subRow][subCol].add(cell);
        });
        for (int subRow = 0; subRow < this.subHeight; subRow++) {
            for (int subCol = 0; subCol < this.subWidth; subCol++) {
                Cell[] cells = (Cell[]) subQuadrants[subRow][subCol].
                        toArray(new Cell[subQuadrants[subRow][subCol].size()]);
                for (Cell cell : cells) {
                    getAt(cell.getRow(), cell.getCol()).setSubNeighbours(cells);
                }
            }
        }
    }

    public void calculateCandidates() {
        this.forEach((row, col, subRow, subCol, cell) -> cell.refreshCandidates());
    }

    public void solve() {
        boolean progress;
        do {
            progress = false;
            for (ISolveStrategy strategy : solveStrategies) {
                progress = progress || strategy.perform(this);
            }
        } while (progress);
    }

    public boolean valid() {
        for (int row = 0; row < height; row++) {
            Set<Integer> figures = new HashSet<>();
            for (Cell cell : getAt(row, 0).getRowNeighbours()) {
                int symbol = cell.getSymbol();
                if (symbol >= 0 && !figures.add(symbol)) {
                    return false;
                }
            }
        }
        for (int col = 0; col < width; col++) {
            Set<Integer> figures = new HashSet<>();
            for (Cell cell : getAt(col, 0).getColNeighbours()) {
                int symbol = cell.getSymbol();
                if (symbol >= 0 && !figures.add(symbol)) {
                    return false;
                }
            }
        }
        for (int subRow = 0; subRow < subHeight; subRow++) {
            for (int subCol = 0; subCol < subWidth; subCol++) {
                Set<Integer> figures = new HashSet<>();
                for (Cell cell : getAt(subRow, subCol).getSubNeighbours()) {
                    int symbol = cell.getSymbol();
                    if (symbol >= 0 && !figures.add(symbol)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public boolean candidatesValid() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Cell cell = getAt(row, col);
                if (cell.getSymbol() < 0 && cell.getCandidates().isEmpty()) {
                    return false;
                } else if (cell.getSymbol() >= 0 && !cell.getCandidates().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean solved() {
        try {
            forEach((row, col, subRow, subCol, cell) -> {
                if (cell.getSymbol() < 0) {
                    throw new IllegalStateException("unset cell");
                }
            });
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getSubHeight() {
        return subHeight;
    }

    public int getSubWidth() {
        return subWidth;
    }

    public SymbolIndex getSymbolIndex() {
        return symbolIndex;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String separator = System.lineSeparator();
        for (Cell[] cell : cells) {
            sb.append(Arrays.toString(cell)).append(separator);
        }
        return sb.toString();
    }

    public String getCandidateString() {
        StringBuilder sb = new StringBuilder();
        int[] maxLengths = new int[width];
        Arrays.fill(maxLengths, Integer.MIN_VALUE);
        forEach((row, col, subRow, subCol, cell) -> {
            String cellValue = cell.getCandidatesString();
            if (cellValue.length() > maxLengths[col]) {
                maxLengths[col] = cellValue.length();
            }
        });
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Cell cell = getAt(row, col);
                String cellValue = cell.getCandidatesString();
                int spaceFill = maxLengths[col] - cellValue.length();
                for (int i = 0; i < spaceFill; i++) {
                    sb.append(" ");
                }
                sb.append(cellValue);
                if (col + 1 < width) {
                    sb.append("|");
                    if ((col + 1) % subWidth == 0) {
                        sb.append("|");
                    }
                }
            }
            sb.append(System.lineSeparator());
            if (row + 1 < height && (row + 1) % subHeight == 0) {
                for (int col = 0; col < width; col++) {
                    for (int i = 0; i < maxLengths[col]; i++) {
                        sb.append("-");
                    }
                    if (col + 1 < width) {
                        sb.append("+");
                        if ((col + 1) % subWidth == 0) {
                            sb.append("+");
                        }
                    }
                }
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }
}
