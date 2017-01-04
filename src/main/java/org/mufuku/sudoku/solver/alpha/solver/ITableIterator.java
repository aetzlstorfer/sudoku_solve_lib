package org.mufuku.sudoku.solver.alpha.solver;

/**
 * @author Andreas Etzlstorfer (a.etzlstorfer@gmail.com)
 */
@FunctionalInterface
public interface ITableIterator {

    void iterate(int row, int col, int subRow, int subCol, Cell cell);
}
