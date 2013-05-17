package dk.aau.cs.d402f13.utilities;

public class SimpleDir {
  public int x, y;
  public SimpleDir(int x, int y){
    this.x = x;
    this.y = y;
  }
  public SimpleDir add(SimpleDir val){
    return new SimpleDir(this.x + val.x, this.y + val.y);
  }
  public SimpleDir clone(){
    return new SimpleDir(this.x, this.y);
  }
}
