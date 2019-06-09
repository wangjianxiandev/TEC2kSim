package com.dlut.tec2kSimVM;


public class InstructionInfo16 {
    String szName;
    byte nCode;
    int nType;
    String pFunc;

    public InstructionInfo16(String szName, byte nCode, int nType, String pFunc) {
        this.szName = szName;
        this.nCode = nCode;
        this.nType = nType;
        this.pFunc = pFunc;
    }

}
