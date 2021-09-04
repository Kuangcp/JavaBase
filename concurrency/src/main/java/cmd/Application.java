package cmd;

import com.blade.Blade;

/**
 * @author https://github.com/kuangcp on 2021-09-05 00:18
 */
public class Application {

    public static void main(String[] args) {
        Blade.of().get("/", ctx -> ctx.text("Hello Blade")).start();
    }
}
