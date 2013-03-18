public Token scan() throws Exception {
  while (isWhitespace()) {
    pop();
  }
  if (isEof()) {
    return token(Type.EOF);
  }
  if (isDigit()) {
    return scanNumeric();
  }
  if (isUppercase()) {
    return scanUppercase();
  }
  if (isOperator()) {
    return scanOperator();
  }
  if (isLowercase()) {
    return scanKeyword();
  }
  if (current() == '"') {
    return scanString();
  }
  if (current() == '$') {
    return scanVar();
  }
  throw new ScannerError("Unidentified character: " + current(), token(Type.EOF));
}