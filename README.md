# Sudoku Solver Library
A Java library to solve sudokus. Fun project used primarily to solve a Project Euler Puzzle.

## Usage
Given the following sudoku:

|A|B|C|D|E|F|G|H|I|
|-|-|-|-|-|-|-|-|-|
| | |8| | | | | | |
|9| | | | | |1| | |
| |1| | | |7|4| | |
| | | | | | | |2|5|
| | | | |2|8| | | |
|7|3| |1| | | | | |
| | | | |5| | | | |
| | |3| |4|2| |8| |
|5| |4| |1| |7| |6|

When going from left to right and from to to bottom and wrapping the sequence into a String while taking an x for empty cells then the following snippet can solve your sudoku:

```java
String mySudoku = "xx8xxxxxx9xxxxx1xxx1xxx74xxxxxxxxx25xxxx28xxx73x1xxxxxxxxx5xxxxxx3x42x8x5x4x1x7x6";
Reader reader = Reader.STANDARD_9x9;
Table table = reader.getTable(mySudoku);
table.solve();
System.out.println(table.toString());
```

**Output**:
```
[2, 4, 8, 5, 3, 1, 6, 7, 9]
[9, 5, 7, 2, 6, 4, 1, 3, 8]
[3, 1, 6, 9, 8, 7, 4, 5, 2]
[1, 8, 9, 4, 7, 6, 3, 2, 5]
[4, 6, 5, 3, 2, 8, 9, 1, 7]
[7, 3, 2, 1, 9, 5, 8, 6, 4]
[8, 7, 1, 6, 5, 9, 2, 4, 3]
[6, 9, 3, 7, 4, 2, 5, 8, 1]
[5, 2, 4, 8, 1, 3, 7, 9, 6]
```
