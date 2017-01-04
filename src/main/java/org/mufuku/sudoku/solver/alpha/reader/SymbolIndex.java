package org.mufuku.sudoku.solver.alpha.reader;

import java.util.List;

/**
 * @author Andreas Etzlstorfer (a.etzlstorfer@gmail.com)
 */
public class SymbolIndex implements ISymbolIndex {

    private final List<String> symbols;

    private final String noSymbol;

    public SymbolIndex(List<String> symbols, String noSymbol) {
        this.symbols = symbols;
        this.noSymbol = noSymbol;
    }

    @Override
    public String getSymbol(int symbolIndex) {
        if (symbolIndex < 0) {
            return noSymbol;
        } else {
            return symbols.get(symbolIndex);
        }
    }

    @Override
    public int getSymbolIndex(String symbol) {
        if (symbol.equals(noSymbol)) {
            return -1;
        } else {
            int symbolIndex = symbols.indexOf(symbol);
            if (symbolIndex < 0) {
                throw new IllegalStateException("Invalid symbol: " + symbol);
            }
            return symbolIndex;
        }
    }

    @Override
    public int numberOfSymbols() {
        return symbols.size();
    }
}
