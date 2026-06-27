package jogoDoTermo;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class Jogo extends JFrame{
	private static final Color VERDE = new Color(58, 163, 148);
	private static final Color AMARELO = new Color(211, 173, 105);
	private static final Color CINZA = new Color(49, 42, 44);
	private static final Color PADRAO = new Color(110, 92, 98);
	Armazenamento arq = new Armazenamento();
	JButton[][] buttons = new JButton[6][5];
	HashMap<JButton, Integer> indices = new HashMap<JButton, Integer>();
	HashMap<String, Integer> quantLetras = new HashMap<String, Integer>();
	ArrayList<JButton>[] teclado = new ArrayList[3];
	Selecionado select = new Selecionado(buttons, indices);
	ArrayList<String> lista = arq.lista("br-5-letras.txt");
	String palavra = arq.palavra(arq.lista("br-5-letras.txt"));
	public Jogo(){
		setSize(900, 900);
		setTitle("Termo");
		setLayout(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setBackground(new Color(110, 92, 98));
		init();
		setVisible(true);
	}
	public void init() {
		for (int i = 0; i < palavra.length(); i++) {
			quantLetras.put(palavra.charAt(i)+"", quantLetras.getOrDefault(palavra.charAt(i)+"", 0)+1);
		}
		criarBtn(getWidth()/4, 25, 75, 5, 7);
		select.alterar(0, 0);
		criarTeclado(getWidth()/6, 50, 50, 60, 5, 10);
	}
	
	public void criarBtn(int x, int y, int tamanho, int espaco, int borda) {
		int tempX = x;
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[0].length; j++) {
				buttons[i][j] = new JButton();
				indices.put(buttons[i][j], j);
				buttons[i][j].setBounds(tempX, y, tamanho, tamanho);
				buttons[i][j].setVisible(true);
				if(i==0) {
					buttons[i][j].setBorder(BorderFactory.createLineBorder(new Color(76, 67, 71), borda));
					buttons[i][j].setBackground(new Color(110, 92, 98));
			        
				} else {
					buttons[i][j].setBackground(new Color(97, 84, 88));
					buttons[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
					buttons[i][j].setEnabled(false);
				}
				buttons[i][j].setForeground(new Color(250, 250, 255));
				buttons[i][j].setFont(new Font("SansSerif", 1, 60));
				add(buttons[i][j]);
				selecionar(buttons[i][j]);
		        tempX+=espaco+tamanho;
			}
			tempX=x;
			y+=espaco+tamanho;
		}
	}
	
	public void teclas(JButton btn) {
		btn.addActionListener(e -> {
			select.btn.setBackground(new Color(110, 92, 98));
			select.btn.setText(btn.getText());
			select.escrever();
			select.btn.setBackground(new Color(97, 84, 88));
		}
		);
	}
	
	public void voltar(JButton btn) {
		btn.addActionListener(e -> {
			select.btn.setBackground(new Color(110, 92, 98));
			select.apagar();
			select.btn.setText("");
			select.btn.setBackground(new Color(97, 84, 88));
		}
		);
	}
	

	public void selecionar(JButton btn) {
		btn.addActionListener(e -> {
			select.btn.setBackground(new Color(110, 92, 98));
			select.alterar(select.posI, indices.get(btn));
			select.btn.setBackground(new Color(97, 84, 88));
		}
		);
	}
	
	public void enviar(JButton btn) {
		btn.addActionListener(e -> {
			HashMap<String, Integer> tempMap = new HashMap<String, Integer>(quantLetras);
			int count = 0;
			StringBuilder temp = new StringBuilder();

			for (int j = 0; j < buttons[0].length; j++) {
				if(buttons[select.posI][j].getText().equals("")) return;
				temp.append(buttons[select.posI][j].getText().trim());
			}

			if(!lista.contains(temp.toString())) {
				JOptionPane.showMessageDialog(null, "Não é uma palavra válida");
				return;
			}

			for (int j = 0; j < buttons[0].length; j++) {
				if(buttons[select.posI][j].getText().trim().equals(palavra.charAt(j)+"")) {
					buttons[select.posI][j].setBackground(VERDE);
					buttons[select.posI][j].setBorder(
						BorderFactory.createLineBorder(VERDE, 7)
					);
					percorrer(buttons[select.posI][j].getText()).setBackground(VERDE);

					count++;

					tempMap.put(
						buttons[select.posI][j].getText(),
						tempMap.getOrDefault(buttons[select.posI][j].getText(), 0)-1
					);
				}
			}

			for (int j = 0; j < buttons[0].length; j++) {
				if(buttons[select.posI][j].getBackground().equals(VERDE))
					continue;

				if(tempMap.getOrDefault(buttons[select.posI][j].getText(), 0) > 0) {

					buttons[select.posI][j].setBackground(AMARELO);
					buttons[select.posI][j].setBorder(
						BorderFactory.createLineBorder(AMARELO, 7)
					);
					if(!percorrer(buttons[select.posI][j].getText()).getBackground().equals(VERDE)) {
						percorrer(buttons[select.posI][j].getText()).setBackground(AMARELO);
					}

					tempMap.put(
						buttons[select.posI][j].getText(),
						tempMap.get(buttons[select.posI][j].getText()) - 1
					);
				}
			}

			for (int j = 0; j < buttons[0].length; j++) {
				if(!buttons[select.posI][j].getBackground().equals(VERDE)&&!buttons[select.posI][j].getBackground().equals(AMARELO)) {

					buttons[select.posI][j].setBackground(CINZA);
					buttons[select.posI][j].setBorder(
						BorderFactory.createLineBorder(CINZA, 7)
					);
					if(!buttons[select.posI][j].getBackground().equals(VERDE)&&!buttons[select.posI][j].getBackground().equals(AMARELO)) {
						percorrer(buttons[select.posI][j].getText()).setBackground(CINZA);
					}
					
				}

				buttons[select.posI][j].setEnabled(false);
			}

			if(count == 5) {
				JOptionPane.showMessageDialog(null, "Ganhou, parabéns");
				System.exit(0);
			}

			if(select.posI + 1 == buttons.length) {
				JOptionPane.showMessageDialog(null, "Perdeu, a palavra era "+palavra);
				System.exit(0);
			}

			select.alterar(select.posI + 1, 0);

			for (int j = 0; j < buttons[0].length; j++) {
				buttons[select.posI][j].setBorder(
					BorderFactory.createLineBorder(new Color(76, 67, 71), 7)
				);

				buttons[select.posI][j].setBackground(PADRAO);
				buttons[select.posI][j].setEnabled(true);
			}
		});
	}
	
	public void criarTeclado(int x, int y, int tamanhoX, int tamanhoY,  int espaco, int recuo) {
		String[] letras = "QWERTYUIOPASDFGHJKLZXCVBNM".split("");
		y+=buttons[buttons.length-1][buttons[0].length-1].getY()+buttons[buttons.length-1][buttons[0].length-1].getHeight();
		int tempX = x;
		for (int i = 0; i < teclado.length; i++) {
			teclado[i] = new ArrayList<JButton>();
			for (int j = 0; j < 10; j++) {
				if(i==2&&j>5) break;
				teclado[i].add(new JButton());
				teclado[i].getLast().setBounds(tempX, y, tamanhoX, tamanhoY);
				teclado[i].getLast().setVisible(true);
				teclado[i].getLast().setBackground(new Color(76, 67, 71));
				teclado[i].getLast().setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
				teclado[i].getLast().setForeground(new Color(250, 250, 255));
				teclado[i].getLast().setText(letras[i*10+j]+"");
				teclado[i].getLast().setFont(new Font("SansSerif", 1, 40));
				add(teclado[i].getLast());
				teclas(teclado[i].getLast());
		        tempX+=espaco+tamanhoX;
			}
			x+=recuo;
			tempX=x;
			y+=espaco+tamanhoY;
		}
		y-=espaco+tamanhoY;
		tempX = teclado[2].getLast().getX()+ tamanhoX + espaco +5;
		teclado[2].add(new JButton());
		teclado[2].getLast().setBounds(tempX, y, tamanhoX*2, tamanhoY);
		teclado[2].getLast().setVisible(true);
		teclado[2].getLast().setBackground(new Color(76, 67, 71));
		teclado[2].getLast().setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
		teclado[2].getLast().setForeground(new Color(250, 250, 255));
		teclado[2].getLast().setText("Voltar");
		voltar(teclado[2].getLast());
		teclado[2].getLast().setFont(new Font("SansSerif", 1, 30));
		add(teclado[2].getLast());
		tempX+=tamanhoX*2+espaco +5;
		teclado[2].add(new JButton());
		teclado[2].getLast().setBounds(tempX, y, tamanhoX*2, tamanhoY);
		teclado[2].getLast().setVisible(true);
		teclado[2].getLast().setBackground(new Color(76, 67, 71));
		teclado[2].getLast().setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
		teclado[2].getLast().setForeground(new Color(250, 250, 255));
		teclado[2].getLast().setText("Enviar");
		teclado[2].getLast().setFont(new Font("SansSerif", 1, 30));
		enviar(teclado[2].getLast());
		add(teclado[2].getLast());
	}
	
	public JButton percorrer(String letra) {
		for (int i = 0; i < teclado.length; i++) {
			for (int j = 0; j < 10; j++) {
				if(i==2&&j>5) break;
				if(teclado[i].get(j).getText().equals(letra)) return teclado[i].get(j);
			}
		}
		return null;
	}
}
class Selecionado{
	public Selecionado(JButton[][] buttons, HashMap<JButton, Integer> indices) {
		this.buttons = buttons;
		this.indices = indices;
	}
	HashMap<JButton, Integer> indices;
	JButton[][] buttons;
	JButton btn;
	int posI;
	int posJ;
	public void alterar(int posI, int posJ) {
		this.posI = posI;
		this.posJ = posJ;
		this.btn = buttons[this.posI][this.posJ];
	}
	public void escrever() {
		for (int j = 0; j < buttons[0].length; j++) {
			if(buttons[posI][j].getText().equals("")) {
				this.btn = buttons[posI][j];
				return;
			}
		}
		this.btn = buttons[posI][0];
	}
	public void apagar() {
		for (int j = buttons[0].length-1; j >=0 ; j--) {
			if(!buttons[posI][j].getText().equals("")) {
				this.btn = buttons[posI][j];
				return;
			}
		}
		this.btn = buttons[posI][0];
	}
}
