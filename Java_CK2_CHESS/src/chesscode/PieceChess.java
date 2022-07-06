package chesscode;
enum Player{
    WHITE,
    BLACK,
}
enum Name {
    KING,
    QUEEN,
    PAWN,
    ROOK,
    BISHOP,
    KNIGHT,
}
public class PieceChess {
    int col;
    int row;
    Player player;
    Name namechess;
    String imgName;

    public PieceChess(int col, int row, Player player, Name namechess, String imgName) {
        super();
        this.col = col;
        this.row = row;
        this.player = player;
        this.namechess = namechess;
        this.imgName = imgName;
    }
}
