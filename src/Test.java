import Board.Board;

public class Test {

    public static void main(String[] args) {
        Board myBoard = new Board();
        myBoard.setupDefaultPositions();

        BoardConsoleRenderer renderer = new BoardConsoleRenderer();
        renderer.render(myBoard);

        int f = 123;
    }
}
