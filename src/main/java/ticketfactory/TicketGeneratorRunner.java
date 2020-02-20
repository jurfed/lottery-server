package ticketfactory;

import com.google.gson.Gson;
import jnr.ffi.LibraryLoader;
import jnr.ffi.Pointer;
import jnr.ffi.provider.FFIProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.IntBuffer;
import java.util.*;

public class TicketGeneratorRunner {


    private static TicketGeneratorRunner ticketGeneratorRunner;

    private final Gson gson = new Gson();

    private static final String LIBRARY_NAME = "EIL.TF.SimulationTool";
    private static String OS = System.getProperty("os.name").toLowerCase();
    ITicketFactoryMathModel mathFacadeLib;
    Pointer pointer;


    private TicketGeneratorRunner() {
    }

    public static TicketGeneratorRunner getInstance() {
        if (ticketGeneratorRunner == null) {
            ticketGeneratorRunner = new TicketGeneratorRunner();
            ticketGeneratorRunner.init();
        }
        return ticketGeneratorRunner;
    }

    public void init() {

        String path = this.getClass().getClassLoader().getResource("lib").getPath();
        File fXmlFile;
        if (OS.contains("win")) {
            fXmlFile = new File(path + "\\");
        } else {
            fXmlFile = new File(path);
        }


        FFIProvider provider = FFIProvider.getSystemProvider();
        LibraryLoader<ITicketFactoryMathModel> factory = provider.createLibraryLoader(ITicketFactoryMathModel.class);
        LibraryLoader<ITicketFactoryMathModel> search = factory.search(path);
        LibraryLoader<ITicketFactoryMathModel> fail = search.failImmediately();
        LibraryLoader<ITicketFactoryMathModel> loader = fail;
        this.mathFacadeLib = loader.load(LIBRARY_NAME);
        this.pointer = this.mathFacadeLib.createMathModel();
    }

    public String generateTickets(String gameName, int bet, int prize, int count) {
        byte[] bytes = new byte[100110000];
        int responseSize = this.mathFacadeLib.getTickets(pointer, bytes, 0, gameName.getBytes(), bet, prize, count);
        String xml = new String(bytes, 0, responseSize);
        return xml;
    }

    public String getRandomTicket(String gameName, int bet, int prize) {
        byte[] bytes = new byte[1000];
        int responseSize = this.mathFacadeLib.getRandomTicket(pointer, bytes, 0, gameName.getBytes(), bet, prize);
        String xml = new String(bytes, 0, responseSize);
        //System.err.println(xml);
        return xml;
    }

    public String configureGame(String gameName, List<Integer> list) throws IOException {
        byte[] bytes = new byte[10000];


        List bet = new ArrayList();
        List bets = new ArrayList();
        bets.add(100);
        bets.add(200);

        Map map = new HashMap();
        map.put(100,bets);
        bet.add(map);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(bet);
        byte[] bytes1 = bos.toByteArray();

        int size = 10;
        IntBuffer allocateDirect = java.nio.ByteBuffer.allocateDirect(4 * size).asIntBuffer();
        for (int x = 0; x < size; ++x)
        {
            int v = allocateDirect.get(x);
            allocateDirect.put(x, v + 1);
        }


        int responseSize = this.mathFacadeLib.configureGame(pointer, bytes, 0, "Black".getBytes(), allocateDirect);
        String xml = new String(bytes, 0, responseSize);
//        System.err.println(xml);
        return xml;
    }

    public String getGamesInfo() {
        byte[] bytes = new byte[100110000];
        int responseSize = this.mathFacadeLib.getGamesInfo(pointer, bytes, 0);
        String xml = new String(bytes, 0, responseSize);
        return xml;
    }

    public String destroy() {
        this.mathFacadeLib.destroyMath(this.pointer);
        return "Math destroed";
    }
}
