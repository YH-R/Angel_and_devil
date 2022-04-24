import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UltimateButton extends JButton{
	private long value;
	private boolean eaten = true;
	private boolean isCurrent = false;
	private boolean isAdjacent = false;
	private Hexagon h;

	private double factor = 0;
	private int size = 0;

	public static final Color[] RAINBOW = { new Color( 255, 0, 0 ), new Color( 255, 127, 80 ), new Color( 255, 255, 0 ), new Color( 0, 153, 0 ), new Color( 0, 0, 204 ), new Color( 204, 0, 204 ) }; // RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE

	private Polygon hexagon = new Polygon();

	//Constructors
	public UltimateButton(){ //When using JButton
		super();
		setContentAreaFilled( false );
	}

	public UltimateButton(ImageIcon x){ //When using JButton
		super(x);
		setContentAreaFilled( false );
	}

	//Assessor and mutator methods
	public void set_value(long d){
		value = d;
	}

	public long get_value(){
		return value;
	}

	public void set_Hexagon(Hexagon hexagon){
		h = hexagon;
	}

	public void set_factor( double f ){
		factor = f;
	}

	public double get_factor(){
		return factor;
	}

	public void set_size( int s ){
		size = s;
	}

	public void set_f_N_s( int f, int s ){
		set_factor( f );
		set_size( s );
	}

	public int get_size(){
		return size;
	}

	public Hexagon get_Hexagon(){
		return h;
	}

	public boolean get_eaten(){
		return eaten;
	}

	public void set_eaten(boolean b){
		eaten = b;
		h.set_eaten(b);
	}

	public boolean get_current(){
		return isCurrent;
	}

	public void set_current(boolean b){
		isCurrent = b;
	}

	public boolean get_adjacent(){
		return isAdjacent;
	}

	public void set_adjacent(boolean b){
		isAdjacent = b;
	}

	//Other methods
	public int buttonWidth(){
		return (int)(Math.ceil(Math.sqrt(3)/2.0*factor));
	}

	public int buttonHeight(){
		return (int)(3.0/2.0*factor);
	}


	//Overridden methods

	@Override public boolean contains(Point p){
		return hexagon.contains(p);
	}

	@Override public void setBounds(int x, int y, int width, int height){
		super.setBounds(x, y, width, height);
		calculateCoords();
	}

	@Override public void setBounds(Rectangle r){
		super.setBounds(r);
		calculateCoords();
	}

	@Override protected void processMouseEvent(MouseEvent e){
		if ( contains(e.getPoint()) ){
			super.processMouseEvent(e);
		}
	}

	private void calculateCoords(){
		int w = getWidth()-1;
		int h = getHeight()-1;

		int ratio = (int)(h*.25);
		int nPoints = 6;
		int[] hexX = new int[nPoints];
		int[] hexY = new int[nPoints];

		hexX[0] = w/2;
		hexY[0] = 0;
		hexX[1] = w;
		hexY[1] = ratio;
		hexX[2] = w;
		hexY[2] = h - ratio;
		hexX[3] = w/2;
		hexY[3] = h;
		hexX[4] = 0;
		hexY[4] = h - ratio;
		hexX[5] = 0;
		hexY[5] = ratio;

		hexagon = new Polygon(hexX, hexY, nPoints);
	}

	@Override protected void paintComponent(Graphics g){
		if ( eaten ){
			g.setColor( new Color( 255, 153, 153 ) ); //Pink
			g.fillPolygon( hexagon );
		}
		else if( isCurrent ){
			g.setColor( new Color( 255, 255, 153 ) ); //Light Yellow
			g.fillPolygon( hexagon );
		}
		else if( isAdjacent ){
			g.setColor( new Color( 128, 255, 0 ) ); //Lime
			g.fillPolygon( hexagon );
		}
		else{
			g.setColor( new Color( 224, 224, 224 ) ); //Silver
			g.fillPolygon( hexagon );
		}


		if( get_Hexagon().hexagonNature() == 2 ){
			g.setColor( RAINBOW[get_Hexagon().get_direction()] );
		}
		else{
			g.setColor( Color.BLACK );
		}
		g.drawPolygon(hexagon);
	}

	@Override protected void paintBorder( Graphics g ){
		// do not paint a border
	}
}