package io.github.xenogew.propedit.eclipse.plugin.analyze;

public record PropertyElement(int keyStartPos,int keyLength,int valueStartPos,int valueLength,String key,String value,int elementStartLine,int elementLineCount){

public static final class Builder {
  private int keyStartPos;
  private int keyLength;
  private int valueStartPos;
  private int valueLength;
  private String key;
  private String value;
  private int elementStartLine;
  private int elementLineCount;

  private Builder() {}

  public Builder keyStartPos(int keyStartPos) {
    this.keyStartPos = keyStartPos;
    return this;
  }

  public Builder keyLength(int keyLength) {
    this.keyLength = keyLength;
    return this;
  }

  public Builder valueStartPos(int valueStartPos) {
    this.valueStartPos = valueStartPos;
    return this;
  }

  public Builder valueLength(int valueLength) {
    this.valueLength = valueLength;
    return this;
  }

  public Builder key(String key) {
    this.key = key;
    return this;
  }

  public Builder value(String value) {
    this.value = value;
    return this;
  }

  public Builder elementStartLine(int elementStartLine) {
    this.elementStartLine = elementStartLine;
    return this;
  }

  public Builder elementLineCount(int elementLineCount) {
    this.elementLineCount = elementLineCount;
    return this;
  }

  public PropertyElement build() {
    return new PropertyElement(this.keyStartPos, this.keyLength, this.valueStartPos,
        this.valueLength, this.key, this.value, this.elementStartLine, this.elementLineCount);
  }

  }

  public static Builder builder() {
    return new Builder();
  }
}
