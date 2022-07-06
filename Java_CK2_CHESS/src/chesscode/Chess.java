package chesscode;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.ClientInfoStatus;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Chess implements Chessitf, ActionListener, Runnable {

    private JFrame frame;
    private Model chessModel = new Model();
    private ChessPanel panel;
    private JButton resetbtn;
    private JButton connectbtn;
    private JButton listenbtn;

    private Socket socket;
    private PrintWriter printWriter;
    private Scanner scanner;

    Chess() {
        chessModel.reset();

        frame = new JFrame("Cờ vua");
        frame.setSize(750, 790);
        frame.setLocationRelativeTo(null);

        panel = new ChessPanel(this);

        // layout for connect socket
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        var buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // btn reset
        resetbtn = new JButton("Bắt đầu lại");
        buttonPanel.add(resetbtn);
        resetbtn.addActionListener(this);
        // btn connect
        connectbtn = new JButton("Bắt đầu chơi");
        buttonPanel.add(connectbtn);
        connectbtn.addActionListener(this);

        // btn listen
        listenbtn = new JButton("Kết nối");
        buttonPanel.add(listenbtn);
        listenbtn.addActionListener(this);
        frame.add(buttonPanel, BorderLayout.PAGE_END);

        frame.add(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // close socket after close window
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                printWriter.close();
                scanner.close();
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        new Chess();
    }

    @Override
    public PieceChess pieceAt(int col, int row) {
        return chessModel.pieceAt(col, row);
    }

    @Override
    public void movePiece(int fromCol, int fromRow, int toCol, int toRow) {
        // TODO Auto-generated method stub
        chessModel.movePiece(fromCol, fromRow, toCol, toRow);
        panel.repaint();
        ;
        // priwriter fix move
        if (printWriter != null) {
            printWriter.println(fromCol + "," + fromRow + "," + toCol + "," + toRow);
        }
    }

    private void receiveMove() {
        // gui du lieu qua server
        while (scanner.hasNextLine()) {
            var moveStr = scanner.nextLine();
            System.out.println("Nuoc vua di: " + moveStr);
            var moveStrArr = moveStr.split(",");
            var fromCol = Integer.parseInt(moveStrArr[0]);
            var fromRow = Integer.parseInt(moveStrArr[1]);
            var toCol = Integer.parseInt(moveStrArr[2]);
            var toRow = Integer.parseInt(moveStrArr[3]);

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                // gui thong tin server for client
                public void run() {
                    chessModel.movePiece(fromCol, fromRow, toCol, toRow);
                    panel.repaint();
                }
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource() == resetbtn) {
            chessModel.reset();
            panel.repaint();
        } else if (e.getSource() == connectbtn) {
            frame.setTitle("Người chơi 1");
            var pool = Executors.newFixedThreadPool(1);
            pool.execute(this);
            connectbtn.setEnabled(false);
            listenbtn.setEnabled(false);
            JOptionPane.showMessageDialog(frame, "Đang kết nối tới cổng chơi");
        } else if (e.getSource() == listenbtn) {
            listenbtn.setEnabled(false);
            connectbtn.setEnabled(false);
            frame.setTitle("Người chơi 2");
            try {
                socket = new Socket("localhost", 9999);
                scanner = new Scanner(socket.getInputStream());
                printWriter = new PrintWriter(socket.getOutputStream(), true);
                JOptionPane.showMessageDialog(frame, "Đã kết nối tới cổng chơi");
                Executors.newFixedThreadPool(1).execute(new Runnable() {
                    @Override
                    public void run() {
                        receiveMove();
                    }
                });
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try (var sever = new ServerSocket(9999)) {
            System.out.println("server is listening to port 9999");
            socket = sever.accept();
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            scanner = new Scanner(socket.getInputStream());

            receiveMove();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
