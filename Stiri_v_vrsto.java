import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

public class Stiri_v_vrsto extends JFrame implements ActionListener {
	
	int gameOver = 0;
	static JButton[][] gumbi = new JButton[6][7];
	static JPanel jp;
	static Stiri_v_vrsto s;
	static ImageIcon ikona;
	static ImageIcon yikona;
	static boolean aizacne = false;
	static boolean multiplayer = false;
	static int player = 1;
	static int difficulty = 6; //4 - easy, 6 - medium, 8 - hard
	static int depth = 6; //easy - 4, medium - 6, hard - 8
	
	public Stiri_v_vrsto() {
		
		this.setSize(700, 600);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setTitle("4 in a row");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		createMenu(this);
	}

	public static void main(String[] args) {
		s = new Stiri_v_vrsto();
		ikona = new ImageIcon(s.getClass().getResource("redcircle.png"));
		yikona = new ImageIcon(s.getClass().getResource("yellowcircle.png"));
		//s.initiate();
	}
	
	public void createMenu(Stiri_v_vrsto s) {
		JMenuBar menubar = new JMenuBar();
        JMenu ngame = new JMenu("New Game");
        JMenuItem pMenuItem = new JMenuItem("Player starts");
        pMenuItem.addActionListener((ActionEvent event) -> {
            aizacne = false;
            multiplayer = false;
            gameOver = 0;
            depth = difficulty;
        	s.initiate();
        });
        JMenuItem aMenuItem = new JMenuItem("AI starts");
        aMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
	            aizacne = true;
	            multiplayer = false;
	            gameOver = 0;
	            depth = difficulty;
	        	s.initiate();
			}

        });
        JMenuItem ppMenuItem = new JMenuItem("Two players");
        ppMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
	            aizacne = false;
	            multiplayer = true;
	            gameOver = 0;
	            player = 1;
	        	s.initiate();
			}

        });
        
        ngame.add(pMenuItem);
        ngame.add(aMenuItem);
        ngame.add(ppMenuItem);
        menubar.add(ngame);
        
        JMenu difMenu = new JMenu("Difficulty");
        ButtonGroup difGroup = new ButtonGroup();

        JRadioButtonMenuItem easy = new JRadioButtonMenuItem("Easy (depth=4)");
        difMenu.add(easy);
        easy.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                difficulty = 4;
            }
        });

        JRadioButtonMenuItem med = new JRadioButtonMenuItem("Medium (depth=6)");
        difMenu.add(med);
        med.setSelected(true);
        med.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                difficulty = 6;
            }
        });

        JRadioButtonMenuItem hard = new JRadioButtonMenuItem("Hard (depth=8)");
        difMenu.add(hard);
        hard.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                difficulty = 8;
            }
        });

        difGroup.add(easy);
        difGroup.add(med);
        difGroup.add(hard);
        menubar.add(difMenu);

        
        s.setJMenuBar(menubar);
        s.setVisible(true);
	}
	
	public void initiate() {
		s.getContentPane().removeAll();
		s.setTitle("4 in a row");
		jp = new JPanel();
		GridLayout gl = new GridLayout(6, 7, 5, 5);
		jp.setLayout(gl);
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				gumbi[i][j] = new JButton();
				gumbi[i][j].addActionListener(s);
				gumbi[i][j].setName("");
				jp.add(gumbi[i][j]);
			}
		}	
		s.add(jp);
		s.setVisible(true);
		if (aizacne) s.aiplay();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();
		if (b.getName() == "" && isValidMove(b) && gameOver == 0) {
			if (multiplayer){
				if (player == 1) {
					b.setIcon(ikona);
					b.setName("X");
					if (zaznaj4("X")) {
						gameOver = 1;
						Stiri_v_vrsto.s.setTitle("4 in a row - Game Over: red player wins");
					}
					else if (vsePolno()) {
						gameOver = 1;
						Stiri_v_vrsto.s.setTitle("4 in a row - Game Over: draw");
					}
					player = 2;
				}
				else {
					b.setIcon(yikona);
					b.setName("O");
					if (zaznaj4("O")) {
						gameOver = 1;
						Stiri_v_vrsto.s.setTitle("4 in a row - Game Over: yellow player wins");
					}
					else if (vsePolno()) {
						gameOver = 1;
						Stiri_v_vrsto.s.setTitle("4 in a row - Game Over: draw");
					}
					player = 1;
				}
			} 
			else { // vs AI
				if (aizacne) {
					b.setIcon(yikona); 
				} else {
					b.setIcon(ikona);
				}
				b.setName("X");
				if (zaznaj4("X")) {
					gameOver = 1;
					Stiri_v_vrsto.s.setTitle("4 in a row - Game Over: player wins");
				}
				else if (vsePolno()) {
					gameOver = 1;
					Stiri_v_vrsto.s.setTitle("4 in a row - Game Over: draw");
				}
				if (gameOver == 0) aiplay();
			}
		}
	}
	
	public void aiplay() {

		Poteza poteza = minmax(1, depth, -1000, 1000); 
		if (aizacne) {
			gumbi[poteza.vrstica][poteza.stolpec].setIcon(ikona);
		} else {
			gumbi[poteza.vrstica][poteza.stolpec].setIcon(yikona);
		}
		gumbi[poteza.vrstica][poteza.stolpec].setName("O");
		if (zaznaj4("O")) {
			gameOver = 1;
			Stiri_v_vrsto.s.setTitle("4 in a row - Game Over: AI wins");
		}
		else if (vsePolno()) {
			gameOver = 1;
			Stiri_v_vrsto.s.setTitle("4 in a row - Game Over: draw");
		}
	}
	
	public Poteza minmax(int player, int nivo, int alpha, int beta) { 
		if (zaznaj4("O")) {
			return new Poteza(100 + nivo); 
		}
		if (zaznaj4("X")) {
			return new Poteza(-100 - nivo);
		}
		if (vsePolno()) {
			return new Poteza(0);
		}
		
		ArrayList<Poteza> poteze = poteze();
		Poteza toppoteza = null;
		if (player == 1) { //max value
			int v = -1000;
			for (Poteza p: poteze) {
				JButton b = gumbi[p.vrstica][p.stolpec];
				
					b.setName("O");
					if (nivo > 1){
						p.toèke = minmax(0, nivo-1, alpha, beta).toèke;
						
						if(p.toèke > v) {
							toppoteza = p;
							v = p.toèke;
							alpha = p.toèke;
						} 
						if (alpha >= beta) {
							//System.out.println(alpha + " " + beta + " " + v);
							b.setName("");
							return toppoteza;
							//break;
						}
						
					}
					if (nivo == 1) {
						if (zaznaj4("O")) {
							p.toèke = 100;
						} else {
							p.toèke = odprte3("O") - odprte3("X"); //hevristika
						}
						if(p.toèke > v) {
							toppoteza = p;
							v = p.toèke;
							alpha = p.toèke;
						} 
						if (alpha >= beta) {
							//System.out.println(alpha + " " + beta + " " + v);
							b.setName("");
							return toppoteza;
							//break;
						}
					}
					b.setName("");
				}
		}
		else { //min value
			int v = 1000;
			for (Poteza p: poteze) {
				JButton b = gumbi[p.vrstica][p.stolpec];
					b.setName("X");
					if (nivo > 1){
						p.toèke = minmax(1, nivo-1, alpha, beta).toèke;
						if (p.toèke < v) {
							toppoteza = p;
							v = p.toèke;
							beta = p.toèke;
						} 
						if (alpha >= beta) {
							//System.out.println(alpha + " " + beta + " " + v);
							b.setName("");
							return toppoteza;
							//break;
						}
					}
					if (nivo == 1) {
						if (zaznaj4("X")) {
							p.toèke = -100;
						} else {
							p.toèke = odprte3("O") - odprte3("X");
						}
						if (p.toèke < v) {
							toppoteza = p;
							v = p.toèke;
							beta = p.toèke;
						} 
						if (alpha >= beta) {
							//System.out.println(alpha + " " + beta + " " + v);
							b.setName("");
							return toppoteza;
							//break;
						}
					}
					b.setName("");
			}
		}
		//System.out.println(toppoteza.toèke);
		return toppoteza;
	}
	
	public boolean zaznaj4(String s) {
		//vrstice
		for (int i = 0; i < 6; i++){
			for (int j = 0; j < 4; j++) {
				if(gumbi[i][j].getName() == s && gumbi[i][j+1].getName() == s && gumbi[i][j+2].getName() == s && gumbi[i][j+3].getName() == s) {
					return true;
				}
			}
		}
		
		//stolpci
		for (int j = 0; j < 7; j++) {
			for (int i = 0; i < 3; i++) {
				if(gumbi[i][j].getName() == s && gumbi[i+1][j].getName() == s && gumbi[i+2][j].getName() == s && gumbi[i+3][j].getName() == s) {
					return true;
				}
			}
		}
		
		// diagonale \
		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 3; j++) {
				if(gumbi[i][j].getName() == s && gumbi[i+1][j+1].getName() == s && gumbi[i+2][j+2].getName() == s && gumbi[i+3][j+3].getName() == s) {
					return true;
				}
			}
		}
		
		// diagonale /
		for (int i = 0; i <= 2; i++) {
			for (int j = 3; j <= 6; j++) {
				if(gumbi[i][j].getName() == s && gumbi[i+1][j-1].getName() == s && gumbi[i+2][j-2].getName() == s && gumbi[i+3][j-3].getName() == s) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	public ArrayList<Poteza> poteze() {
		ArrayList<Poteza> poteze = new ArrayList<Poteza>();
		for (int j = 0; j <= 6; j++) {
			for (int i = 5; i >= 0; i--) {
				if (gumbi[i][j].getName() == "") {
					Poteza poteza = new Poteza(i, j);
					poteze.add(poteza);
					break;
				}
			}
		}
		return poteze;
	}

	public boolean isValidMove(JButton b) {
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
		else if (gumbi[c+1][d].getName()!="") return true;
		else return false;
	}
	
	public boolean vsePolno() {
			for (int j = 0; j < 7; j++) {
				if (gumbi[0][j].getName()=="") return false;
			}
		return true;
	}
	
	public int odprte3(String s) {
		int res = 0;
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				if(gumbi[i][j].getName()=="") {
					//vrstica
						if (j > 2 && gumbi[i][j-3].getName()==s && gumbi[i][j-2].getName()==s && gumbi[i][j-1].getName()==s
							||
							j > 1 && j < 6 && gumbi[i][j-2].getName()==s && gumbi[i][j-1].getName()==s && gumbi[i][j+1].getName()==s
							||
							j > 0 && j < 5 && gumbi[i][j-1].getName()==s && gumbi[i][j+1].getName()==s && gumbi[i][j+2].getName()==s
							||
							j < 4 && gumbi[i][j+1].getName()==s && gumbi[i][j+2].getName()==s && gumbi[i][j+3].getName()==s) {
							res++;
							continue;
						}
					//stolpec
						if (i < 3 && gumbi[i+1][j].getName()==s && gumbi[i+2][j].getName()==s && gumbi[i+3][j].getName()==s) {
							res++;
							continue;
						}
					
					//diagonala /
						if (i > 2 && j < 4 && gumbi[i-3][j+3].getName()==s && gumbi[i-2][j+2].getName()==s && gumbi[i-1][j+1].getName()==s
								||
								i > 1 && i < 5 && j < 5 && j > 0 && gumbi[i-2][j+2].getName()==s && gumbi[i-1][j+1].getName()==s && gumbi[i+1][j-1].getName()==s
								||
								i > 0 && i < 4 && j < 6 && j > 1 && gumbi[i-1][j+1].getName()==s && gumbi[i+1][j-1].getName()==s && gumbi[i+2][j-2].getName()==s
								||
								i < 3 && j > 2 && gumbi[i+1][j-1].getName()==s && gumbi[i+2][j-2].getName()==s && gumbi[i+3][j-3].getName()==s) {
							res++;
							continue;
						}
					
					//diagonala \
						if (i > 2 && j > 2 && gumbi[i-3][j-3].getName()==s && gumbi[i-2][j-2].getName()==s && gumbi[i-1][j-1].getName()==s
								||
								i > 1 && i < 5 && j > 1 && j < 6 && gumbi[i-2][j-2].getName()==s && gumbi[i-1][j-1].getName()==s && gumbi[i+1][j+1].getName()==s
								||
								i > 0 && i < 4 && j > 0 && j < 5 && gumbi[i-1][j-1].getName()==s && gumbi[i+1][j+1].getName()==s && gumbi[i+2][j+2].getName()==s
								||
								i < 3 && j < 4 && gumbi[i+1][j+1].getName()==s && gumbi[i+2][j+2].getName()==s && gumbi[i+3][j+3].getName()==s) {
							res++;
						}
				}
			}
		}	
		return res;
	}
	
}