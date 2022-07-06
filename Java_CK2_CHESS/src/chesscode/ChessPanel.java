package chesscode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ChessPanel extends JPanel implements MouseListener, MouseMotionListener {

    Chessitf chessitf;

    private int originX = -1;
    private int originY = -1;
    private int cellside = -1;

    private Map<String, Image> keyNameValueImage = new HashMap<String, Image>();
    private int fromCol = -1;
    private int fromRow = -1;
    private PieceChess movingPiece;
    private Point movingPiecePoint;

    public ChessPanel(Chessitf chessitf) {
        this.chessitf = chessitf;

        String[] imageNames = {
                "bishop-black",
                "bishop-white",
                "king-black",
                "king-white",
                "knight-black",
                "knight-white",
                "pawn-black",
                "pawn-white",
                "queen-black",
                "queen-white",
                "rook-black",
                "rook-white",
        };
        try {
            for (String imgNm : imageNames) {
                Image img = addImage(imgNm + ".png");
                keyNameValueImage.put(imgNm, img);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // add mouse listener to chess piece
        addMouseListener(this);
        // add mouse listener motion
        addMouseMotionListener(this);
    }

    @Override
    protected void paintChildren(Graphics g) {

        super.paintChildren(g);

        // reponsive model
        int smaller = Math.min(getSize().width - 90, getSize().height - 100);
        cellside = smaller / 8;
        originX = (getSize().width + 5 - 8 * cellside) / 2;
        originY = (getSize().height + 5 - 8 * cellside) / 2;

        Graphics2D g2 = (Graphics2D) g;
        drawBoard(g2);
        drawPieces(g2);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
        g2.setColor(Color.black);
        for (int i = 0; i < 8; i++) {
            g2.drawString(((char) (i + 65)) + "", i * cellside + cellside / 2 + originY + 4, originY / 2 + 10);
            g2.drawString(((char) (i + 65)) + "", i * cellside + cellside / 2 + originY + 4,
                    getSize().height - originY / 2 + 10);
            g2.drawString(i + 1 + "", originX / 2 - 7, i * cellside + cellside / 2 + originX + 3);
            g2.drawString(i + 1 + "", getSize().width - originX / 2 - 7, i * cellside + cellside / 2 + originX + 3);
        }
    }

    private void drawPieces(Graphics2D g2) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                PieceChess piece = chessitf.pieceAt(col, row);
                if (piece != null && piece != movingPiece) {
                    drawImage(g2, col, row, piece.imgName);
                }
            }
        }
        // set moving piece with image on mouse
        if (movingPiece != null && movingPiecePoint != null) {
            Image img = keyNameValueImage.get(movingPiece.imgName);
            g2.drawImage(img, movingPiecePoint.x - cellside / 2, movingPiecePoint.y - cellside / 2, cellside, cellside,
                    null);
        }
    }

    // add chess, chinh vi tri chess piece
    private void drawImage(Graphics2D g2, int col, int row, String imgName) {
        Image img = keyNameValueImage.get(imgName);
        g2.drawImage(img, originX + col * cellside, originY + (7 - row) * cellside, cellside, cellside, null);
    }

    private Image addImage(String imgfilename) throws Exception {
        ClassLoader classloader = getClass().getClassLoader();
        java.net.URL url = classloader.getResource("chessimg/" + imgfilename);
        if (url == null) {
            return null;
        } else {
            File imgfile = new File(url.toURI());
            return ImageIO.read(imgfile);
        }
    }

    private void drawBoard(Graphics2D g2) {
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                drawSquare(g2, 0 + 2 * i, 0 + 2 * j, true);
                drawSquare(g2, 1 + 2 * i, 1 + 2 * j, true);

                drawSquare(g2, 1 + 2 * i, 0 + 2 * j, false);
                drawSquare(g2, 0 + 2 * i, 1 + 2 * j, false);
            }
        }
    }

    private void drawSquare(Graphics2D g2, int col, int row, boolean light) {
        g2.setColor(light ? Color.white : Color.gray);
        g2.fillRect(originX + col * cellside, originY + row * cellside, cellside, cellside);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    // move piece mouse listener
    @Override
    public void mousePressed(MouseEvent e) {
        // move piece khi press mouse, theo kí tự trắng đen (sau khi đổi chỗ trắng cho
        // đen)
        fromCol = (e.getPoint().x - originX) / cellside;
        fromRow = 7 - (e.getPoint().y - originY) / cellside;
        movingPiece = chessitf.pieceAt(fromCol, fromRow);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // đường số ô đi, sau khi fix press mouse
        int col = (e.getPoint().x - originX) / cellside;
        int row = 7 - (e.getPoint().y - originY) / cellside;
        // fix move lỗi nhảy khi di chuyển cờ
        if (fromCol != col || fromRow != row) {
            chessitf.movePiece(fromCol, fromRow, col, row);
        }
        // moving piece img (bóng của ảnh)
        movingPiece = null;
        movingPiecePoint = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        movingPiecePoint = e.getPoint();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }
}
