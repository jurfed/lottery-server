package ticketfactory;

import jnr.ffi.LibraryLoader;
import jnr.ffi.Pointer;
import jnr.ffi.provider.FFIProvider;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;

public class TicketGenerator {

    protected final ITicketFactoryMathModel mathModel;
    protected Pointer pointer;

    public TicketGenerator() {
        LibraryLoader<ITicketFactoryMathModel> loader = FFIProvider
                .getSystemProvider()
                .createLibraryLoader(ITicketFactoryMathModel.class)
                .search("lib")
                .search("/usr/local/lib")
                .failImmediately();

        this.mathModel = loader.load("EIL.TF.SimulationTool");
        pointer = this.mathModel.createMathModel();
        this.mathModel.getTickets(pointer, new byte[0], 0, new byte[0], 0,0,0);
        this.mathModel.getRandomTicket(pointer, new byte[0], 0, new byte[0], 0, 0);
        this.mathModel.configureGame(pointer, new byte[0], 0, new byte[0], java.nio.ByteBuffer.allocateDirect(4 * 10).asIntBuffer());
        this.mathModel.getGamesInfo(pointer, new byte[0], 0);
        this.mathModel.destroyMath(pointer);
    }
}
