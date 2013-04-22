package dk.aau.cs.d402f13.utilities.types;

public class Square implements Cloneable  {
	public String getImgPath(){ return ""; }

    public Square clone() throws CloneNotSupportedException {
        return (Square) super.clone();
    }
}
