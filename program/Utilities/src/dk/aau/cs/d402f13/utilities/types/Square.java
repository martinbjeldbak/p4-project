package dk.aau.cs.d402f13.utilities.types;

public class Square implements Cloneable  {
	private String imgPath = null;

	public Square( Square s ){
		this.imgPath = s.imgPath;
	}
	public Square(){
	}
	
	public String getImgPath() {
		return imgPath;
	}
	
	public void setImgPath(String value) {
		imgPath = value;
	}

    public Square clone() throws CloneNotSupportedException {
        return (Square) super.clone();
    }
}
