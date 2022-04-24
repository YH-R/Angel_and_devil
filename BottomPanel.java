import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class BottomPanel extends JPanel{

	private Manager m = new Manager();

	private JRadioButton hOnD, hOffD, hOnA, hOffA;
	private JLabel whoseTurn;

	boolean devilHOn = false; //true if devil handicap mode is on
	boolean angelHOn = false; //true if angel handicap mode is on

	public BottomPanel( ){
		super();

		setPreferredSize( new Dimension( 600, 105 ) ); // (width,heigth)
		setLayout( new FlowLayout() );

		whoseTurn = new JLabel( "It's angel's turn, turn 1", SwingConstants.CENTER );
		whoseTurn.setVerticalAlignment( SwingConstants.CENTER );
		whoseTurn.setFont( new Font( "Times New Roman", Font.PLAIN, 25 ) );
		whoseTurn.setPreferredSize( new Dimension( 275, 30 ) );


		hOnD = new JRadioButton( "Turn Devil Handicap ON" );
		hOnD.setFont( new Font( "Gabriola", Font.PLAIN, 25 ) );
		hOnD.setPreferredSize( new Dimension( 240, 30 ) );
		hOnD.setSelected( false );
		hOnD.setActionCommand( "Turn Devil Handicap ON" );
		hOnD.addActionListener( new TurnDHandicapListener() );

		hOffD = new JRadioButton( "Turn Devil Handicap OFF" );
		hOffD.setFont( new Font( "Gabriola", Font.PLAIN, 25 ) );
		hOffD.setPreferredSize( new Dimension( 240, 30 ) );
		hOffD.setSelected( true );
		hOffD.setForeground( Color.RED );
		hOffD.setActionCommand( "Turn Devil Handicap OFF" );
		hOffD.addActionListener( new TurnDHandicapListener() );

		ButtonGroup groupD = new ButtonGroup();
		groupD.add( hOnD );
		groupD.add( hOffD );


		add( hOnD );
		add( hOffD );

		hOnA = new JRadioButton( "Turn Angel Handicap ON" );
		hOnA.setFont( new Font( "Gabriola", Font.PLAIN, 25 ) );
		hOnA.setPreferredSize( new Dimension( 240, 30 ) );
		hOnA.setSelected( false );
		hOnA.setActionCommand( "Turn Angel Handicap ON" );
		hOnA.addActionListener( new TurnAHandicapListener() );

		hOffA = new JRadioButton( "Turn Angel Handicap OFF" );
		hOffA.setFont( new Font( "Gabriola", Font.PLAIN, 25 ) );
		hOffA.setPreferredSize( new Dimension( 240, 30 ) );
		hOffA.setSelected( true );
		hOffA.setForeground( Color.RED );
		hOffA.setActionCommand( "Turn Angel Handicap OFF" );
		hOffA.addActionListener( new TurnAHandicapListener() );

		ButtonGroup groupA = new ButtonGroup();
		groupA.add( hOnA );
		groupA.add( hOffA );


		add( hOnD );
		add( hOffD );

		add( hOnA );
		add( hOffA );


		add( whoseTurn );

	}

	public void setManager( Manager m ){
		this.m = m;
	}

	public JLabel get_whoseTurn(){
		return whoseTurn;
	}

	public void set_WhoseTurn( boolean b ){
		if(b){
			whoseTurn.setText( "It's angel's turn, turn " + m.getBoard().getTurn() );
		}
		else{
			whoseTurn.setText( "It's devil's turn, turn " + m.getBoard().getTurn() );
		}
	}

	private class TurnDHandicapListener implements ActionListener{
		public void actionPerformed( ActionEvent e ){
			if( e.getActionCommand().equals( "Turn Devil Handicap ON" ) ){
				if( !devilHOn ){
					m.getBoard().setDHandicapped( true );
					hOnD.setForeground( Color.GREEN );
					hOffD.setForeground( Color.BLACK );
					devilHOn = true;
				}
			}
			else{
				if( devilHOn ){
					m.getBoard().setDHandicapped( false );
					hOnD.setForeground( Color.BLACK );
					hOffD.setForeground( Color.RED );
					devilHOn = false;
				}
			}
		}
	}


	private class TurnAHandicapListener implements ActionListener{
		public void actionPerformed( ActionEvent e ){
			if( e.getActionCommand().equals( "Turn Angel Handicap ON" ) ){
				if( !angelHOn ){
					m.getBoard().setAHandicapped( true );
					hOnA.setForeground( Color.GREEN );
					hOffA.setForeground( Color.BLACK );
					angelHOn = true;
				}
			}
			else{
				if( angelHOn ){
					m.getBoard().setAHandicapped( false );
					hOnA.setForeground( Color.BLACK );
					hOffA.setForeground( Color.RED );
					angelHOn = false;
				}
			}
		}
	}
}