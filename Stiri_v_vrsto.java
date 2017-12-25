import java.awt.Button;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Stiri_v_vrsto extends JFrame implements ActionListener {
	
	int stPotez = 0;
	int gameOver = 0;
	static Button[][] gumbi = new Button[6][7];
	static Stiri_v_vrsto s;
	
	public Stiri_v_vrsto() {
		this.setVisible(true);
		this.setSize(700, 600);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setTitle("4 in a row - To play: X");
	}

	public static void main(String[] args) {
		s = new Stiri_v_vrsto();
		JPanel jp = new JPanel();
		GridLayout gl = new GridLayout(6, 7, 5, 5);
		jp.setLayout(gl);
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				gumbi[i][j] = new Button();
				gumbi[i][j].addActionListener(s);
				jp.add(gumbi[i][j]);
			}
		}	
		s.add(jp);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Button b = (Button) e.getSource();
		if (b.getLabel() == "" && isValidMove(b) && gameOver == 0) {
			if (stPotez % 2 == 0) {
				b.setLabel("X");
				s.setTitle("4 in a row - To play: O");
			}
			else {
				b.setLabel("O");
				s.setTitle("4 in a row - To play: X");
			}
			stPotez++;
			if (checkForVictory(b)) {
				gameOver = 1;
			}
		}
	}
	
	public boolean isValidMove(Button b) {
		int c = -1, d = -1;
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				if (gumbi[i][j] == b) {
					c = i;
					d = j;
					break;
				}
			}
		}	
		if (c == 5) return true;
		else if (gumbi[c+1][d].getLabel()!="") return true;
		else return false;
	}
	
	public boolean checkForVictory(Button b) {
		int c = -1, d = -1;
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				if (gumbi[i][j] == b) {
					c = i;
					d = j;
					break;
				}
			}
		}
		String l = b.getLabel();
		
		for (int j = 0; j < 4; j++) {
			if (gumbi[c][j].getLabel()==l && gumbi[c][j+1].getLabel()==l && gumbi[c][j+2].getLabel()==l && gumbi[c][j+3].getLabel()==l) {
				s.setTitle("4 in a row - Game Over: " + l + " wins");
				return true;
			}
		}	
		
		for (int i = 0; i < 3; i++) {
			if (gumbi[i][d].getLabel()==l && gumbi[i+1][d].getLabel()==l && gumbi[i+2][d].getLabel()==l && gumbi[i+3][d].getLabel()==l) {
				s.setTitle("4 in a row - Game Over: " + l + " wins");
				return true;
			}
		}
		
		int m = Math.min(c, d);
		int n = -1;
		if (d > c) {
			n = 6 - d;
		} else {
			n = 5 - c;
		}
		if (m + n >= 3) {
			int x = c - m;
			int y = d - m;
			for (int i = 0; i < m + n - 2; i++) {
				if (gumbi[x+i][y+i].getLabel()==l && gumbi[x+i+1][y+i+1].getLabel()==l && gumbi[x+i+2][y+i+2].getLabel()==l && gumbi[x+i+3][y+i+3].getLabel()==l) {
					s.setTitle("4 in a row - Game Over: " + l + " wins");
					return true;
				}
			}
		}
		
		int begin = Math.min(6-d, c);
		int end = Math.min(5-c, d);
		if (begin + end >= 3) {
			int x = c - begin;
			int y = d + begin;
			for (int i = 0; i < begin + end - 2; i++) {
				if (gumbi[x+i][y-i].getLabel()==l && gumbi[x+i+1][y-i-1].getLabel()==l && gumbi[x+i+2][y-i-2].getLabel()==l && gumbi[x+i+3][y-i-3].getLabel()==l) {
					s.setTitle("4 in a row - Game Over: " + l + " wins");
					return true;
				}
			}
		}
		return false;
	}
	
}
