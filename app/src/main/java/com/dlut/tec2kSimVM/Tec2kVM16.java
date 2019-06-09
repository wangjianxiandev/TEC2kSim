package com.dlut.tec2kSimVM;

import android.os.Handler;
import android.os.Message;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Tec2kVM16 {
    public final static short VM16_FLAG_BIT_CARRY = (short) 0x8000;
    public final static short VM16_FLAG_BIT_ZERO = 0x4000;
    public final static short VM16_FLAG_BIT_OVER = 0x2000;
    public final static short VM16_FLAG_BIT_SIGN = 0x1000;

    public final static int VM16_REGISTER_INDEX_SP = 4;
    public final static int VM16_REGISTER_INDEX_PC = 5;

    public final static int VM16_SEND_DATA_START_ADDRESS = 0x2600;
    public final static int VM16_SEND_DATA_LENGTH = 0x2601;

    static InstructionInfo16[] InstructionList = {
            new InstructionInfo16("add", (byte) 0x00, 1, "ExecuteADD"),
            new InstructionInfo16("sub", (byte) 0x01, 1, "ExecuteSUB"),
            new InstructionInfo16("and", (byte) 0x02, 1, "ExecuteAND"),
            new InstructionInfo16("cmp", (byte) 0x03, 1, "ExecuteCMP"),
            new InstructionInfo16("xor", (byte) 0x04, 1, "ExecuteXOR"),
            new InstructionInfo16("test", (byte) 0x05, 1, "ExecuteTEST"),
            new InstructionInfo16("or", (byte) 0x06, 1, "ExecuteOR"),
            new InstructionInfo16("mvrr", (byte) 0x07, 1, "ExecuteMVRR"),
            new InstructionInfo16("dec", (byte) 0x08, 4, "ExecuteDEC"),
            new InstructionInfo16("inc", (byte) 0x09, 4, "ExecuteINC"),
            new InstructionInfo16("shl", (byte) 0x0a, 4, "ExecuteSHL"),
            new InstructionInfo16("shr", (byte) 0x0b, 4, "ExecuteSHR"),
            new InstructionInfo16("jr", (byte) 0x41, 8, "ExecuteJR"),
            new InstructionInfo16("jrc", (byte) 0x44, 8, "ExecuteJRC"),
            new InstructionInfo16("jrnc", (byte) 0x45, 8, "ExecuteJRNC"),
            new InstructionInfo16("jrz", (byte) 0x46, 8, "ExecuteJRZ"),
            new InstructionInfo16("jrnz", (byte) 0x47, 8, "ExecuteJRNZ"),
            new InstructionInfo16("jmpa", (byte) 0x80, 9, "ExecuteJMPA"),
            new InstructionInfo16("ldrr", (byte) 0x81, 1, "ExecuteLDRR"),
            new InstructionInfo16("in", (byte) 0x82, 2, "ExecuteIN"),
            new InstructionInfo16("strr", (byte) 0x83, 1, "ExecuteSTRR"),
            new InstructionInfo16("pshf", (byte) 0x84, 5, "ExecutePSHF"),
            new InstructionInfo16("push", (byte) 0x85, 3, "ExecutePUSH"),
            new InstructionInfo16("out", (byte) 0x86, 2, "ExecuteOUT"),
            new InstructionInfo16("pop", (byte) 0x87, 4, "ExecutePOP"),
            new InstructionInfo16("mvrd", (byte) 0x88, 6, "ExecuteMVRD"),
            new InstructionInfo16("popf", (byte) 0x8c, 5, "ExecutePOPF"),
            new InstructionInfo16("ret", (byte) 0x8f, 5, "ExecuteRET"),
            new InstructionInfo16("cala", (byte) 0xce, 9, "ExecuteCALA"),
            new InstructionInfo16("adc", (byte) 0x20, 1, "ExecuteADC"),
            new InstructionInfo16("sbb", (byte) 0x21, 1, "ExecuteSBB"),
            new InstructionInfo16("rcl", (byte) 0x2a, 4, "ExecuteRCL"),
            new InstructionInfo16("rcr", (byte) 0x2b, 4, "ExecuteRCR"),
            new InstructionInfo16("asr", (byte) 0x2c, 4, "ExecuteASR"),
            new InstructionInfo16("not", (byte) 0x2d, 4, "ExecuteNOT"),
            new InstructionInfo16("jmpr", (byte) 0x60, 3, "ExecuteJMPR"),
            new InstructionInfo16("jrs", (byte) 0x64, 8, "ExecuteJRS"),
            new InstructionInfo16("jrns", (byte) 0x65, 8, "ExecuteJRNS"),
            new InstructionInfo16("clc", (byte) 0x6c, 5, "ExecuteCLC"),
            new InstructionInfo16("stc", (byte) 0x6d, 5, "ExecuteSTC"),
            new InstructionInfo16("ei", (byte) 0x6e, 5, "ExecuteEI"),
            new InstructionInfo16("di", (byte) 0x6f, 5, "ExecuteDI"),
            new InstructionInfo16("calr", (byte) 0xe0, 3, "ExecuteCALR"),
            new InstructionInfo16("ldra", (byte) 0xe4, 6, "ExecuteLDRA"),
            new InstructionInfo16("ldrx", (byte) 0xe5, 7, "ExecuteLDRX"),
            new InstructionInfo16("strx", (byte) 0xe6, 7, "ExecuteSTRX"),
            new InstructionInfo16("stra", (byte) 0xe7, 6, "ExecuteSTRA"),
            new InstructionInfo16("iret", (byte) 0xef, 5, "ExecuteIRET"),
            new InstructionInfo16("halt", (byte) 0xf0, 5, "ExecuteHALT")};

    static InstructionInfo16[] InstructionInfoMap = new InstructionInfo16[256];
    static boolean InfoMapInitialized;
    final static short[] FlagCarryValues = {0, VM16_FLAG_BIT_CARRY};
    final static short[] FlagZeroValues = {0, VM16_FLAG_BIT_ZERO};
    final static short[] FlagOverValues = {0, VM16_FLAG_BIT_OVER};
    final static short[] FlagSignValues = {0, VM16_FLAG_BIT_SIGN};

    short[] Register = new short[16];
    short[] pMemory;
    boolean FlagCarry;
    boolean FlagZero;
    boolean FlagOver;
    boolean FlagSign;
    boolean InterruptEnabled;
    boolean ToEnd;
    int pPC;
    int pSP;
    char CurrentCode;// 低8位寄存器号，高8位OperatorCode
    private StringBuffer outString = new StringBuffer();

    Tec2kVM16(int MemorySize) {
        // 建立内存区域，初始化各指针
        pMemory = new short[MemorySize];
        pPC = VM16_REGISTER_INDEX_PC;
        pSP = VM16_REGISTER_INDEX_SP;

        if (!InfoMapInitialized)
            InitInstructionInfoMap();
    }

    public Tec2kVM16() {
        // 建立内存区域，初始化各指针
        pMemory = new short[65536];
        pPC = VM16_REGISTER_INDEX_PC;
        pSP = VM16_REGISTER_INDEX_SP;

        if (!InfoMapInitialized)
            InitInstructionInfoMap();
    }

    void InitInstructionInfoMap() {
        for (int i = 0; i < InstructionList.length; i++) {
            InstructionInfoMap[InstructionList[i].nCode & 0xff] = InstructionList[i];
        }
        InfoMapInitialized = true;
    }

    void ExecuteAnInstruction() {
        // 取操作码，PC并加1
        CurrentCode = (char) pMemory[(char) ((Register[pPC]++) & 0x0ffff)];
        byte OperatorCode = (byte) ((CurrentCode >> 8) & 0xff);
        // 根据高8位字节的值，调用相应的函数，注意成员函数的调用方式
        if (InstructionInfoMap[OperatorCode & 0xff] != null) {
            Method pFunc = null;
            try {
                pFunc = this.getClass().getDeclaredMethod(
                        InstructionInfoMap[OperatorCode & 0xff].pFunc);
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            }
            try {
                pFunc.invoke(this);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public int getMemery(int index) {
        return pMemory[index];
    }

    public void setMemery(int index, int value) {
        pMemory[index] = (short) value;
    }

    void SetPC(int Address) {
        Register[pPC] = (short) ((char) (Address & 0x0ffff));
    }

    String GetSendCommand() {
        return "\rg769\r";
    }

    String GetReceiveCommand() {
        return "\rg78b\r";
    }

    // //////////////////////////////////////////////////////////////////////////////
    //
    // 以下Method只能在CTec2kVM16类的各条指令执行函数中使用，在其他位置使用将出错！
    //

    // 取得目标寄存器指令的寄存器号，操作码的低2位字节是寄存器号，结果存index

    public int VM8_HIGH_REG_INDEX() {
        return (int) ((CurrentCode >> 2) & 3);
    }// 取得双寄存器指令的寄存器号。操作码的低2位字节是sr的寄存器号，结果存srindex
    // 操作码的第2，3位字节是dr的寄存器号，结果存drindex

    public int VM8_LOW_REG_INDEX() {
        return (int) (CurrentCode & 3);
    }// 取得单寄存器指令的寄存器号，操作码的低2位字节是寄存器号，结果存index

    public void VM8_SET_REG_VALUE(int index, byte value) {
        Register[index] = (byte) (value);
    }

    public char VM8_EXT_REG_VALUE(int index) {
        return ((char) ((short) Register[(index)] & 0x1ff));
    }

    public char VM8_EXT_LOW_REG_VALUE() {
        return (VM8_EXT_REG_VALUE(VM8_LOW_REG_INDEX()));
    }

    public void VM8_SET_FLAG_CARRY_ADD(char value) {
        FlagCarry = ((value) & 0x200) != 0;
    }

    public void VM8_SET_FLAG_CARRY_SUB(char value) {
        FlagCarry = ((value) & 0x200) == 0;
    }

    public void VM8_SET_FLAG_ZERO(char value) {
        FlagZero = (byte) (value) == 0;
    }

    public void VM8_SET_FLAG_OVER(char value) {
        FlagOver = ((((value) >> 1) ^ (value)) & 0x80) != 0;
    }

    public void VM8_SET_FLAG_SIGN(char value) {
        FlagSign = ((value) & 0x80) != 0;
    }

    public void VM8_SET_ALL_FLAGS_ADD(char value) {
        VM8_SET_FLAG_CARRY_ADD(value);
        VM8_SET_FLAG_ZERO(value);
        VM8_SET_FLAG_OVER(value);
        VM8_SET_FLAG_SIGN(value);
    }

    public void VM8_SET_ALL_FLAGS_SUB(char value) {
        VM8_SET_FLAG_CARRY_SUB(value);
        VM8_SET_FLAG_ZERO(value);
        VM8_SET_FLAG_OVER(value);
        VM8_SET_FLAG_SIGN(value);
    }

    public int VM16_DST_REG_INDEX() {
        return (int) ((CurrentCode >> 4) & 0x0f);
    }

    // 取得单寄存器指令的寄存器号，操作码的低2位字节是寄存器号，结果存index
    public int VM16_SRC_REG_INDEX() {
        return (int) (CurrentCode & 0x0f);
    }

    public long VM16_EXT_REG_VALUE(int index) {
        // return (int) ((Register[(index)] & 0x1ffff) );
        return (((int) Register[(index)]) & 0x1ffff) & 0x0ffffffff;
    }

    public long VM16_EXT_DST_REG_VALUE() {
        return (VM16_EXT_REG_VALUE(VM16_DST_REG_INDEX()));
    }

    public long VM16_EXT_SRC_REG_VALUE() {
        return (VM16_EXT_REG_VALUE(VM16_SRC_REG_INDEX()));
    }

    public char VM16_WORD_PARAM() {
        return (char) pMemory[(char) ((Register[pPC]++) & 0x0ffff)];

    }

    public byte VM16_BYTE_PARAM() {
        return (byte) (CurrentCode);
    }

    public void VM16_SET_REG_VALUE(int index, char value) {
        Register[index] = (short) (value & 0xffff);
    }

    public void VM16_SET_REG_VALUE(int index, short value) {
        Register[index] = value;
    }

    public void VM16_SET_FLAG_CARRY_ADD(long value) {
        FlagCarry = (((value) & 0x20000) != 0);
    }

    public void VM16_SET_FLAG_CARRY_SUB(long value) {
        FlagCarry = (((value) & 0x20000) == 0);
    }

    public void VM16_SET_FLAG_ZERO(long value) {
        FlagZero = ((short) (value) == 0);
    }

    public void VM16_SET_FLAG_OVER(long value) {
        FlagOver = (((((value) >> 1) ^ (value)) & 0x8000) != 0);
    }

    public void VM16_SET_FLAG_SIGN(long value) {
        FlagSign = (((value) & 0x8000) != 0);
    }

    public void VM16_SET_ALL_FLAGS_ADD(long value) {
        VM16_SET_FLAG_CARRY_ADD(value);
        VM16_SET_FLAG_ZERO(value);
        VM16_SET_FLAG_OVER(value);
        VM16_SET_FLAG_SIGN(value);
    }

    public void VM16_SET_ALL_FLAGS_SUB(long value) {
        VM16_SET_FLAG_CARRY_SUB(value);
        VM16_SET_FLAG_ZERO(value);
        VM16_SET_FLAG_OVER(value);
        VM16_SET_FLAG_SIGN(value);
    }

    public void VM16_PUSH_TO_STACK(short value) {
        pMemory[(int) (--(Register[pSP]) & 0x0ffff)] = (value);
    }

    public short VM16_POP_FROM_STACK() {
        return pMemory[(Register[pSP]++) & 0x0ffff];
    }

    public void VM16_JUMP_RELATIVE() {
        Register[pPC] += (VM16_BYTE_PARAM());
        // if ((char) (VM16_BYTE_PARAM() & 0xff) >> 7 == 1)
        // Register[pPC] += ((char) (VM16_BYTE_PARAM() & 0xff) | 0xff00);
        // else
        // Register[pPC] += ((char) (VM16_BYTE_PARAM() & 0xff) & 0x00ff);
    }

    public void VM16_JUMP_CONDITION(boolean boolcondition) {
        if (boolcondition)
            VM16_JUMP_RELATIVE();
    }

    // //////////////////////////////////////////////////////////////////////////////

    void ExecuteADD() {
        int dr = VM16_DST_REG_INDEX();

        long resValue = (VM16_EXT_REG_VALUE(dr) + VM16_EXT_SRC_REG_VALUE());

        VM16_SET_ALL_FLAGS_ADD(resValue);
        VM16_SET_REG_VALUE(dr, (char) resValue);
    }

    void ExecuteSUB() {
        int dr = VM16_DST_REG_INDEX();

        long resValue = (VM16_EXT_REG_VALUE(dr) - VM16_EXT_SRC_REG_VALUE()) & 0x0ffffffff;

        VM16_SET_ALL_FLAGS_SUB(resValue);

        VM16_SET_REG_VALUE(dr, (char) resValue);
    }

    void ExecuteAND() {
        int dr = VM16_DST_REG_INDEX();

        long resValue = (VM16_EXT_REG_VALUE(dr) & VM16_EXT_SRC_REG_VALUE());

        VM16_SET_ALL_FLAGS_ADD(resValue);
        VM16_SET_REG_VALUE(dr, (char) resValue);
    }

    void ExecuteCMP() {
        long resValue = (VM16_EXT_DST_REG_VALUE() - VM16_EXT_SRC_REG_VALUE());

        VM16_SET_ALL_FLAGS_SUB(resValue);
    }

    void ExecuteXOR() {
        int dr = VM16_DST_REG_INDEX();

        long resValue = (VM16_EXT_REG_VALUE(dr) ^ VM16_EXT_SRC_REG_VALUE());

        VM16_SET_ALL_FLAGS_ADD(resValue);
        VM16_SET_REG_VALUE(dr, (char) resValue);
    }

    void ExecuteTEST() {
        long resValue = (VM16_EXT_DST_REG_VALUE() & VM16_EXT_SRC_REG_VALUE());

        VM16_SET_ALL_FLAGS_ADD(resValue);
    }

    void ExecuteOR() {
        int dr = VM16_DST_REG_INDEX();

        long resValue = (VM16_EXT_REG_VALUE(dr) | VM16_EXT_SRC_REG_VALUE());

        VM16_SET_ALL_FLAGS_ADD(resValue);
        VM16_SET_REG_VALUE(dr, (char) resValue);
    }

    void ExecuteMVRR() {
        VM16_SET_REG_VALUE(VM16_DST_REG_INDEX(),
                (char) Register[VM16_SRC_REG_INDEX()]);
    }

    void ExecuteDEC() {
        int reg = VM16_DST_REG_INDEX();
        long value = VM16_EXT_REG_VALUE(reg);

        value--;

        VM16_SET_ALL_FLAGS_SUB(value);
        VM16_SET_REG_VALUE(reg, (char) value);
    }

    void ExecuteINC() {
        int reg = VM16_DST_REG_INDEX();
        long value = VM16_EXT_REG_VALUE(reg);

        value++;

        VM16_SET_ALL_FLAGS_ADD(value);
        VM16_SET_REG_VALUE(reg, (char) value);
    }

    void ExecuteSHL() {
        int reg = VM16_DST_REG_INDEX();

        char value = (char) Register[reg];
        FlagCarry = (value & 0x8000) != 0;

        VM16_SET_REG_VALUE(reg, (char) (value << 1));
    }

    void ExecuteSHR() {
        int reg = VM16_DST_REG_INDEX();
        char value = (char) Register[reg];
        FlagCarry = ((value & 1) == 0 ? false : true);

        VM16_SET_REG_VALUE(reg, (char) (value >> 1));
    }

    void ExecuteJR() {
        VM16_JUMP_RELATIVE();
    }

    void ExecuteJRC() {
        VM16_JUMP_CONDITION(FlagCarry);
    }

    void ExecuteJRNC() {
        VM16_JUMP_CONDITION(!FlagCarry);
        // VM16_JUMP_CONDITION(false);
    }

    void ExecuteJRZ() {
        VM16_JUMP_CONDITION(FlagZero);
    }

    void ExecuteJRNZ() {
        VM16_JUMP_CONDITION(!FlagZero);
    }

    void ExecuteJMPA() {
        char WordParam = VM16_WORD_PARAM();

        Register[pPC] = (short) (WordParam & 0x0ffff);
    }

    void ExecuteLDRR() {
        VM16_SET_REG_VALUE(VM16_DST_REG_INDEX(),
                pMemory[(char) (Register[VM16_SRC_REG_INDEX()] & 0xffff)]);
    }

    void ExecuteIN() {
        VM16_SET_REG_VALUE(0, getInStringChar((char) VM16_BYTE_PARAM() & 3));
    }

    private boolean hasUnreadKey = false;

    public void KeyPressed(char Key) {
        inChar = (Key == '\b') ? 0x7f : Key;
        hasUnreadKey = true;
    }


    private char getInStringChar(int port) {
        switch (port) {
            case 0:
                setHasUnreadKey(false);
                return inChar;

            case 1:
                if (isHasUnreadKey()) {
                    return (char) 3;
                } else {
                    return (char) 1;
                }

            default:
                return 0;
        }
    }

    void ExecuteSTRR() {
        pMemory[(char) Register[VM16_DST_REG_INDEX()]] = Register[VM16_SRC_REG_INDEX()];
    }

    void ExecutePSHF() {
        short FlagChar = (short) (FlagCarryValues[FlagCarry == false ? 0 : 1]
                | FlagZeroValues[FlagZero == false ? 0 : 1]
                | FlagOverValues[FlagOver == false ? 0 : 1] | FlagSignValues[FlagSign == false ? 0
                : 1]);

        VM16_PUSH_TO_STACK(FlagChar);
    }

    void ExecutePUSH() {
        VM16_PUSH_TO_STACK(Register[VM16_SRC_REG_INDEX()]);
    }

    void ExecuteOUT() {
        // pConsole->PortOut((unsigned char)VM16_BYTE_PARAM & 3, (unsigned
        // char)Register[0]);
        // Log.e("mr",(char)(Register[0]&0xff)+" :"+Integer.toHexString(Register[0]));
        // Log.e("mr", "R1:"+(char)(Register[1]&0xff));
        portOut((short) (VM16_BYTE_PARAM() & 3 & 0x0ff),
                (byte) (short) (Register[0] & 0x0ff));
    }

    private void portOut(int Port, byte Value) {
        if (Port != 0)
            return;

        switch (Value) {
            case '\r':
                outString.append((char) Value);
                // ColPos = 0;
                break;

            case '\n':
                // if (++RowPos == Height)
                // {
                // ScrollALine();
                // RowPos--;
                // }

                // String[] len = outString.split("\n");
                // if(len.length==18){
                // String newLine = System.getProperty("line.separator");
                // int mark = outString.indexOf(newLine);
                // outString = mark < 0 ? "" : outString.substring(mark +
                // newLine.length());
                // }
                outString.append((char) Value);
                break;

            case '\t':
                outString.append((char) Value);
                // do {
                // ScreenText[RowPos * Width + ColPos] = ' ';
                // pChildView->UpdateAChar(RowPos, ColPos++, Value);
                // } while ((ColPos & 7) && (ColPos < Width));
                // if (ColPos >= Width)
                // {
                // ColPos = 0;
                // if (++RowPos == Height)
                // {
                // ScrollALine();
                // RowPos--;
                // }
                // }
                break;

            case '\b':
                // if (ColPos)
                // ColPos--;
                // else if (RowPos)
                // {
                // RowPos--;
                // ColPos = Width - 1;
                // }
                outString.append((char) Value);
                break;

            case 0x1b:
                break;

            default:
                // ScreenText[RowPos * Width + ColPos] = Value;
                // pChildView->UpdateAChar(RowPos, ColPos, Value);
                // if (++ColPos == Width)
                // {
                // ColPos = 0;
                // if (++RowPos == Height)
                // {
                // ScrollALine();
                // RowPos--;
                // }
                // }
                outString.append((char) Value);

        }

        // outString += (char)Value;

        Message message = new Message();
        message.what = 1;
        handler.sendMessage(message);
    }

    void ExecutePOP() {
        VM16_SET_REG_VALUE(VM16_DST_REG_INDEX(), (char) VM16_POP_FROM_STACK());
    }

    void ExecuteMVRD() {
        VM16_SET_REG_VALUE(VM16_DST_REG_INDEX(), (char) VM16_WORD_PARAM());
    }

    void ExecutePOPF() {
        short FlagChar = VM16_POP_FROM_STACK();

        FlagCarry = (FlagChar & VM16_FLAG_BIT_CARRY) != 0;
        // FlagCarry = (FlagChar & 0x8000) != 0;
        FlagZero = (FlagChar & VM16_FLAG_BIT_ZERO) != 0;
        FlagOver = (FlagChar & VM16_FLAG_BIT_OVER) != 0;
        FlagSign = (FlagChar & VM16_FLAG_BIT_SIGN) != 0;
    }

    void ExecuteRET() {
        Register[pPC] = (short) ((char) (VM16_POP_FROM_STACK() & 0x0ffff));
    }

    void ExecuteCALA() {
        char WordParam = VM16_WORD_PARAM();

        VM16_PUSH_TO_STACK((short) (Register[pPC] & 0x0ffff));
        Register[pPC] = (short) (WordParam & 0x0ffff);
    }

    void ExecuteADC() {
        int dr = VM8_HIGH_REG_INDEX();

        char resValue = (char) (VM8_EXT_REG_VALUE(dr) + VM8_EXT_LOW_REG_VALUE() + (FlagCarry == true ? 1
                : 0));

        VM8_SET_ALL_FLAGS_ADD(resValue);
        VM8_SET_REG_VALUE(dr, (byte) resValue);
    }

    void ExecuteSBB() {
        int dr = VM16_DST_REG_INDEX();

        long resValue = (VM16_EXT_REG_VALUE(dr) - VM16_EXT_SRC_REG_VALUE() - (FlagCarry == true ? 0
                : 1));

        VM16_SET_ALL_FLAGS_SUB(resValue);
        VM16_SET_REG_VALUE(dr, (char) resValue);
    }

    void ExecuteRCL() {
        int reg = VM16_DST_REG_INDEX();

        short value = Register[reg];
        value <<= 1;
        if (FlagCarry)
            value |= 1;
        FlagCarry = (value & 0x10000) != 0;

        VM16_SET_REG_VALUE(reg, (char) value);
    }

    void ExecuteRCR() {
        int reg = VM16_DST_REG_INDEX();

        short value = Register[reg];
        if (FlagCarry)
            value |= 0x10000;
        else
            value &= 0xffff;
        FlagCarry = ((value & 1) == 1 ? true : false);

        VM16_SET_REG_VALUE(reg, (char) (value >> 1));
    }

    void ExecuteASR() {
        int reg = VM16_DST_REG_INDEX();

        short value = Register[reg];
        FlagCarry = ((value & 1) == 1 ? true : false);

        VM16_SET_REG_VALUE(reg, (char) (value >> 1));
    }

    void ExecuteNOT() {
        int reg = VM16_DST_REG_INDEX();
        VM16_SET_REG_VALUE(reg, (char) ~Register[reg]);
    }

    void ExecuteJMPR() {
        Register[pPC] = (short) (Register[VM16_SRC_REG_INDEX()] & 0x0ffff);
    }

    void ExecuteJRS() {
        VM16_JUMP_CONDITION(FlagSign);
    }

    void ExecuteJRNS() {
        VM16_JUMP_CONDITION(!FlagSign);
    }

    void ExecuteCLC() {
        FlagCarry = false;
    }

    void ExecuteSTC() {
        FlagCarry = true;
    }

    void ExecuteEI() {
        InterruptEnabled = true;
    }

    void ExecuteDI() {
        InterruptEnabled = false;
    }

    void ExecuteCALR() {
        VM16_PUSH_TO_STACK((short) (Register[pPC] & 0x0ffff));
        Register[pPC] = (short) (Register[VM16_SRC_REG_INDEX()] & 0x0ffff);
    }

    void ExecuteLDRA() {
        VM16_SET_REG_VALUE(VM16_DST_REG_INDEX(),
                (char) pMemory[VM16_WORD_PARAM()]);
    }

    void ExecuteLDRX() {
        VM16_SET_REG_VALUE(VM16_DST_REG_INDEX(),
                (char) pMemory[VM16_WORD_PARAM()
                        + (char) Register[VM16_SRC_REG_INDEX()]]);
    }

    void ExecuteSTRX() {
        pMemory[VM16_WORD_PARAM() + (char) Register[VM16_SRC_REG_INDEX()]] = Register[VM16_DST_REG_INDEX()];
    }

    void ExecuteSTRA() {
        pMemory[VM16_WORD_PARAM()] = Register[VM16_SRC_REG_INDEX()];
    }

    void ExecuteIRET() {
    }

    void ExecuteHALT() {
        ToEnd = true;
    }

    void SetRegister(int Index, int Value) {
        Register[Index] = (short) Value;
    }

    int GetRegister(int Index) {
        return Register[Index];
    }

    public String showMemory() {
        String memory = "";
        for (int i = 0; i < 200; i++) {
            memory += Integer.toHexString((int) pMemory[i]);
            memory += " ";
        }
        return memory;
    }

    public void WriteToMemory(int StartAddress, int Length, byte[] Buffer) {
        if (StartAddress < 0 || StartAddress >= 0x10000)
            return;

        if (StartAddress + Length > 0x10000)
            Length = 0x10000 - StartAddress;
        int pSrc = 0;
        int pDst = StartAddress;
        for (int i = 0; i < Length / 2; i++) {
            pMemory[pDst++] = (short) (((Buffer[pSrc] & 0xff) << 8) | ((Buffer[pSrc + 1] & 0xff)) & 0xffff);
            pSrc++;
            pSrc++;
        }
    }

    void ReadFromMemory(int StartAddress, int Length, byte[] Buffer) {
        if (StartAddress < 0 || StartAddress >= 0x10000)
            return;

        if (StartAddress + Length > 0x10000)
            Length = 0x10000 - StartAddress;
        int pSrc = StartAddress;
        int pDst = 0;
        for (int i = 0; i < Length; i++) {
            Buffer[pDst++] = (byte) (((pMemory[pSrc]) >> 8) & 0xff);
            Buffer[pDst++] = (byte) ((pMemory[pSrc++]) & 0xff);
        }
    }

    void SetStubAndRun(int StartAddress) {
        byte[] StartBuf = new byte[]{(byte) 0xce, 0, 0, 0, (byte) 0xf0, 0};
        StartBuf[2] = (byte) (StartAddress >> 8);
        StartBuf[3] = (byte) StartAddress;
        WriteToMemory(0xfff0, StartBuf.length, StartBuf);
        Run(0xfff0);
    }

    public void Run(int StartAddress) {
        SetPC(StartAddress);
        ToEnd = false;
        // pConsole->EnableCursor();
        // int PeekMessageCount = MESSAGE_PEEKING_CYCLE;
        // do {
        // ExecuteAnInstruction();
        // // if (!(--PeekMessageCount))
        // // {
        // // pConsole->DoMessageLoop();
        // // PeekMessageCount = MESSAGE_PEEKING_CYCLE;
        // // }
        // } while (!ToEnd);
        do {
            ExecuteAnInstruction();
        } while (!ToEnd);
        // pConsole->DisbleCursor();
        // pConsole->DirectMode();
        // if (ToDestroy)
        // {
        // pConsole->ClearScreen();
        // pConsole->ToDoNotify();
        // delete this;
        // }
    }

    int GetWordSize() {
        return 2;// 2个字节
    }

    String GetMonitorFileName() {
        return "monitor16.cod";
    }

    void GetSendStartLen(char pStart, char pLen) {
        pStart = (char) pMemory[VM16_SEND_DATA_START_ADDRESS];
        pLen = (char) pMemory[VM16_SEND_DATA_LENGTH];
    }

    void SetSendStartLen(int Start, int Len) {
        pMemory[VM16_SEND_DATA_START_ADDRESS] = (short) Start;
        pMemory[VM16_SEND_DATA_LENGTH] = (short) Len;
    }

    public StringBuffer getOutString() {
        return outString;
    }

    public void setOutString(StringBuffer outString) {
        this.outString = outString;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;

    }

    Handler handler;
    private char inChar;


    public boolean isHasUnreadKey() {
        return hasUnreadKey;
    }

    public void setHasUnreadKey(boolean hasUnreadKey) {
        this.hasUnreadKey = hasUnreadKey;
    }
}