import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;

public class AngelMenuBar extends JMenuBar{
	private JMenuItem jmuUndo, jmuNewGame, jmuExport;
	private JMenu options;
	private Manager m;

	public AngelMenuBar(){

		options = new JMenu("Options");

		jmuUndo = new JMenuItem( "Undo" );
		jmuUndo.setEnabled( false );
		jmuUndo.addActionListener( new UndoListener() );
		jmuUndo.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK) );

		jmuNewGame = new JMenuItem( "New Game" );
		jmuNewGame.addActionListener( new NewGameListener() );
		jmuNewGame.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK) );

		jmuExport = new JMenuItem( "Export game" );
		jmuExport.addActionListener( new ExportListener() );


		options.add( jmuUndo );
		options.add( jmuNewGame );
		options.add( jmuExport );

		add( options );
		setPreferredSize( new Dimension(600,25) );
	}

	public void setManager( Manager m ){
		this.m = m;
	}

	public JMenuItem getUndo(){
		return jmuUndo;
	}

	private class UndoListener implements ActionListener{
		public void actionPerformed(ActionEvent e){

			int tempTurn = m.getBoard().getTurn();
			System.out.println( "tempTurn = " + tempTurn );

			if(tempTurn <= 2){ // first move of angel made only
				jmuUndo.setEnabled(false);
				m.getBoard().devilMoveSpecial( m.getBoard().getHistory().get( (tempTurn-2)/2 ) );
			}
			else if(tempTurn%2 == 0){ //previous move was made by angel
				m.getBoard().devilMove( m.getBoard().getHistory().get( (tempTurn-2)/2 ), false );
			}
			else{ //previous move was made by devil
				m.getBoard().angelMove( m.getBoard().getEatenButtons().get( (tempTurn-3)/2 ), false );
			}
		}
	}

	private class NewGameListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			m.getBoard().newGame();
			jmuUndo.setEnabled(false);
			System.out.println("The game has been reset");
		}
	}

	private class ExportListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			File f = new File("VS_Player.csv");
			Boolean bool = true;
			int i = 0;
			int tempTurn = m.getBoard().getTurn();
			PrintWriter outputStream;

			while(bool){
				f = new File("VS_Player("+i+").csv");
				bool = f.exists();
				i++;
			}

			try{
				outputStream = new PrintWriter( new FileOutputStream(f) );
				outputStream.println("Angel,Devil");

				if(tempTurn > 1){
					for(int j = 2; j <= tempTurn; j++ ){
						if( j%2 == 0 ){
							outputStream.print(m.getBoard().getHistory().get((j-2)/2).get_Hexagon().toString()+",");
						}
						else{
							outputStream.println(m.getBoard().getEatenButtons().get((j-3)/2).get_Hexagon().toString());
						}
					}
				}

				outputStream.close();
				System.out.println( "File successfully exported." );
			}
			catch(FileNotFoundException ex){}
		}
	}


}