 
import java.awt.Color;


public class TextStyle {
	
	private Boolean big;
	private Boolean italic;
	private int textSize;
	private Color forgroundColor;
	private Color backgroundColor;
	
	public TextStyle(Boolean big, Boolean italic, int textSize,
			Color forgroundColor, Color backgroundColor) {
		super();
		this.big = big;
		this.italic = italic;
		this.textSize = textSize;
		this.forgroundColor = forgroundColor;
		this.backgroundColor = backgroundColor;
	}
	
	
	public Boolean getBig() {
		return big;
	}
	public void setBig(Boolean big) {
		this.big = big;
	}
	public Boolean getItalic() {
		return italic;
	}
	public void setItalic(Boolean italic) {
		this.italic = italic;
	}
	public int getTextSize() {
		return textSize;
	}
	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}
	public Color getForgroundColor() {
		return forgroundColor;
	}
	public void setForgroundColor(Color forgroundColor) {
		this.forgroundColor = forgroundColor;
	}
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	
	
}
