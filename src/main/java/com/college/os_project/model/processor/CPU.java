package com.college.os_project.model.processor;

import com.college.os_project.model.assembler.Assembler;
import com.college.os_project.model.memory.Memory;
import com.college.os_project.model.memory.MemoryManager;

import java.util.ArrayList;
import java.util.Collections;

public class CPU  {
    private static Process activeProcess;

    // General purpose registers
    private static Register A, B, C, D;

    // special purpose registers - instruction register, program counter, status register - zero flag
    private static Register IR, PC, ZF;
    private static int timeQuantum = 1;
    private static ArrayList<Register> generalRegisters = new ArrayList<>();
    private Assembler assembler;

    public CPU(Assembler assembler) {
        activeProcess = null;
        A = new Register("A", "0000", 0);
        B = new Register("B", "0001", 0);
        C = new Register("C", "0010", 0);
        D = new Register("D", "0011", 0);
        Collections.addAll(generalRegisters, A, B, C, D);

        IR = new Register("Instruction register", "");
        PC = new Register("Program counter", 0);
        ZF = new Register("Zero flag", 0);
        this.assembler = assembler;
    }

    public void execute(Process process, long startTime) {
        activeProcess.setStartTime(startTime);

        while (process.isRunning() && System.currentTimeMillis() - startTime < timeQuantum) {
            IR.setStrValue(process.getInstruction(PC.getValue()));
            executeMachineCode();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("Error with thread.");
        }

        if (process.isDone()) {
            System.out.println("Process done");
            process.setEndTime(System.currentTimeMillis());
            MemoryManager.removeProcess(process);
            Memory.info();
        } else if (process.isBlocked()) {
            System.out.printf("Process with PID = %d is blocked, you need to unblock it first.\n", activeProcess.getPID());
        } else if (process.isTerminated()) {
            System.out.printf("Process with PID = %d is terminated.\n", activeProcess.getPID());
        } else {
            saveRegistersValues();
        }

        clearRegisters();
    }

    public void executeMachineCode() {
        boolean programCounterChanged = false;
        String instruction = IR.getStrValue().substring(0, 4);
        printRegisters();

        if (instruction.equals(assembler.getInstructionCode("HLT"))) {
            assembler.hlt();
        } else if (instruction.equals(assembler.getInstructionCode("MOV"))) {
            String r1 = IR.getStrValue().substring(4, 8);
            String r2 = IR.getStrValue().substring(8, 12);
            assembler.mov(r1, r2);
        } else if (instruction.equals(assembler.getInstructionCode("ADD"))) {
            String r1 = IR.getStrValue().substring(4, 8);
            String r2;

            if (IR.getStrValue().length() == 12) {
                r2 = IR.getStrValue().substring(8, 12);
            } else {
                r2 = IR.getStrValue().substring(8, 16);
            }
            assembler.add(r1, r2);
        } else if (instruction.equals(assembler.getInstructionCode("SUB"))) {
            String r1 = IR.getStrValue().substring(4, 8);
            String r2;

            if (IR.getStrValue().length() == 12) {
                r2 = IR.getStrValue().substring(8, 12);
            } else {
                r2 = IR.getStrValue().substring(8, 16);
            }
            assembler.sub(r1, r2);
        } else if (instruction.equals(assembler.getInstructionCode("INC"))) {
            String r1 = IR.getStrValue().substring(4, 8);
            assembler.inc(r1);
        } else if (instruction.equals(assembler.getInstructionCode("DEC"))) {
            String r1 = IR.getStrValue().substring(4, 8);
            assembler.dec(r1);
        } else if (instruction.equals(assembler.getInstructionCode("MUL"))) {
            String r1 = IR.getStrValue().substring(4, 8);
            String r2;

            if (IR.getStrValue().length() == 12) {
                r2 = IR.getStrValue().substring(8, 12);
            } else {
                r2 = IR.getStrValue().substring(8, 16);
            }
            assembler.mul(r1, r2);
        } else if (instruction.equals(assembler.getInstructionCode("CMP"))) {
            String r1 = IR.getStrValue().substring(4, 8);
            String r2;

            if (IR.getStrValue().length() == 12) {
                r2 = IR.getStrValue().substring(8, 12);
            } else {
                r2 = IR.getStrValue().substring(8, 16);
            }
            assembler.cmp(r1, r2);
        } else if (instruction.equals(assembler.getInstructionCode("JMP"))) {
            String r1 = IR.getStrValue().substring(4, 12);
            assembler.jmp(r1);
        } else if (instruction.equals(assembler.getInstructionCode("JZ"))) {
            String r1 = IR.getStrValue().substring(4, 12);
            programCounterChanged = assembler.jz(r1);
        } else if (instruction.equals(assembler.getInstructionCode("JNZ"))) {
            String r1 = IR.getStrValue().substring(4, 12);
            programCounterChanged = assembler.jnz(r1);
        }

        if (!programCounterChanged) {
            PC.incValue(1);
        }
    }

    public static Register getRegister(String addresss) {
        for (Register r : generalRegisters) {
            if (r.getAddress().equals(addresss)) {
                return r;
            }
        }

        return null;
    }

    public static String getRegisterCode(String registerName) {
        for (Register r : generalRegisters) {
            if (r.getName().equals(registerName)) {
                return r.getAddress();
            }
        }

        return null;
    }

    public static Process getActiveProcess() {
        return activeProcess;
    }

    public void setActiveProcess(Process activeProcess) {
        CPU.activeProcess = activeProcess;
    }

    public static void changeActiveProcessState(ProcessState state) {
        activeProcess.setState(state);
    }

    public static Register getPC() {
        return PC;
    }

    public static void setPCvalue(int value) {
        PC.setValue(value);
    }

    public static int getZFValue() {
        return ZF.getValue();
    }

    public static void setZFvalue(int value) {
        CPU.ZF.setValue(value);
    }

    private void clearRegisters() {
        for (Register r : generalRegisters) {
            r.setValue(0);
        }
    }

    private void saveRegistersValues() {
        int[] registersValues = {A.getValue(), B.getValue(), C.getValue(), D.getValue()};
        activeProcess.setValuesOfRegisters(registersValues);
        activeProcess.setPC(PC.getValue());
    }

    public void loadRegistersValues() {
        int[] registers = activeProcess.getRegisters();
        for (int i = 0; i < registers.length; i++) {
            generalRegisters.get(i).setValue(registers[i]);
        }

        PC.setValue(activeProcess.getPC());
    }

    public void printRegisters() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%-5s %-5s %-5s %-5s %-20s %-5s %-5s", "A", "B", "C", "D", "IR", "PC", "ZF")).append("\n");
        sb.append(String.format("%-5d %-5d %-5d %-5d %-20s %-5d %-5d", A.getValue(), B.getValue(), C.getValue(), D.getValue(), IR.getStrValue(), PC.getValue(), ZF.getValue()));
        sb.append("\n");

        System.out.println(sb);
    }
}
