package com.github.kuangcp.key;

import java.math.BigInteger;

/**
 * @author kuangcp on 3/31/19-2:53 PM
 */
class RSAEncoder {

  private final BigInteger privateKey;
  private final BigInteger pubKey;
  private final int c;
  private final int d;
  private int e = 0;
  private final BigInteger f;
  private final boolean g;

  RSAEncoder(BigInteger privateKey, BigInteger pubKey, int len, boolean newLine) {
    this.privateKey = privateKey;
    this.pubKey = pubKey;
    this.g = newLine;
    int privKeyLen = privateKey.bitLength();
    this.f = new BigInteger(String.valueOf(len));
    int i = (int) Math.ceil(privKeyLen / Math.log(len) * Math.log(2.0D));
    if (i % 5 != 0) {
      i = (i / 5 + 1) * 5;
    }
    this.d = i;
    this.c = (privKeyLen / 8 - 1);
  }

  String encode(byte[] bytes) {
    int i = bytes.length % this.c;
    byte[] arrayOfByte = new byte[i == 0 ? bytes.length : bytes.length + this.c - i];
    System.arraycopy(bytes, 0, arrayOfByte, this.c - i, bytes.length);

    StringBuffer stringBuffer = new StringBuffer();
    for (int j = 0; j < arrayOfByte.length; j += this.c) {
      encode(arrayOfByte, stringBuffer, j, this.c);
    }
    return stringBuffer.toString();
  }

  private void encode(byte[] bytes, StringBuffer stringBuffer, int off, int len) {
    if (len == 0) {
      return;
    }
    byte[] arrayOfByte = new byte[this.c];
    System.arraycopy(bytes, off, arrayOfByte, 0, len);
    BigInteger localBigInteger1 = new BigInteger(1, arrayOfByte);
    if (localBigInteger1.compareTo(this.pubKey) >= 0) {
      throw new IllegalArgumentException("result is too long");
    }
    BigInteger localBigInteger2 = localBigInteger1.modPow(this.privateKey, this.pubKey);
    stringBuffer.append(a(a(localBigInteger2)));
  }

  private String a(String paramString) {
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < paramString.length(); i++) {
      a(localStringBuffer);
      localStringBuffer.append(paramString.charAt(i));
    }
    return localStringBuffer.toString();
  }

  private String a(BigInteger paramBigInteger) {
    StringBuilder localStringBuffer = new StringBuilder();
    for (int i = 0; i < this.d; i++) {
      localStringBuffer.insert(0, b(paramBigInteger.mod(this.f)));
      paramBigInteger = paramBigInteger.divide(this.f);
    }
    return localStringBuffer.toString();
  }

  private void a(StringBuffer paramStringBuffer) {
    if ((this.e > 0) && (this.e % 5 == 0)) {
      if (this.e % 30 == 0) {
        paramStringBuffer.append('\n');
      } else if (this.g) {
        paramStringBuffer.append('-');
      }
    }
    this.e += 1;
  }

  private static char b(BigInteger paramBigInteger) {
    int value = paramBigInteger.intValue();

    if (value < 10) {
      return (char) (48 + value);
    }

    if (value < 36) {
      return (char) (65 + value - 10);
    }

    if (value < 62) {
      return (char) (97 + value - 36);
    }

    return (char) (33 + value - 62);
  }
}

