package chesscode;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.print.DocFlavor.URL;
import javax.swing.JPanel;

public class ChessPanel extends JPanel{

	int originX = 55;
	int originY = 45;
	int cellsize = 60;
	
	Map<String, Image> keyNameValueImage = new HashMap<String, Image>();
	public ChessPanel() {
		String[] imageNames = {
				"bishop-black",
				"bishop-white",
				"bishop-white",
				"bishop-white",
				"bishop-white",
				"bishop-white",
				"pawn-black",
				"pawn-white",
				"queen-black",
				"queen-white",
				"rook-black",
				"rook-white",
		};
		
			for (String imgNm: imageNames) {
				try {
				Image img = addImage(imgNm + ".png");
				keyNameValueImage.put(imgNm, img);
			} catch (Exception e) {
				// TODO: handle exception
			}
		
	}
}
	@Override
	protected void paintChildren(Graphics g) {
		// TODO Auto-generated method stub
		
		super.paintChildren(g);
		
		Graphics2D g2 = (Graphics2D)g;
		drawBoard(g2);
		
        drawImage(g2, 0 ,0, "rook-black");
        drawImage(g2, 0 ,1, "pawn-black");
		
		
}
//  add chess, chinh vi tri chess piece
  private void drawImage(Graphics2D g2,  int col, int row, String imgName) {
  	Image img = keyNameValueImage.get(imgName);
  	g2.drawImage(img,originX + col* cellsize, originY + row * cellsize, cellsize, cellsize, null);
  }
    private Image addImage(String imgfilename) throws Exception {
	ClassLoader classloader = getClass().getClassLoader();
	java.net.URL url = classloader.getResource("chessimg/" +imgfilename);
    if (url == null) {
return null;
    } else {
			File imgfile = new	File(url.toURI( ));
			return ImageIO.read(imgfile);

		} 
    
}
    

	private void drawBoard(Graphics2D g2) {
		for (int j = 0; j < 4; j++) {
			for(int i = 0; i < 4; i++) {
				drawSquare(g2, 0+2*i, 0+2*j, true);
				drawSquare(g2, 1+2*i, 1+2*j, true);
				
				drawSquare(g2, 1+2*i, 0+2*j, false);
				drawSquare(g2, 0+2*i, 1+2*j, false);
				}
		}
	}
	private void drawSquare(Graphics2D g2, int col, int row, boolean light) {
	   g2.setColor(light ? Color.white : Color.gray);
	   g2.fillRect(originX + col *cellsize, originY + row *cellsize, cellsize, cellsize);
	}
}
