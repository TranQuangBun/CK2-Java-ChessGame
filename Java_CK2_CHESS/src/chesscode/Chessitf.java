package chesscode;

public interface Chessitf {

    PieceChess pieceAt(int col, int row);
    void movePiece(int fromCol, int fromRow, int toCol, int toRow);
}
