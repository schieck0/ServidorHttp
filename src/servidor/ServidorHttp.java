package servidor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

public class ServidorHttp {

    public static void main(String[] args) throws IOException {

        InetSocketAddress addr = new InetSocketAddress(80);
        HttpServer server = HttpServer.create(addr, 0);

        server.createContext("/", new MyHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("Servidor iniciado na porta 80. Acesse pelo navegador em: http://localhost");
    }
}

