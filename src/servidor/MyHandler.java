package servidor;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class MyHandler implements HttpHandler {

    private final File root;

    public MyHandler() {
        root = new File("html");
        if (!root.exists()) {
            throw new RuntimeException("Pasta html não encontrada!");
        }
        System.out.println("HTMLs em: " + root.getAbsolutePath());
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("GET")) {
            String req = exchange.getRequestURI().getPath();
            System.out.println("Pagina requisitada: " + req);

            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "text/html;charset=UTF-8");

            File pag;
            if (req.equals("/")) {
                pag = new File(root, "index.html");
            } else {
                pag = new File(root, req + ".html");
            }
            if (!pag.exists()) {
                exchange.sendResponseHeaders(404, 0);
                try (OutputStream responseBody = exchange.getResponseBody()) {
                    responseBody.write("Página não encontrada!".getBytes());
                }
            } else {
                exchange.sendResponseHeaders(200, 0);
                try (OutputStream responseBody = exchange.getResponseBody()) {
                    responseBody.write(getHtml(pag).getBytes());
                }
            }
        }
    }

    public static String getHtml(File f) throws FileNotFoundException, IOException {
        BufferedReader reader = null;
        FileInputStream fis;
        fis = new FileInputStream(f);
        reader = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));

        StringBuilder sb = new StringBuilder();
        String str;
        while ((str = reader.readLine()) != null) {
            sb.append(str);
        }
        fis.close();
        reader.close();
        return sb.toString().trim();
    }
}
