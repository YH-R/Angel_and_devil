import java.util.*;
import java.lang.Math;

public class Hexagon{

	private int power = 0;
	private int direction = 0;
	private int deviation = 0;
	private long value;
	private boolean eaten = false;

	public Hexagon(int a, int b, int c){
		if(validHexagon1(a, b, c)){
			power = a;
			direction = b;
			deviation = c;
			value = (long)(Math.pow(2,power) * Math.pow(5,direction) * Math.pow(3,deviation));
		}
		else{
			System.out.println("Invalid hexagon. Printed from first constructor of Hexagon");
		}
	}

	public Hexagon(long d){
		int[] temp = getAllCoordinates( d );
		if( temp[3] == 1 && validHexagon1( temp[0], temp[1], temp[2] ) ){
			power = temp[0];
			direction = temp[1];
			deviation = temp[2];
			value = d;
		}
		else{
			System.out.println( "Invalid hexagon. Printed from second constructor of Hexagon" );
			System.out.println( "Remainder is " + temp[3] );
		}		

	}

	public static int[] getAllCoordinates( long index ){
		int[] array = { 0, 0, 0, 0 };

		while( index%2 == 0 ){
			index /= 2;
			array[0]++;
		}

		while( index%5 == 0 ){
			index /= 5;
			array[1]++;
		}

		while( index%3 == 0 ){
			index /= 3;
			array[2]++;
		}

		array[3] = (int)index;

		return array;
	}

	public int get_power(){
		return power;
	}

	public int get_direction(){
		return direction;
	}

	public int get_deviation(){
		return deviation;
	}

	public long get_value(){
		return value;
	}

	public void set_eaten(boolean b){
		eaten = b;
	}

	public boolean get_eaten(){
		return eaten;
	}

	public boolean validHexagon1(int a, int b, int c){
		if( a == 0 && b == 0 && c == 0 ){
			return true;
		}
		else if( a > 0 && ( b >= 0 && b < 6 ) && ( c >= 0 && c < a ) ){
			return true;
		}
		else{
			System.out.println( "( " + a + ", " + b + ", " + c + ") is not a valid coordinate for a hexagon." );
			return false;
		}
	}

	public boolean validHexagon2(){
		return validHexagon1(power, direction, deviation);
	}

	public int hexagonNature(){ //0-invalid, 1-center, 2-corner, 3-side
		if( power == 0 && direction == 0 && deviation == 0 ){
			return 1;
		}
		else if( power > 0 && ( direction >= 0 && direction < 6 ) && deviation == 0 ){
			return 2;
		}
		else if( power > 0 && ( direction >= 0 && direction < 6 ) && ( deviation > 0 && deviation < power ) ){
			return 3;
		}
		else{
			System.out.println("???");
			return 0;
		}
	}

	public Hexagon[] adjacentHexagons(){
		Hexagon[] h = new Hexagon[6];

		switch(hexagonNature()){
			case 1: // Center
				for(int i = 0; i < 6; i++){
					h[i] = new Hexagon(1,i,0);
				}

				break;
			case 2: // Corner
				h[0] = new Hexagon((power+1),direction,0);
				h[1] = new Hexagon((power+1),direction,1);
				if( power == 1 ){
					h[2] = new Hexagon(power,(direction+1)%6,0);
				}
				else{
					h[2] = new Hexagon(power,direction,1);

				}
				if( power == 1 ){
					h[3] = new Hexagon(0,0,0);
				}
				else{
					h[3] = new Hexagon((power-1),direction,0);
				}
				h[4] = new Hexagon(power,(direction+5)%6,(power-1));
				h[5] = new Hexagon((power+1),(direction+5)%6,power);

				break;
			case 3: //Side
				h[0] = new Hexagon((power+1),direction,deviation);
				h[1] = new Hexagon((power+1),direction,(deviation+1));

				if( power == deviation+1 ){
					h[2] = new Hexagon(power,(direction+1)%6,0);
					h[3] = new Hexagon((power-1),(direction+1)%6,0);
				}
				else{
					h[2] = new Hexagon(power,direction,(deviation+1));
					h[3] = new Hexagon((power-1),direction,deviation);
				}

				h[4] = new Hexagon((power-1),direction,(deviation-1));
				h[5] = new Hexagon(power,direction,deviation-1);

				break;
			default:
				System.out.println("???");
				for(int i = 0; i < 6; i++){
					h[i] = new Hexagon(0,0,0);
				}
		}

		return h;
	}

	public UltimateButton get_button( UltimateButton[][][] blist ){
		return blist[get_power()][get_direction()][get_deviation()];
	}


	public boolean equals(Hexagon h){
		return (get_power() == h.get_power()) && (get_direction() == h.get_direction()) && (get_deviation() == h.get_deviation()) && (get_value() == h.get_value()) && (get_eaten() == h.get_eaten());
	}

	public int row(int size){
		if(size>0){
			switch(direction){
				case 0:
					return (size-power);

				case 1:
					return (size-power+deviation);

				case 2:
					return (size+deviation);

				case 3:
					return (size+power);

				case 4:
					return (size+power-deviation);

				case 5:
					return (size-deviation);

				default:
					return 0;
			}

		}
		else{
			return -1;
		}
	}

	public int yCoor(int size, double factor){
		return (int)Math.ceil((6.0*(double)row(size)-5.0)*factor/4.0);
	}

	public int col(int size){
		if(size>0){
			switch(direction){
				case 0:
					return (2*size-1-power+2*deviation);

				case 1:
					return (2*size-1+power+deviation);

				case 2:
					return (2*size-1+2*power-deviation);

				case 3:
					return (2*size-1+power-2*deviation);

				case 4:
					return (2*size-1-power-deviation);

				case 5:
					return (2*size-1-2*power+deviation);

				default:
					return 0;
			}
		}
		else{
			return -1;
		}
	}

	public int xCoor(int size, double factor){
		return (int)Math.ceil( (2.0*(double)col(size)-1.0)*Math.sqrt(3)*factor/4.0 );
	}

	public long valueOf(){
		return (long)( Math.pow(2,power) * Math.pow(5,direction) * Math.pow(3,deviation));
	}

	public double getR(){
			return Math.sqrt( power*power + deviation*deviation - power*deviation );
	}

	public double getTheta(){
		if( getR() == 0 ){
			return 0;
		}
		else{
			return ( Math.PI/3.0*(double)direction + Math.asin( Math.sqrt(0.75) * (double)deviation / getR() ) );
		}
	}

	public static double disBet2Hex( Hexagon h1, Hexagon h2 ){
		double theta = Math.min( 2*Math.PI-( Math.abs( h1.getTheta()-h2.getTheta() ) ), Math.abs( h1.getTheta()-h2.getTheta() ) );
		return Math.sqrt( h1.getR()*h1.getR() + h2.getR()*h2.getR() - 2*h1.getR()*h2.getR()*Math.cos(theta) );
	}

	public static int[] getACfromD( double d ){
		final double EPSILON = 0.00000001;
		int[] returnArray = {0,0};
		boolean b = false; //true means break out of outer loop

		for( int a = (int)Math.ceil(d); a <= Math.floor( Math.sqrt(4.0/3.0)*d ); a++ ){
			for( int c = 0; c < a; c++ ){
				if( Math.abs( d - Math.sqrt( a*a + c*c - a*c ) ) < EPSILON ){
					returnArray[0] = a;
					returnArray[1] = c;
					b = true;
					break;
				}
			}
			if(b){
				break;
			}
		}

		return returnArray;
	}

	public String toString(){
		return ("("+power+"_"+direction+"_"+deviation+")");
	}
}