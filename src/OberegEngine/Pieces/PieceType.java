package OberegEngine.Pieces;

// Перечисление типа enum для указания разновидностей фигуры
public enum PieceType{
    WARRIOR(500) {
        @Override
        public boolean isKing() {
            return false;
        }
    },
    KING( 5000) {
        @Override
        public boolean isKing() {
            return true;
        }
    };
    private final int pieceValue;
    PieceType(final int pieceValue){
        this.pieceValue = pieceValue;
    }
    public int getPieceValue(){
        return this.pieceValue;
    }
    public abstract boolean isKing();
}
