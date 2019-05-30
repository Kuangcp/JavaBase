package scattered;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import lombok.AllArgsConstructor;
import lombok.Data;
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
 * TODO should use two stack one record over num the other record less num
 * in short, minimize array data range
 * output:
 * like this: 4, 4, 4, 4, 4
 */
@Slf4j
public class SmoothingTheData {

  private int dataSize = 3;

  // TODO record has error
  // start -> end
  private List<MoveRecord> movingRecord = new ArrayList<>();

  @Data
  @AllArgsConstructor
  private class MoveRecord {

    private int startIndex;
    private int targetIndex;
    private int weight;
  }

  @Data
  @AllArgsConstructor
  private class DataUnit {

    private int index;
    private int delta;
  }

  private Integer[] providerData() {
    Integer[] allData = new Integer[dataSize];
    for (int i = 0; i < dataSize; i++) {
      allData[i] = (int) (Math.random() * dataSize * 10);
    }

    return allData;
  }

  private List<Integer> normalizeByLinked(Integer[] originData) {
    List<Integer> result = new ArrayList<>(dataSize);
    List<Integer> list = new LinkedList<>(Arrays.asList(originData).subList(0, dataSize));
    list.sort(Integer::compareTo);
    list.sort(Comparator.reverseOrder());
    int sum = list.stream().mapToInt(value -> value).sum();
    int average = sum / dataSize;
    log.debug("generate array: sum={} average={}", sum, average);

    Stack<DataUnit> stack = new Stack<>();

    for (int i = 0; i < list.size(); i++) {
      Integer number = list.get(i);
      if (number >= average) {
        if (number > average) {
          log.debug("push start={}", i);
          stack.push(new DataUnit(i, number - average));
        }
        result.add(i, average);
        continue;
      }

      int deltaNum = average - number;
      DataUnit nextDataUnit = null;
      while (deltaNum > 0) {
        if (stack.isEmpty()) {
          log.debug("stack is empty, exit");
          return result;
        }
        nextDataUnit = stack.pop();
        deltaNum -= nextDataUnit.getDelta();
//        log.debug(">>>  deltaNum={} delta={}", deltaNum, nextDataUnit.getDelta());
        if (deltaNum >= 0) {
          movingRecord.add(new MoveRecord(nextDataUnit.getIndex(), i, nextDataUnit.getDelta()));
        } else {
          movingRecord
              .add(new MoveRecord(nextDataUnit.getIndex(), i, deltaNum + nextDataUnit.getDelta()));
        }
      }
      if (Objects.isNull(nextDataUnit)) {
        return result;
      }
      if (deltaNum < 0) {
        deltaNum *= -1;
        nextDataUnit.setDelta(deltaNum);
        log.debug("push start={}", nextDataUnit.getIndex());
        stack.push(nextDataUnit);
      }
      if (deltaNum != 0) {
        movingRecord.add(new MoveRecord(nextDataUnit.getIndex(), i, deltaNum));
      }
      result.add(i, average);
    }
    if (!stack.isEmpty()) {
      int index = 0;
      DataUnit dataUnit = stack.pop();
      int delta = dataUnit.getDelta();
      while (delta > 0) {
        result.set(index, average + 1);
        movingRecord.add(new MoveRecord(dataUnit.getIndex(), index, 1));
        delta--;
        index++;
      }
    }
    return result;
  }

  @Test
  public void testNormalize() {

    Integer[] integers = providerData();
    for (int i = 0; i < integers.length; i++) {
      log.debug("origin data: index={} value={}", i, integers[i]);
    }

    List<Integer> result = normalizeByLinked(integers);
    result.forEach(num -> log.debug("single: num={}", num));
    movingRecord.forEach(record -> log.debug("record: start={} target={} weight={}",
        record.getStartIndex(), record.getTargetIndex(), record.getWeight()));
  }
}
