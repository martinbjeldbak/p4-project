/** {@inheritDoc}  */
@Override
public Value subtract(Value other) throws StandardError {
  if(other.is(CoordValue.type())) {
    CoordValue oCoord = (CoordValue)other.as(CoordValue.type());
    return new DirValue(x - oCoord.getX(), y - oCoord.getY());
  }
  else if(other.is(DirValue.type())) {
    DirValue oDir = (DirValue)other;
    return new CoordValue(x - oDir.getX(), y - oDir.getY());
  }
  throw new TypeError("Cannot subtract a " + other + " from a coordinate");
}
