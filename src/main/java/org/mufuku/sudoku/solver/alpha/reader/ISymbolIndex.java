package org.mufuku.sudoku.solver.alpha.reader;

/**
 * @author Andreas Etzlstorfer (a.etzlstorfer@gmail.com)
 */
public interface ISymbolIndex {
    String getSymbol(int symbolIndex);

    int getSymbolIndex(String symbol);

    int numberOfSymbols();
}
