package jogoDoTermo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JOptionPane;

public class Armazenamento {
	public Armazenamento() {
		
	}
	protected String palavra(ArrayList<String> lista) {
		int index =  ThreadLocalRandom.current().nextInt(0, lista.size());
		return lista.get(index);
	}
	protected ArrayList<String> lista(String arquivo) {
		try {
			BufferedReader ler = new BufferedReader(new FileReader(arquivo));
			ArrayList<String> lista = new ArrayList<String>();
			String temp;
			while ((temp = ler.readLine())!=null) {
				lista.add(temp.toUpperCase());
			}
			ler.close();
			return lista;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
			System.exit(0);
			return null;
		}
	}
}
