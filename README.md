Paralleler Sudoku-Löser in Java


Inhaltsverzeichnis

1. Einführung
2. Projektübersicht
3. Verwendete Technologien
4. Projektstruktur
5. Build




1. Einführung
   Dieser Sodokulöser wurde zu Test zwecken und Lern zwecken in Java programmiert, er nutzt Multithreading und verschiedene Lösungsstrategien Gleichzeitig. Das Ziel ist es Lösungen von Sodku Effizient zu finden. 


2. Projektübersicht
   Das Projekt besteht aus mehreren Klassen, die jeweils unterschiedlich versuchen das Sodoku zu lösen
   Die Datei SudokuSolver, koordiniert die ganzen Strategien in Seperaten Threads wodurch man Schneller auf Lösungen kommt

   Solverstrategy: Definiert unterschiedliche Lösungsstrategien
   BacktrackingStrategy: benutzt die Backtracking methode
   ForwardCheckingStrategy: Optimiertes Backtracking mit Forwardprüfung
   Constraint: PropagationStrategy Eweitertes Backtracking mit Constrant Propagation
   SudokuSolver: Main zur Koordination und parralelen Ausführung

3. Verwendete/Benötigte Dependencys: 

  - Java 8 oder höher
  - Mulitthreading
  - Backtracking Algorithmen
  - Clean Code Prinzipien
  - Design Patterns

4. Projektstruktur

sudoku/               Ordner muss noch umbenannt werden bei einem Pull !!! 

├── SudokuSolver.java

├── SolverStrategy.java

├── BacktrackingStrategy.java

├── ForwardCheckingStrategy.java

└── ConstraintPropagationStrategy.java


5. Build

Sudoku in der SudokuSolver.java datei eintragen 



0 = Leerer Wert, das Feld ist so sortiert wie ein Normales Sodokufeld :D 


Kompillieren der Datein

javac sudoku/*.java


Ausführen 
java sudoku.SudokuSolver

Expected Result 

