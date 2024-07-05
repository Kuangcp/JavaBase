package syntax.bit;

import org.junit.Test;
import org.roaringbitmap.longlong.Roaring64NavigableMap;

/**
 * https://www.cnblogs.com/yougewe/p/16390651.html
 * @author Kuangcp
 * 2024-03-13 18:49
 */
public class RoaringBitmapTest {

    @Test
    public void testRoaringBitmap() {
        Roaring64NavigableMap bitmapObj = new Roaring64NavigableMap();
        bitmapObj.add(5L);
        boolean exists = bitmapObj.contains(5);
        long eleSize = bitmapObj.getLongCardinality();
        System.out.println("exits:" + exists + ", eleSize:" + eleSize);
    }
}
