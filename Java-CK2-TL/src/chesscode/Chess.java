package chesscode;

import java.awt.Component;

import javax.swing.JFrame;

public class Chess {
public static void main(String[] args) {
	JFrame frame = new JFrame("Chess");
	frame.setSize(600,600);
	frame.setLocationRelativeTo(null);
	ChessPanel panel = new ChessPanel();
	frame.add(panel);
	frame.setVisible(true);
}
}
