package org.mufuku.sudoku.solver.alpha.reader;

import org.mufuku.sudoku.solver.alpha.solver.Table;

/**
 * @author Andreas Etzlstorfer (a.etzlstorfer@gmail.com)
 */
public interface IReader {
    Table getTable(String content);
}
