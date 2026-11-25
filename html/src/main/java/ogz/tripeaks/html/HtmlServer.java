package ogz.tripeaks.html;

import com.github.xpenatan.gdx.backends.teavm.gen.SkipClass;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * 启动服务
 * */
@SkipClass
public final class HtmlServer {
    public static void main(String[] args) throws Exception {
        Path root = Path.of(args[0]).toRealPath();
        int port = Integer.parseInt(args[1]);
        Map<String, String> types = Map.of(
                "html", "text/html; charset=utf-8", "js", "text/javascript; charset=utf-8",
                "css", "text/css; charset=utf-8", "png", "image/png",
                "txt", "text/plain; charset=utf-8", "wasm", "application/wasm");
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
        server.createContext("/", exchange -> {
            try {
                if (!exchange.getRequestMethod().equals("GET")) {
                    exchange.sendResponseHeaders(405, -1);
                    return;
                }
                String url = exchange.getRequestURI().getPath();
                Path file = root.resolve(url.equals("/") ? "index.html" : url.substring(1)).normalize();
                if (!file.startsWith(root) || !Files.isRegularFile(file)
                        || !file.toRealPath().startsWith(root)) {
                    exchange.sendResponseHeaders(404, -1);
                    return;
                }
                String name = file.getFileName().toString();
                String extension = name.substring(name.lastIndexOf('.') + 1);
                exchange.getResponseHeaders().set("Content-Type",
                        types.getOrDefault(extension, "application/octet-stream"));
                exchange.sendResponseHeaders(200, Files.size(file));
                Files.copy(file, exchange.getResponseBody());
            } finally {
                exchange.close();
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread(() -> server.stop(0)));
        server.start();
        System.out.println("TriPeaks: http://localhost:" + port + "/ (Ctrl+C to stop)");
    }
}
