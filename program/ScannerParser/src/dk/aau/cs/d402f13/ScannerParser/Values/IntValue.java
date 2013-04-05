package dk.aau.cs.d402f13.ScannerParser.Values;

import dk.aau.cs.d402f13.ScannerParser.ScannerParser.Type;

public class IntValue extends Value {
  private final int value;

  public IntValue(int value) {
    this.value = value;
  }
  
  public int getValue() {
    return this.value;
  }
  
  @Override
  public String toString() {
    return "" + this.value;
  }

  @Override
  public Type getType() {
    return Type.INT;
  }
}
