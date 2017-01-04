package org.mufuku.sudoku.solver.alpha.reader;

import org.apache.commons.lang3.StringUtils;
import org.mufuku.sudoku.solver.alpha.solver.Table;
import org.mufuku.sudoku.solver.alpha.strategies.*;
import org.mufuku.sudoku.solver.alpha.strategies.IntersectionClaimingSolveStrategy;
import org.mufuku.sudoku.solver.alpha.strategies.IntersectionPointingSolveStrategy;

import java.util.Arrays;
import java.util.List;

/**
 * @author Andreas Etzlstorfer (a.etzlstorfer@gmail.com)
 */
public class Reader implements IReader {

    public static final Reader STANDARD_9x9 = new Reader(9, 9, 3, 3,
            new SymbolIndex(
                    Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9"), "x"),
            Arrays.asList(
                    new NakedSingleSolveStrategy(),
                    new HiddenSingleSolveStrategy(),
                    new IntersectionPointingSolveStrategy(),
                    new NakedPairSolveStrategy(),
                    new IntersectionClaimingSolveStrategy(),
                    new HiddenPairSolveStrategy()
            ));

    private final int height;

    private final int width;

    private final int subHeight;

    private final int subWidth;

    private final SymbolIndex symbolIndex;

    private final List<ISolveStrategy> solveStrategies;

    public Reader(int height, int width, int subHeight, int subWidth, SymbolIndex symbolIndex, List<ISolveStrategy> solveStrategies) {
        this.height = height;
        this.width = width;
        this.subHeight = subHeight;
        this.subWidth = subWidth;
        this.symbolIndex = symbolIndex;
        this.solveStrategies = solveStrategies;
    }

    @Override
    public Table getTable(String content) {
        if (StringUtils.isEmpty(content)) {
            throw new IllegalArgumentException("empty matrix string not allowed");
        }
        if (content.length() != width * height) {
            throw new IllegalArgumentException("String must be " + width * height + " long to fulfil "
                    + width + " x " + height + " matrix");
        }
        Table table = new Table(height, width, subHeight, subWidth, symbolIndex, solveStrategies);
        table.forEach((row, col, subRow, subCol, cell) -> {
            int symbolIndex = this.symbolIndex.getSymbolIndex("" + content.charAt(row * height + col));
            cell.setSymbol(symbolIndex);
        });
        table.initializeFieldDependencies();
        table.calculateCandidates();
        return table;
    }
}
