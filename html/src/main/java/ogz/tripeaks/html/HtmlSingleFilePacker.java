package ogz.tripeaks.html;

import com.github.xpenatan.gdx.backends.teavm.gen.SkipClass;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/** Packages a TeaVM website as an offline HTML file. Runs only on the JVM. */
@SkipClass
public final class HtmlSingleFilePacker {
    public static void main(String[] args) throws IOException {

//        "D:\Android\tripeaks-gdx\html\build\dist\webapp"
//        "D:\Android\tripeaks-gdx\html\build\dist\tripeaks.html"
//        "D:\Android\tripeaks-gdx\html\webapp\single-file-runtime.js"

//        "html/build/dist/webapp" "html/build/dist/tripeaks.html" "html/webapp/single-file-runtime.js"
        Path root = Path.of(args.length > 0 ? args[0] : "html/build/dist/webapp").toRealPath();
        Path output = Path.of(args.length > 1 ? args[1] : "html/build/dist/tripeaks.html").toAbsolutePath();
        String html = Files.readString(root.resolve("index.html"));
        Pattern scriptPattern = Pattern.compile(
                "<script\\b[^>]*\\s+src\\s*=\\s*([\"'])(?:\\./)?teavm/app\\.js\\1[^>]*>\\s*</script\\s*>",
                Pattern.CASE_INSENSITIVE);
        Matcher scriptMatcher = scriptPattern.matcher(html);
        if (!scriptMatcher.find()) {
            throw new IllegalStateException("Cannot find the TeaVM script in index.html.");
        }
        String entry = scriptMatcher.group();

        StringBuilder assets = new StringBuilder("{");
        List<Path> files;
        try (Stream<Path> walk = Files.walk(root)) {
            files = walk.filter(Files::isRegularFile).sorted().toList();
        }
        int count = 0;
        for (Path file : files) {
            String name = root.relativize(file).toString().replace('\\', '/');
            if (!name.startsWith("assets/") && !name.startsWith("scripts/")) continue;
            if (count++ > 0) assets.append(',');
            assets.append('"').append(escape(name)).append("\":[\"")
                    .append(mimeType(name)).append("\",\"")
                    .append(Base64.getEncoder().encodeToString(Files.readAllBytes(file)))
                    .append("\"]");
        }
        assets.append('}');
        String app = Base64.getEncoder().encodeToString(Files.readAllBytes(root.resolve("teavm/app.js")));
        String runtime = Files.readString(Path.of(args.length > 2 ? args[2] : "html/webapp/single-file-runtime.js"));
        runtime = runtime.replace("/*__EMBEDDED_FILES__*/", assets)
                .replace("/*__APPLICATION__*/", '"' + app + '"');
        html = html.replace(entry, "<script>\n" + runtime + "\n</script>");
        // The favicon is the only asset directly referenced by the HTML template.
        String favicon = "href=\"assets/images/dark_title.png\"";
        if (html.contains(favicon)) {
            html = html.replace(favicon, "href=\"data:image/png;base64,"
                    + Base64.getEncoder().encodeToString(Files.readAllBytes(root.resolve("assets/images/dark_title.png"))) + "\"");
        }
        Files.createDirectories(output.getParent());
        Files.writeString(output, html, StandardCharsets.UTF_8);
        System.out.printf("Single HTML: %s%nEmbedded %d files; %.2f MiB. Open directly in a browser.%n",
                output, count + 1, Files.size(output) / 1048576.0);
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\r", "\\r").replace("\n", "\\n").replace("<", "\\u003c");
    }

    private static String mimeType(String path) {
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".js")) return "text/javascript";
        if (path.endsWith(".wasm")) return "application/wasm";
        return "application/octet-stream";
    }
}
