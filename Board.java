import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Math;
import java.util.ArrayList;

public class Board extends JPanel implements ActionListener{

	public static final double FACTOR = 18.0;
	private int SIZE;
	private Manager m;

	private UltimateButton[][][] b; 
	private Hexagon[][][] h; 
	private Hexagon[] adjacent = new Hexagon[6];
	private ArrayList<UltimateButton> history = new ArrayList<UltimateButton>();
	private ArrayList<UltimateButton> eatenButton = new ArrayList<UltimateButton>();
	private UltimateButton currentButton;
	private Hexagon currentHexagon;

	private boolean devilHandicapped = false;
	private boolean angelHandicapped = false;

	private int turn = 1;

	public Board(int s){
		super();
		SIZE = s;
		setLayout(null);
		setPreferredSize(new Dimension((int)Math.ceil(((2*SIZE+1)*Math.sqrt(3))*FACTOR),(int)((3*SIZE+2)*FACTOR))); //Width,Height

		newGame();
	}

	public void setManager(Manager m){
		this.m = m;
	}

	public int getTurn(){
		return turn;
	}

	public void setTurn(int t){
		turn = t;
	}

	public void setDHandicapped( boolean bool ){
		devilHandicapped = bool;
		if( turn%2 == 1 ){ //if angel's turn now
			turn++;
			angelMove( currentButton, true );
		}
		if( !bool ){
			devilMove( history.get( history.size()-1 ), false );
		}
		System.out.println( "Devil is now handicapped = " + devilHandicapped );
	}

	public void setAHandicapped( boolean bool ){
		angelHandicapped = bool;
		if( turn%2 == 1 ){ //if angel's turn now
			paintUneaten( currentButton );
		}
		System.out.println( "Angel is now handicapped = " + devilHandicapped );
	}

	public UltimateButton[][][] get_UBList(){
		return b;
	}

	public int get_size(){
		return SIZE;
	}

	public ArrayList<UltimateButton> getHistory(){
		return history;
	}

	public ArrayList<UltimateButton> getEatenButtons(){
		return eatenButton;
	}

	public void eatButton( UltimateButton ub ){
		ub.set_eaten( true );
		ub.repaint();
		eatenButton.add( ub );
		System.out.println( "Eaten " + ub.get_Hexagon().toString() +"." );
	}


	public void uneatButton( UltimateButton ub ){
		ub.set_eaten( false );
		ub.repaint();
		eatenButton.remove( ub );
	}

	public void paintUneaten( UltimateButton cb ){ //Next move is angel's
		for(int i = 0; i <= SIZE; i++){
			for(int j = 0; j < b[i].length; j++){
				for(int k = 0; k < b[i][j].length; k++){
					b[i][j][k].setEnabled(false);
					b[i][j][k].set_current( false );
					b[i][j][k].set_adjacent( false );
				}
			}
		}

		cb.set_current( true );

		currentHexagon = currentButton.get_Hexagon();
		adjacent = currentHexagon.adjacentHexagons();


		for(int i = 0; i < 6; i++){
			if( !angelHandicapped || ( angelHandicapped && adjacent[i].get_power() >= currentButton.get_Hexagon().get_power() ) ){
				b[adjacent[i].get_power()][adjacent[i].get_direction()][adjacent[i].get_deviation()].setEnabled( true );
				b[adjacent[i].get_power()][adjacent[i].get_direction()][adjacent[i].get_deviation()].set_adjacent( true );	
			}
		}
	}

	public void paintUneaten2( UltimateButton cb ){ //Next move is devil's
		for(int i = 0; i <= SIZE; i++){
			for(int j = 0; j < b[i].length; j++){
				for(int k = 0; k < b[i][j].length; k++){
					b[i][j][k].setEnabled( true );
					b[i][j][k].set_current( false );
					b[i][j][k].set_adjacent( false );
					b[i][j][k].repaint();
				}
			}
		}

		cb.set_current( true );
		cb.setEnabled( false );
	}

	public void paintEaten(){
		for( int i = 0; i <= SIZE; i++ ){
			for( int j = 0; j < b[i].length; j++ ){
				for( int k = 0; k < b[i][j].length; k++ ){
					if( b[i][j][k].get_eaten() ){
						b[i][j][k].setEnabled( false );
					}
				}
			}
		}
	}

	public void newGame(){
		removeAll();

		b = new UltimateButton[SIZE+1][][];
		h = new Hexagon[SIZE+1][][];

		for( int i = 0; i <= SIZE; i++ ){
			if( i == 0 ){
				b[i] = new UltimateButton[1][1];
				h[i] = new Hexagon[1][1];
			}
			else{
				b[i] = new UltimateButton[6][i];
				h[i] = new Hexagon[6][i];
			}
		}

		history.clear();
		eatenButton.clear();

		for(int i = 0; i <= SIZE; i++){
			for(int j = 0; j < b[i].length; j++){
				for(int k = 0; k < b[i][j].length; k++){
					h[i][j][k] = new Hexagon( i, j, k );
					b[i][j][k] = new UltimateButton();
					b[i][j][k].set_Hexagon( h[i][j][k] );
					b[i][j][k].set_f_N_s( (int)FACTOR, (int)SIZE );
					b[i][j][k].set_value( (long)(Math.pow(2,i)*Math.pow(5,j)*Math.pow(3,k)) );
					b[i][j][k].set_eaten( false );
					b[i][j][k].addActionListener( this );
					b[i][j][k].setBounds( h[i][j][k].xCoor( SIZE+1, FACTOR )-b[i][j][k].buttonWidth()/2+2, h[i][j][k].yCoor( SIZE+1, FACTOR )-b[i][j][k].buttonHeight()/6+2, b[i][j][k].buttonWidth()*2-4, b[i][j][k].buttonHeight()*4/3-4 );
					add( b[i][j][k] );
				}
			}
		}

		currentButton = b[0][0][0];

		paintUneaten( currentButton );

		if(turn>1){
			turn = 1;
			m.getBottomPanel().set_WhoseTurn( true );
		}

		turn = 1;

		revalidate();
	}

	public void devilMoveSpecial( UltimateButton devilb ){ //undo first angel move, after this is angel's turn
		currentButton = b[0][0][0];
		history.clear();

		paintUneaten( currentButton );

		paintEaten();

		m.getBottomPanel().set_WhoseTurn( true );

		System.out.println( "The last angel move has been undone, it is a new game now.");

		turn = 1;

		m.getBottomPanel().set_WhoseTurn( true );
	}

	public void devilMove( UltimateButton devilb, boolean bool ){ //after this move is angel's turn
		if(bool){
			eatButton( devilb );

			if( devilHandicapped ){
				turn++;
				angelMove( currentButton, true );
			}

			int count = 0;
			for( int i = 0; i < 6; i++ ){
				if( currentButton.get_Hexagon().adjacentHexagons()[i].get_button( b ).get_eaten() ){
					count++;
				}
			}

			if( count == 6 ){
				int n = JOptionPane.showConfirmDialog( null, "Game over. Devil wins. New game?", "Choose an option", JOptionPane.YES_NO_OPTION );
				if( n == JOptionPane.YES_OPTION ){
					newGame();
				}
				else{
					if( devilHandicapped ){
						devilMove( currentButton, false );
					}	
					angelMove( devilb, false );
				}
			}
			else{
				if( devilHandicapped ){
					paintUneaten2( currentButton );
				}
				else{
					paintUneaten( currentButton );
				}
			}

		}
		else{
			turn--;
			if( turn > 2 ){
				currentButton = history.get( history.size()-2 );
			}
			else{
				currentButton = b[0][0][0];
			}
			history.remove( devilb );
			System.out.println( "The last angel move has been undone.");

			paintUneaten( currentButton );
		}

		paintEaten();

		m.getBottomPanel().set_WhoseTurn( true );
	}

	public void angelMove( UltimateButton angelb, boolean bool ){ //after this move is devil's turn
		boolean gameOver = false;

		if( bool ){
			history.add( angelb );
			currentButton = angelb;
			if( angelb.get_Hexagon().get_power()  == SIZE ){
				gameOver = true;
			}
			System.out.println( "The Angel has landed on " + currentButton.get_Hexagon().toString() );
		}
		else{
			turn--;
			uneatButton( angelb );
			System.out.println( "The last devil move has been undone.");
		}

		if( gameOver ){
			int n = JOptionPane.showConfirmDialog( null, "Game over. Angel wins. New game?", "Choose an option", JOptionPane.YES_NO_OPTION );
			if( n == JOptionPane.YES_OPTION ){
				newGame();
			}
			else{
				devilMove( angelb , false );
			}
		}
		else{
			paintUneaten2( currentButton );

			paintEaten();

			m.getBottomPanel().set_WhoseTurn( false );
		}
	}

	public void actionPerformed( ActionEvent e ){
		turn++;
		System.out.println( "Turn " + turn );

		Object o = e.getSource();

		if( turn%2 == 0 ){ //Angel's move
			angelMove( (UltimateButton) o, true );
		}
		else{ // Devil's move
			devilMove( (UltimateButton) o, true );
		}

		System.out.println( "The angel is currently on " + currentButton.get_Hexagon().toString() );

		m.getAngelMenuBar().getUndo().setEnabled( true );
	}


}