package org.mufuku.sudoku.solver.alpha.solver;

import org.mufuku.sudoku.solver.alpha.reader.SymbolIndex;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Andreas Etzlstorfer (a.etzlstorfer@gmail.com)
 */
public class Cell {

    private final SymbolIndex symbolIndex;

    private final BitSet candidates;

    private final int row;

    private final int col;

    private int symbol;

    private Cell[] rowNeighbours;

    private Cell[] colNeighbours;

    private Cell[] subNeighbours;

    public Cell(int col, int row, SymbolIndex symbolIndex) {
        this.col = col;
        this.row = row;
        this.symbolIndex = symbolIndex;
        this.candidates = new BitSet(symbolIndex.numberOfSymbols());
        this.candidates.set(0, symbolIndex.numberOfSymbols(), true);
    }

    public int getSymbol() {
        return symbol;
    }

    public void setSymbol(int symbol) {
        this.symbol = symbol;
        if (symbol >= 0) {
            this.candidates.set(0, symbolIndex.numberOfSymbols(), false);
        }
    }

    public void refreshCandidates() {
        if (this.symbol >= 0) {
            return;
        }
        Consumer<Cell> f = c -> {
            if (c != this && c.symbol >= 0) {
                this.candidates.set(c.symbol, false);
            }
        };
        Arrays.stream(rowNeighbours).forEach(f);
        Arrays.stream(colNeighbours).forEach(f);
        Arrays.stream(subNeighbours).forEach(f);
    }

    public void refreshNeighbours() {
        Consumer<Cell> f = c -> {
            if (c != this && c.symbol < 0) {
                c.candidates.set(this.symbol, false);
            }
        };
        Arrays.stream(rowNeighbours).forEach(f);
        Arrays.stream(colNeighbours).forEach(f);
        Arrays.stream(subNeighbours).forEach(f);
    }

    public BitSet getCandidates() {
        return candidates;
    }

    public void addCandidates(int... candidates) {
        for (int candidate : candidates) {
            this.candidates.set(candidate, true);
        }
    }

    public int[] getCandidatesAsArray() {
        return candidates.stream().toArray();
    }

    public boolean strikeCandidates(BitSet other) {
        boolean changed = false;
        for (int bitIndex = other.nextSetBit(0); bitIndex >= 0;
             bitIndex = other.nextSetBit(bitIndex + 1)) {
            if (candidates.get(bitIndex)) {
                candidates.set(bitIndex, false);
                changed = true;
            }
        }
        return changed;
    }

    public void clearCandidatesExcept(int... candidates) {
        this.candidates.clear();
        for (int candidate : candidates) {
            this.candidates.set(candidate, true);
        }
    }

    public List<String> getCandidateSymbols() {
        return this.candidates.stream().mapToObj(symbolIndex::getSymbol).collect(Collectors.toList());
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Cell[] getRowNeighbours() {
        return rowNeighbours;
    }

    public void setRowNeighbours(Cell[] rowNeighbours) {
        this.rowNeighbours = rowNeighbours;
    }

    public Cell[] getColNeighbours() {
        return colNeighbours;
    }

    public void setColNeighbours(Cell[] colNeighbours) {
        this.colNeighbours = colNeighbours;
    }

    public Cell[] getSubNeighbours() {
        return subNeighbours;
    }

    public void setSubNeighbours(Cell[] subNeighbours) {
        this.subNeighbours = subNeighbours;
    }

    @Override
    public String toString() {
        return symbolIndex.getSymbol(symbol);
    }

    public String getCandidatesString() {
        return getCandidates().stream().mapToObj(symbolIndex::getSymbol).collect(Collectors.joining(" "));
    }
}
