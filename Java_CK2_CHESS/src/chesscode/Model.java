package chesscode;

import java.util.HashSet;
import java.util.Set;

public class Model {
    private Set<PieceChess> piecesBox = new HashSet<PieceChess>();
    private Player player1Turn = Player.WHITE;
    //set chess
    public void reset() {
        piecesBox.removeAll(piecesBox);

        for (int i = 0; i < 2; i++) {
            piecesBox.add(new PieceChess(0 + i * 7, 7, Player.BLACK, Name.ROOK,"rook-black"));
            piecesBox.add(new PieceChess(0 + i * 7, 0, Player.WHITE, Name.ROOK,"rook-white"));

            piecesBox.add(new PieceChess(1 + i * 5, 7, Player.BLACK, Name.KNIGHT,"knight-black"));
            piecesBox.add(new PieceChess(1 + i * 5, 0, Player.WHITE, Name.KNIGHT,"knight-white"));

            piecesBox.add(new PieceChess(2 + i * 3, 7, Player.BLACK, Name.BISHOP,"bishop-black"));
            piecesBox.add(new PieceChess(2 + i * 3, 0, Player.WHITE, Name.BISHOP,"bishop-white"));

        }
        for (int i = 0; i < 8; i++) {
            piecesBox.add(new PieceChess(i, 6, Player.BLACK, Name.PAWN,"pawn-black"));
            piecesBox.add(new PieceChess(i, 1, Player.WHITE, Name.PAWN,"pawn-white"));
        }
        piecesBox.add(new PieceChess(4, 7, Player.BLACK, Name.QUEEN,"queen-black"));
        piecesBox.add(new PieceChess(4, 0, Player.WHITE, Name.QUEEN,"queen-white"));
        piecesBox.add(new PieceChess(3, 7, Player.BLACK, Name.KING,"king-black"));
        piecesBox.add(new PieceChess(3, 0, Player.WHITE, Name.KING,"king-white"));

        player1Turn = Player.WHITE;
    }

    // di chuyen chess
    public void movePiece(int fromCol, int fromRow, int toCol, int toRow) {
        PieceChess candidate = pieceAt(fromCol, fromRow);
        if (candidate == null /*chỉnh turn đi trước */|| candidate.player != player1Turn || fromCol == toCol && fromRow == toRow ) {
            return;
        }

// fix di chuyển đè nhau (ăn quân cờ)
        PieceChess aim = pieceAt(toCol, toRow);
        if (aim != null) {
            if(aim.player == candidate.player) {
                return;
            }else {
                piecesBox.remove(aim);
            }
        }

        candidate.col = toCol;
        candidate.row = toRow;

        /*chỉnh turn đi trước */
        player1Turn = player1Turn == Player.WHITE ? Player.BLACK : Player.WHITE;

    }
    // set vi tri chess
    public PieceChess pieceAt(int col, int row) {
        for (PieceChess chesspiece : piecesBox) {
            if (chesspiece.col == col && chesspiece.row == row) {
                return chesspiece;
            }
        }
        return null;
    }

    public String toString() {
        String desc = "";
        for (int row = 7; row >= 0; row --) {
            desc += "" +row;
            for (int col = 0; col <8; col ++) {
                PieceChess piece = pieceAt(col, row);
                if (piece == null) {
                    desc +=" .";
                }else {
                    desc += " ";
                    switch(piece.namechess) {
                        case KING:
                            desc += piece.player == Player.WHITE ? "k" :"K";
                            break;
                        case QUEEN:
                            desc += piece.player == Player.WHITE ? "q" :"Q";
                            break;
                        case BISHOP:
                            desc += piece.player == Player.WHITE ? "b" :"B";
                            break;
                        case ROOK:
                            desc += piece.player == Player.WHITE ? "r" :"R";
                            break;
                        case PAWN:
                            desc += piece.player == Player.WHITE ? "p" :"P";
                            break;
                        case KNIGHT:
                            desc += piece.player == Player.WHITE ? "n" :"N";
                            break;
                    }
                }
            }
            desc +="\n";
        }
        desc +="  0 1 2 3 4 5 6 7";
        return desc;
    }
}
