package Board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Stack;

public class Board {

    public static final int MATRIX_SIZE = 9;

    private Stack<Transition> myHistory = new Stack<>();
    private HashSet<Transition> myAllowedTransitions = new HashSet<>();
    private int[][] myMatrix;

    private static final HashSet<Coordinate> SPECIAL_COORDINATES = new HashSet<>(Arrays.asList(
            new Coordinate(0, 0),
            new Coordinate(0, 8),
            new Coordinate(8, 0),
            new Coordinate(8, 8)
    ));
    
    private static int[][] CONSTRUCT_START_MATRIX() {
        int[][] aMatrixBuff = new int[Board.MATRIX_SIZE][Board.MATRIX_SIZE];
        aMatrixBuff[0][0] = 0;
        return aMatrixBuff;

    }

    public enum Piece {
        OUT(-1),
        EMPTY(0),
        KING(1),
        DEFENDER(2),
        ATTACKER(3);

        public final int myValue;

        private Piece(final int theValue) {
            this.myValue = theValue;
        }

    }

    public static class Coordinate {
        public Coordinate() {}
        public Coordinate(int theRow, int theCol) {myRow = theRow; myCol = theCol;}
        int myRow = 0;
        int myCol = 0;
    }

    public static class Entry {
        public Entry(){ }
        public Coordinate myCoord;
        public Piece myValue = Piece.OUT;

        public boolean isEmpty(){
            return myValue == Piece.OUT;
        }
    }

    public static class Transition {
        Coordinate myStartCoord;
        Coordinate myDestCoord;
        int myDestValue = 0;
        Entry myRemovedEntry;

        public void Reverse() {
            Coordinate aCoordBuff = myStartCoord;
            myStartCoord = myDestCoord;
            myDestCoord = aCoordBuff;
        }
    }

    public HashSet<Transition> getAllowedTransitions(){
        return myAllowedTransitions;
    }

    public boolean applyTransition(Transition theTransition) {
        constructAllowedMoves();
        if (!myAllowedTransitions.contains(theTransition)) {
            return false;
        }
        int value = myMatrix[theTransition.myStartCoord.myRow][theTransition.myStartCoord.myCol];
        myMatrix[theTransition.myDestCoord.myRow][theTransition.myDestCoord.myCol] = value;
        myMatrix[theTransition.myStartCoord.myRow][theTransition.myStartCoord.myCol] = 0;
        return true;
    }

    public boolean undoMove(){
        if (myHistory.isEmpty()){
            return false;
        }
        Transition aTransition = myHistory.pop();
        aTransition.Reverse();
        applyTransition(aTransition);
        if (aTransition.myRemovedEntry.isEmpty()) {
            return true;
        }
        myMatrix[aTransition.myRemovedEntry.myCoord.myRow][aTransition.myRemovedEntry.myCoord.myCol] = aTransition.myRemovedEntry.myValue.myValue;
        return true;
    }

    public void undoMove(int number){
        for (int aIdx = 0; aIdx < number; ++aIdx) {
            undoMove();
        }
    }

    private boolean removeNearbyEntries(final Coordinate theCoord) {
        int aCounter = 0;

        int value = myMatrix[theCoord.myRow + 1][theCoord.myCol];
        aCounter += value;

        value = myMatrix[theCoord.myRow - 1][theCoord.myCol];
        aCounter += value;

        if (aCounter % 2 == 0) {
            return true;
        }

        value = myMatrix[theCoord.myRow][theCoord.myCol + 1];
        aCounter += value;

        value = myMatrix[theCoord.myRow][theCoord.myCol - 1];
        aCounter += value;

        if (aCounter % 2 == 0) {
            return true;
        }

        return false;
    }

    private void constructAllowedMoves() {
        Coordinate aCurrentCoord = new Coordinate();

        for (int aRow = 0; aRow < MATRIX_SIZE; ++aRow) {
            for (int aCol = 0; aCol < MATRIX_SIZE; ++aCol) {
                aCurrentCoord.myRow = aRow;
                aCurrentCoord.myCol = aCol;
                if (myMatrix[aRow][aCol] == Piece.EMPTY.myValue){
                    continue;
                }
                constructAllowedMovesFrom(aCurrentCoord);
            }
        }
    }

    private void constructAllowedMovesFrom(final Coordinate theCoord) {
        ArrayList<Coordinate> aPassedCoords = new ArrayList<>();
        // Look down
        seekFreeCellsRows(theCoord, aPassedCoords, true);
        formTransitionFromStartAndEnds(theCoord, aPassedCoords);

        // Look up
        seekFreeCellsRows(theCoord, aPassedCoords, false);
        formTransitionFromStartAndEnds(theCoord, aPassedCoords);

        // Look right
        seekFreeCellsCols(theCoord, aPassedCoords, true);
        formTransitionFromStartAndEnds(theCoord, aPassedCoords);

        // Look left
        seekFreeCellsCols(theCoord, aPassedCoords, false);
        formTransitionFromStartAndEnds(theCoord, aPassedCoords);
    }

    private void seekFreeCellsRows(Coordinate theStartCoord, ArrayList<Coordinate> thePassedCoords, final boolean theDir) {
        for (int aIdx = theStartCoord.myRow; aIdx <= MATRIX_SIZE; ++aIdx) {
            if (aIdx == MATRIX_SIZE) {
                thePassedCoords.clear();
                break;
            }
            int aRealIdx = theDir ? aIdx : MATRIX_SIZE - aIdx;
            int aValue = myMatrix[aRealIdx][theStartCoord.myCol];
            if (aValue > 0) {
                if (aValue == Piece.KING.myValue) {
                    while (thePassedCoords.size() > 3)
                    {
                        thePassedCoords.removeLast();
                    }
                } else if (aValue == Piece.DEFENDER.myValue || aValue == Piece.ATTACKER.myValue) {
                    if (SPECIAL_COORDINATES.contains(theStartCoord))
                    {
                        thePassedCoords.clear();
                    }
                }
                break;
            }
            thePassedCoords.add(new Coordinate(aRealIdx, theStartCoord.myCol));
        }
    }

    private void seekFreeCellsCols(Coordinate theStartCoord, ArrayList<Coordinate> thePassedCoords, final boolean theDir) {
        for (int aIdx = theStartCoord.myCol; aIdx <= MATRIX_SIZE; ++aIdx) {
            if (aIdx == MATRIX_SIZE) {
                thePassedCoords.clear();
                break;
            }
            int aRealIdx = theDir ? aIdx : MATRIX_SIZE - aIdx;
            int aValue = myMatrix[theStartCoord.myRow][aRealIdx];
            if (aValue > 0) {
                if (aValue == Piece.KING.myValue) {
                    while (thePassedCoords.size() > 3)
                    {
                        thePassedCoords.removeLast();
                    }
                } else if (aValue == Piece.DEFENDER.myValue || aValue == Piece.ATTACKER.myValue) {
                    if (SPECIAL_COORDINATES.contains(theStartCoord))
                    {
                        thePassedCoords.clear();
                    }
                }
                break;
            }
            thePassedCoords.add(new Coordinate(theStartCoord.myRow, aRealIdx));
        }
    }

    private void formTransitionFromStartAndEnds(final Coordinate theStartCoord, ArrayList<Coordinate> theEndCoords){
        for (Coordinate aCoord : theEndCoords) {
            Transition aNewTransition = new Transition();
            aNewTransition.myStartCoord = theStartCoord;
            aNewTransition.myDestCoord = aCoord;
            myAllowedTransitions.add(aNewTransition);
        }
        theEndCoords.clear();
    }
}

