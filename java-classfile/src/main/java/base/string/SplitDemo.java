package base.string;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 * @date 18-5-21  下午2:42
 */
public class SplitDemo {

    // 截取最后出现的字符
    public String removeChar(String target, String index){
        return target.substring(0, target.indexOf(index));
    }
    // 去除最后一个字符
    public String removeLast(String target){
        return target.substring(0, target.length()-1);
    }
}
