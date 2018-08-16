package scattered;


import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * @author kuangcp on 18-8-16-下午8:57
 * smoothing the data,
 *
 * input :
 * integer array, like this: 2, 4, 6, 1, 7
 *
 * make all the integer diffrent is minimized, and record moving action(conclude index)
 * like this: 7(5) -> 2(0) : 2 then array turn : 4, 4, 6, 1, 5
 *
 * in short, minimize array data range
 * output:
 * like this: 4, 4, 4, 4, 4
 */
@Slf4j
public class SmoothingTheData {

  private int dataSize = 10;


  private Integer[] providerData() {
    Integer[] allData = new Integer[dataSize];
    for (int i = 0; i < dataSize; i++) {
      allData[i] = (int) (Math.random() * dataSize * 10);
    }

    return allData;
  }

  private Integer[] normalize(Integer[] originData) {
    return originData;
  }

  @Test
  public void testNormalize() {

    Integer[] integers = providerData();

    Integer[] result = normalize(integers);

    for (Integer integer : result) {
      
    }
  }
}
