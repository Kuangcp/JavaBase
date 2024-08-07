package com.github.kuangcp.random;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2020-03-19 18:09
 */
public class MT19937Random {

  /**
   * Auto-generated serial version UID. Note that MTRandomTest does NOT support serialisation of its
   * internal state and it may even be necessary to implement read/write methods to re-seed it
   * properly. This is only here to make Eclipse shut up about it being missing.
   */
  // Constants used in the original C implementation
  private final static int UPPER_MASK = 0x80000000;
  private final static int LOWER_MASK = 0x7fffffff;
  private final static int N = 624;
  private final static int M = 397;
  private final static int[] MAGIC = {0x0, 0x9908b0df};
  private final static int MAGIC_FACTOR1 = 1812433253;
  private final static int MAGIC_FACTOR2 = 1664525;
  private final static int MAGIC_FACTOR3 = 1566083941;
  private final static int MAGIC_MASK1 = 0x9d2c5680;
  private final static int MAGIC_MASK2 = 0xefc60000;
  private final static int MAGIC_SEED = 19650218;

  // Internal state
  private transient int[] mt;
  private transient int mti;
  private transient boolean compact = false;

  // Temporary buffer used during setSeed(long)
  private transient int[] ibuf;

  public MT19937Random(long seed) {
    setSeed(seed);
  }


  /**
   * Initializes mt[N] with a simple integer seed. This method is required as part of the Mersenne
   * Twister algorithm but need not be made public.
   */
  private void setSeed(int seed) {

    // Annoying runtime check for initialisation of internal data
    // caused by java.util.Random invoking setSeed() during init.
    // This is unavoidable because no fields in our instance will
    // have been initialised at this point, not even if the code
    // were placed at the declaration of the member variable.
    if (mt == null) {
      mt = new int[N];
    }

    // ---- Begin Mersenne Twister Algorithm ----
    mt[0] = seed;
    for (mti = 1; mti < N; mti++) {
      mt[mti] = (MAGIC_FACTOR1 * (mt[mti - 1] ^ (mt[mti - 1] >>> 30)) + mti);
    }
    // ---- End Mersenne Twister Algorithm ----
  }

  /**
   * This method resets the state of this instance using the 64 bits of seed data provided. Note
   * that if the same seed data is passed to two different instances of MTRandomTest (both of which
   * share the same compatibility state) then the sequence of numbers generated by both instances
   * will be identical.
   * <p/>
   * If this instance was initialised in 'compatibility' mode then this method will only use the
   * lower 32 bits of any seed value passed in and will match the behaviour of the original C code
   * exactly with respect to state initialisation.
   *
   * @param seed The 64 bit value used to initialise the random number generator state.
   */
  public final synchronized void setSeed(long seed) {
    if (compact) {
      setSeed((int) seed);
    } else {
      // Annoying runtime check for initialisation of internal data
      // caused by java.util.Random invoking setSeed() during init.
      // This is unavoidable because no fields in our instance will
      // have been initialised at this point, not even if the code
      // were placed at the declaration of the member variable.
      if (ibuf == null) {
        ibuf = new int[2];
      }

      ibuf[0] = (int) seed;
      ibuf[1] = (int) (seed >>> 32);
      setSeed(ibuf);
    }
  }

  /**
   * This method resets the state of this instance using the integer array of seed data provided.
   * This is the canonical way of resetting the pseudo random number sequence.
   *
   * @param buf The non-empty integer array of seed information.
   * @throws NullPointerException if the buffer is null.
   * @throws IllegalArgumentException if the buffer has zero length.
   */
  public final synchronized void setSeed(int[] buf) {
    int length = buf.length;

    if (length == 0) {
      throw new IllegalArgumentException("Seed buffer may not be empty");
    }

    // ---- Begin Mersenne Twister Algorithm ----
    int i = 1, j = 0, k = (Math.max(N, length));
    setSeed(MAGIC_SEED);

    for (; k > 0; k--) {
      mt[i] = (mt[i] ^ ((mt[i - 1] ^ (mt[i - 1] >>> 30)) * MAGIC_FACTOR2)) + buf[j] + j;
      i++;
      j++;

      if (i >= N) {
        mt[0] = mt[N - 1];
        i = 1;
      }

      if (j >= length) {
        j = 0;
      }
    }

    for (k = N - 1; k > 0; k--) {
      mt[i] = (mt[i] ^ ((mt[i - 1] ^ (mt[i - 1] >>> 30)) * MAGIC_FACTOR3)) - i;
      i++;
      if (i >= N) {
        mt[0] = mt[N - 1];
        i = 1;
      }
    }

    mt[0] = UPPER_MASK;
    // ---- End Mersenne Twister Algorithm ----
  }

  /**
   * This method forms the basis for generating a pseudo random number sequence from this class. If
   * given a value of 32, this method behaves identically to the genrand_int32 function in the
   * original C code and ensures that using the standard nextInt() function (inherited from Random)
   * we are able to replicate behaviour exactly.
   * <p/>
   * Note that where the number of bits requested is not equal to 32 then bits will simply be masked
   * out from the top of the returned integer value. That is to say that:
   * <p/>
   *
   * <pre>
   * mt.setSeed(12345);
   * int foo = mt.nextInt(16) + (mt.nextInt(16) &lt;&lt; 16);
   * </pre>
   * <p>
   * <p/>
   * will not give the same result as
   * <p/>
   *
   * <pre>
   * mt.setSeed(12345);
   * int foo = mt.next(32);
   * </pre>
   * <p>
   * The number of significant bits desired in the output.
   *
   * @return The next value in the pseudo random sequence with the specified number of bits in the
   * lower part of the integer.
   */
  public synchronized int next() {
    return next(32);
  }

  public synchronized int next(int bits) {
    // ---- Begin Mersenne Twister Algorithm ----
    int y, kk;

    if (mti >= N) {
      // generate N words at one time
      // In the original C implementation, mti is checked here
      // to determine if initialisation has occurred; if not
      // it initialises this instance with DEFAULT_SEED (5489).
      // This is no longer necessary as initialisation of the
      // Java instance must result in initialisation occurring
      // Use the constructor MTRandomTest(true) to enable backwards
      // compatible behaviour.

      for (kk = 0; kk < N - M; kk++) {
        y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
        mt[kk] = mt[kk + M] ^ (y >>> 1) ^ MAGIC[y & 0x1];
      }

      for (; kk < N - 1; kk++) {
        y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
        mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ MAGIC[y & 0x1];
      }

      y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
      mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ MAGIC[y & 0x1];

      mti = 0;
    }

    y = mt[mti++];

    // Tempering
    y ^= (y >>> 11);
    y ^= (y << 7) & MAGIC_MASK1;
    y ^= (y << 15) & MAGIC_MASK2;
    y ^= (y >>> 18);
    // ---- End Mersenne Twister Algorithm ----
    return Math.abs((y >>> (32 - bits)));
  }
}
