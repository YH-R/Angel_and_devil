import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Manager{
	private AngelMenuBar amb;
	private Board b;
	private BottomPanel bp;

	public Manager(){}

	public Manager( AngelMenuBar amb, Board b, BottomPanel bp ){
		this.amb = amb;
		this.b = b;
		this.bp = bp;
	}

	public AngelMenuBar getAngelMenuBar(){
		return amb;
	}

	public Board getBoard(){
		return b;
	}

	public BottomPanel getBottomPanel(){
		return bp;
	}

	public void setAngelMenuBar( AngelMenuBar amb ){
		this.amb = amb;
	}

	public void setBoard( Board b ){
		this.b = b;
	}

	public void setBottomPanel( BottomPanel bp ){
		this.bp = bp;
	}
}