package ticketfactory;


import jnr.ffi.Pointer;

import java.nio.IntBuffer;
import java.util.List;
import java.util.Map;

public interface ITicketFactoryMathModel {
    Pointer createMathModel();
    int getTickets(Pointer var1, byte[] var2, int var3, byte[] var4, int var5, int var6, int var7);
    int getRandomTicket(Pointer var1, byte[] var2, int var3, byte[] var4, int var5, int var6);
    int configureGame(Pointer var1, byte[] var2, int var3, byte[] var4, IntBuffer var5);
    int getGamesInfo(Pointer var1, byte[] var2, int var3);
    void destroyMath(Pointer var1);
}
