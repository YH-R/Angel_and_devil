import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class MathProject extends JFrame{

	private JButton change;
	private JLabel theSizeTo, whoseTurn;
	private JTextField jtf;
	private JPanel p1, p2;
	private JScrollPane jsp, jsp2;

	private AngelMenuBar amb;
	private Board b;
	private BottomPanel bp;
	private Manager m;

	public MathProject(){
		setLayout(new BorderLayout());
		setSize( 600, 600 ); //width, height

		int s = 0;
		String string = "";
		boolean bool = true;
		while( bool ){
			string = JOptionPane.showInputDialog( null, "Enter size of board", "Start Up", JOptionPane.QUESTION_MESSAGE );

			if( string == null ){
				System.exit(0);
			}

			try{
				s = Integer.parseInt( string );

				if( s > 0 && s < 50 ){
					bool = false;
				}
				else{
					JOptionPane.showMessageDialog( null, "Error! Positive integer smaller than 50 expected. " );
					bool = true;
				}
			}
			catch(NumberFormatException e){
				JOptionPane.showMessageDialog( null, "Error! Positive integer smaller than 50 expected. " );
				bool = true;
			}
		}

		amb = new AngelMenuBar();
		b = new Board( s );
		bp = new BottomPanel();
		

		m = new Manager( amb, b, bp );
		amb.setManager( m );
		bp.setManager( m );
		b.setManager( m );

		jsp = new JScrollPane( b );


		change = new JButton( "Change" );
		change.setFont( new Font( "Comic Sans MS", Font.PLAIN, 18 ) );
		change.setPreferredSize( new Dimension( 93, 35 ) );
		change.addActionListener( new ChangeSizeListener() );	

		theSizeTo = new JLabel( "the Board Size to:", SwingConstants.CENTER );
		theSizeTo.setVerticalAlignment( SwingConstants.CENTER );
		theSizeTo.setFont( new Font( "Comic Sans MS", Font.PLAIN, 18 ) );
		theSizeTo.setPreferredSize( new Dimension( 150, 35 ) );

		jtf = new JTextField( ""+b.get_size(), SwingConstants.CENTER );
		jtf.setFont( new Font( "Comic Sans MS", Font.PLAIN, 18 ) );
		jtf.setPreferredSize( new Dimension( 30, 30 ) );

		p1 = new JPanel();
		p1.setLayout( new FlowLayout() );
		p1.setPreferredSize( new Dimension( 400, 40 ) );

		p1.add( change );
		p1.add( theSizeTo );
		p1.add( jtf );


		p2 = new JPanel();
		p2.setLayout( new FlowLayout() );
		p2.setPreferredSize( new Dimension( 600, 160 ) );

		p2.add( bp );
		p2.add( p1 );

		jsp2 = new JScrollPane( p2 );

		add( amb, BorderLayout.NORTH );
		add( jsp2, BorderLayout.SOUTH );
		add( jsp, BorderLayout.CENTER );


		setTitle( "Hexagonal 1-Angel" );
		pack();
		setLocationRelativeTo( null );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setVisible( true );
	}

	public static void main( String args[] ){
		MathProject mp = new MathProject();
	}


	private class ChangeSizeListener implements ActionListener{
		public void actionPerformed( ActionEvent e ){
			int s = 0;
			String string = jtf.getText();
			boolean bool;

			try{
				s = Integer.parseInt( string );

				if( s > 0 && s < 50 ){
					bool = false;
				}
				else{
					JOptionPane.showMessageDialog( null, "Error! Positive integer smaller than 50 expected. " );
					bool = true;
				}
			}
			catch( NumberFormatException ex ){
				JOptionPane.showMessageDialog( null, "Error! A number expected. " );
				bool = true;
			}

			if( bool ){
				jtf.setText( ""+b.get_size() );
			}
			else{
				if( s != b.get_size() ){
					getContentPane().removeAll();

					amb = new AngelMenuBar();
					b = new Board( s );
					bp = new BottomPanel();

					m.setAngelMenuBar( amb );
					m.setBoard( b );
					m.setBottomPanel( bp );

					m.getAngelMenuBar().setManager( m );
					m.getBoard().setManager( m );
					m.getBottomPanel().setManager( m );

					jtf.setText( ""+s );

					jsp2 = new JScrollPane( p2 );
					jsp = new JScrollPane( b );

					add( amb, BorderLayout.NORTH );
					add( jsp2, BorderLayout.SOUTH );
					add( jsp, BorderLayout.CENTER );

					pack();
					setLocationRelativeTo( null );
					revalidate();
				}
			}
		}
	}
}