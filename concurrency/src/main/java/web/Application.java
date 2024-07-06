package web;


import com.hellokaton.blade.Blade;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-05 00:18
 */
public class Application {

    public static void main(String[] args) {
        Blade.create()
                .listen(32993)
                .get("/", ctx -> ctx.text("Hello Blade"))

                .start(Application.class, args);
    }
}
