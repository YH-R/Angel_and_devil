import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class Board9 extends JPanel implements ActionListener{

	public static final int SIZE = 9;
	public static final double FACTOR = 20.0;

	private UltimateButton[][][] b; 
	private Hexagon[][][] h; 
	private Hexagon[] adjacent = new Hexagon[6];
	private UltimateButton currentButton;

	private JButton reset, undo, export;

	private ArrayList<UltimateButton> angelHistory = new ArrayList<UltimateButton>();
	private ArrayList<UltimateButton> devilHistory = new ArrayList<UltimateButton>();

	private int turn;

	public Board9(){
		super();
		setLayout( null );
		setPreferredSize( new Dimension( (int)Math.ceil( ( (2*SIZE+1)*Math.sqrt(3) )*FACTOR ), (int)( (3*SIZE+2)*FACTOR ) ) ); //Width,Height

		newGame();
		setBackground( Color.WHITE );
	}

	public void eatButton( UltimateButton ub ){
		ub.set_eaten( true );
		ub.repaint();
		devilHistory.add( ub );
		System.out.println( "Eaten " + ub.get_Hexagon().toString() + "." );
	}

	public void uneatButton( UltimateButton ub ){
		ub.set_eaten( false );
		ub.repaint();	
		devilHistory.remove( ub );
	}

	public void eatRandom(){ //This method wastes one turn to eat an unimportant tile
		boolean breakAll = false;
		for( int i = 0; i <= SIZE; i++ ){
			for( int j = 0; j < b[i].length; j++ ){
				for( int k = 0; k < b[i][j].length; k++ ){
					if( !b[i][j][k].get_eaten() && !b[i][j][k].get_Hexagon().equals( currentButton.get_Hexagon() ) ){
						eatButton( b[i][j][k] );
						breakAll = true;
						break;
					}
				}
				if( breakAll ){
					break;
				}
			}
			if( breakAll ){
				break;
			}
		}
	}

	public void eatNotRandom(){
		Hexagon[] tempHexArray = currentButton.get_Hexagon().adjacentHexagons();

		for( int i = 0; i < 6; i++ ){
			if( !b[tempHexArray[i].get_power()][tempHexArray[i].get_direction()][tempHexArray[i].get_deviation()].get_eaten() ){
				eatButton( b[tempHexArray[i].get_power()][tempHexArray[i].get_direction()][tempHexArray[i].get_deviation()] );
				break;
			}
		}
	}

	public void paintUneaten( UltimateButton cb ){
		for( int i = 0; i <= SIZE; i++ ){
			for( int j = 0; j < b[i].length; j++ ){
				for( int k = 0; k < b[i][j].length; k++ ){
					b[i][j][k].setEnabled( false );
					b[i][j][k].set_current( false );
					b[i][j][k].set_adjacent( false );
				}
			}
		}

		cb.set_current( true );

		adjacent = currentButton.get_Hexagon().adjacentHexagons();

		for(int i = 0; i < 6; i++){
			b[adjacent[i].get_power()][adjacent[i].get_direction()][adjacent[i].get_deviation()].setEnabled( true );
			b[adjacent[i].get_power()][adjacent[i].get_direction()][adjacent[i].get_deviation()].set_adjacent( true );
		}
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

		for( int i = 0; i <= SIZE; i++ ){
			for( int j = 0; j < b[i].length; j++ ){
				for( int k = 0; k < b[i][j].length; k++ ){
					h[i][j][k] = new Hexagon( i, j, k );
					b[i][j][k] = new UltimateButton();
					b[i][j][k].set_Hexagon( h[i][j][k] );
					b[i][j][k].set_f_N_s( (int)FACTOR, (int)SIZE );
					b[i][j][k].set_value( (long)( Math.pow(2,i)*Math.pow(5,j)*Math.pow(3,k) ) );
					b[i][j][k].set_eaten( false );
					b[i][j][k].addActionListener( this );
					b[i][j][k].setBounds( h[i][j][k].xCoor( SIZE+1, FACTOR )-b[i][j][k].buttonWidth()/2+2, h[i][j][k].yCoor( SIZE+1, FACTOR )-b[i][j][k].buttonHeight()/6+2, b[i][j][k].buttonWidth()*2-4, b[i][j][k].buttonHeight()*4/3-4 );
					add(b[i][j][k]);
				}
			}
		}

		currentButton = b[0][0][0];

		paintUneaten( currentButton );

		angelHistory.clear();
		devilHistory.clear();

		//eatButton( b[SIZE][0][SIZE-1] );

		reset = new JButton( "<Html>Reset<br/>Board<Html/>" );
		reset.setFont( new Font( "Times New Roman", Font.PLAIN, (int)FACTOR ) );
		reset.setBounds(  0, 0, (int)(5*FACTOR), (int)(3*FACTOR) );
		reset.addActionListener( this );	
		add(reset);

		undo = new JButton( "<Html>Undo<br/>Move<Html/>" );
		undo.setFont( new Font( "Times New Roman", Font.PLAIN, (int)FACTOR ) );
		undo.setBounds(  (int)Math.ceil( ( (2*SIZE+1)*Math.sqrt(3) )*FACTOR )-(int)( 5*FACTOR ), 0, (int)( 5*FACTOR ), (int)( 3*FACTOR ) );
		undo.addActionListener( this );	
		add( undo );

		undo.setEnabled( false );

		export = new JButton( "<Html>Export<br/>Game<Html/>" );
		export.setFont( new Font( "Times New Roman", Font.PLAIN, (int)FACTOR ) );
		export.setBounds(  0, (int)( (3*SIZE+2)*FACTOR )-(int)(3*FACTOR), (int)(5*FACTOR), (int)(3*FACTOR) );
		export.addActionListener( this );	
		add( export );

		turn = 1;
		System.out.println("Turn " + turn);

	}

	public void actionPerformed( ActionEvent e ){
		String actionCommand = e.getActionCommand();
		System.out.println( "You clicked on " + actionCommand + " button. " );

		if( actionCommand.equals( "" ) ){ //hexagon
			Object o = e.getSource();
			currentButton = (UltimateButton) o;
			angelHistory.add( currentButton );

			paintUneaten( currentButton );

			System.out.println( "Angel moved to " + currentButton.get_Hexagon().toString() );

			//A.I.
			if(turn<=6){
				eatButton( b[SIZE][turn-1][SIZE-1] );
			}
			else if( currentButton.get_Hexagon().get_power() == SIZE-2 ){ // 2 tiles away from boundary
				if( !b[SIZE][currentButton.get_Hexagon().get_direction()][currentButton.get_Hexagon().get_deviation()+1].get_eaten() ){
					eatButton( b[SIZE][currentButton.get_Hexagon().get_direction()][currentButton.get_Hexagon().get_deviation()+1] );
				}
				else{
					eatNotRandom();
				}
			}
			else if( currentButton.get_Hexagon().get_power() == SIZE-1 ){ // 1 tile away from boundary
				if( !b[SIZE][currentButton.get_Hexagon().get_direction()][currentButton.get_Hexagon().get_deviation()].get_eaten() ){
					eatButton( b[SIZE][currentButton.get_Hexagon().get_direction()][currentButton.get_Hexagon().get_deviation()] );
				}
				else if( !b[SIZE][currentButton.get_Hexagon().get_direction()][currentButton.get_Hexagon().get_deviation()+1].get_eaten() ){
					eatButton( b[SIZE][currentButton.get_Hexagon().get_direction()][currentButton.get_Hexagon().get_deviation()+1] );
				}
				else if( !b[SIZE][(currentButton.get_Hexagon().get_direction()+1)%6][0].get_eaten() ){
					eatButton( b[SIZE][(currentButton.get_Hexagon().get_direction()+1)%6][0] );
				}
				else{
					eatNotRandom();
				}
			}
			else{
				eatNotRandom();
			}

			paintEaten();

			undo.setEnabled( true );

			turn++;
			System.out.println( "Turn " + turn );
		}

		else if( actionCommand.equals( "<Html>Reset<br/>Board<Html/>" ) ){ //Reset Board
			newGame();
			revalidate();
		}

		else if( actionCommand.equals( "<Html>Undo<br/>Move<Html/>" ) ){ //Undo Move
			if( turn <= 2 ){
				undo.setEnabled( false );
				currentButton = b[0][0][0];
			}
			else{
				currentButton = angelHistory.get( turn-3 );
			}

			uneatButton( devilHistory.get( turn-2 ) );
			angelHistory.remove( turn-2 );

			paintUneaten( currentButton );
			paintEaten();

			turn--;
			System.out.println( "Turn " + turn );
		}

		else if( actionCommand.equals( "<Html>Export<br/>Game<Html/>" ) ){ //Export Game
			File f = new File( "VS_CPU_Devil.csv" );
			Boolean bool = true;
			int i = 0;
			PrintWriter outputStream;

			while(bool){
				f = new File( "VS_CPU_Devil("+i+").csv" );
				bool = f.exists();
				i++;
			}

			try{
				outputStream = new PrintWriter( new FileOutputStream(f) );
				outputStream.println( "Angel,Devil" );

				if( turn > 1 ){
					for(int j = 0; j <= turn-2; j++ ){
						outputStream.print( angelHistory.get( j ).get_Hexagon().toString()+"," );
						outputStream.println( devilHistory.get( j ).get_Hexagon().toString() );
					}
				}

				outputStream.close();
				System.out.println( "File successfully exported." );
			}
			catch(FileNotFoundException ex){}
		}
	}

	public static void main( String[] args ){
		JFrame a = new JFrame();

		a.add( new Board9() );

		a.setTitle( "Hexagonal 1-Angel - Play as 1-Angel on board of size 9" );
		a.setResizable( false );
		a.pack();
		a.setLocationRelativeTo( null );
		a.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		a.setVisible( true );
	}
}